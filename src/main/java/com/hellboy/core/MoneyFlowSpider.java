package com.hellboy.core;

import com.google.common.collect.Lists;
import com.hellboy.entity.Money;
import com.hellboy.entity.MoneyFlow;
import com.hellboy.entity.Stock;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.TypeFactory;
import org.slf4j.Logger;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hellboy on 2015/10/3.
 */
public class MoneyFlowSpider {
    private static CloseableHttpClient client;
    private static RequestConfig requestConfig;
    private static ObjectMapper mapper = new ObjectMapper();
    static Logger logger = org.slf4j.LoggerFactory.getLogger(MoneyFlowSpider.class);
    static{
            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            TypeFactory typeFactory = mapper.getTypeFactory();

            requestConfig = RequestConfig.custom().setSocketTimeout(6000).setConnectTimeout(6000).build();
            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
            cm.setMaxTotal(200);
            cm.setDefaultMaxPerRoute(20);
            HttpHost proxy = new HttpHost("192.168.4.120", 3128);
            cm.setMaxPerRoute(new HttpRoute(proxy), 80);
            DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
            client = HttpClients.custom()
                    .setRoutePlanner(routePlanner)
                    .setConnectionManager(cm)
                    .build();

    }
    public static List<MoneyFlow> run(int start,int end) {
        try{
            List<Stock> stocks = StockBase.stocks.subList(start, end);
            List<MoneyFlow> moneyFlows = Lists.newArrayList();
          /*  stocks.stream()
                    .filter(u -> {
                        MoneyFlow moneyFlow = httpGet(u);
                        if (moneyFlow == null)
                            return false;
                        moneyFlows.add(moneyFlow);
                        return true;
                    }).collect(Collectors.toList());*/
            for(Stock stock:stocks){
                MoneyFlow moneyFlow = httpGet(stock);
                if (moneyFlow == null)
                    continue;
                moneyFlows.add(moneyFlow);
            }
            System.out.println("start:" + start + " " + "end:" + end);
            System.out.println(moneyFlows.size());
            return moneyFlows;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static MoneyFlow httpGet(Stock stock){
        HttpGet httpGet  = new HttpGet("http://money.finance.sina.com.cn/quotes_service/api/json_v2.php/MoneyFlow.ssi_ssfx_flzjtj?daima="+stock.getNum());
        httpGet.setConfig(requestConfig);
        MoneyFlow moneyFlow = null;
        try {
            CloseableHttpResponse response = client.execute(httpGet);
            HttpEntity httpEntity = response.getEntity();
            String value = EntityUtils.toString(httpEntity);
            if(value.equals("null"))
                return null;
            moneyFlow = buildMoneyFlow(value);
            response.close();
            if(moneyFlow == null){
                // logger.error(stock.getName()+"___"+stock.getNum());
                System.out.println(stock.getNum());
                return null;
            }
            moneyFlow.setNum(stock.getNum());
            moneyFlow.setStock(stock);
            return moneyFlow;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(stock.getNum() + "===" + stock.getName());
            logger.error(e.getMessage());
            return null;
        }
    }

/*
    public static String getHttpNum(){
        HttpGet httpGet  = new HttpGet("http://money.finance.sina.com.cn/quotes_service/api/json_v2.php/MoneyFlow.ssx_ggzj_fszs?page=1&num=60&daima=sz002308&sort=time");
        httpGet.setConfig(requestConfig);
        try {
            CloseableHttpResponse response = client.execute(httpGet);
            HttpEntity httpEntity = response.getEntity();
            String value = EntityUtils.toString(httpEntity);
            int end = StringUtils.indexOf(value, "\",");
            String result = value.substring(2, end);
            response.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return "";
        }
    }
*/




    public static MoneyFlow buildMoneyFlow(String value) throws IOException {
        DecimalFormat fnum  =   new  DecimalFormat("##0.00");
        String result = value.substring(value.indexOf("(") + 1, value.lastIndexOf(")"));
        MoneyFlow moneyFlow = mapper.readValue(result,MoneyFlow.class);
        moneyFlow.setMainin(Double.valueOf(fnum.format(moneyFlow.getR0_in()/10000)));
        moneyFlow.setMainout(Double.valueOf(fnum.format(moneyFlow.getR0_out()/10000)));
        moneyFlow.setZllp(moneyFlow.getR0x_ratio());
        BigDecimal b1 = new BigDecimal(Double.toString(moneyFlow.getR0_in()));
        BigDecimal b2 = new BigDecimal(Double.toString(moneyFlow.getR0_out()));
        moneyFlow.setMainnetmount(Double.valueOf(fnum.format(b1.subtract(b2).doubleValue()/10000)));
        moneyFlow.setNetamount(Double.valueOf(fnum.format(moneyFlow.getNetamount() / 10000)));
        moneyFlow.setTurnover(Double.valueOf(fnum.format(moneyFlow.getTurnover() / 100)));
        moneyFlow.setChangeratio(Double.valueOf(fnum.format(moneyFlow.getChangeratio() * 100)));
        moneyFlow.setDate( DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
        moneyFlow.setTime( DateFormatUtils.format(new Date(), "HH:mm:ss"));
        return moneyFlow;
    }


    public static void close(){
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
