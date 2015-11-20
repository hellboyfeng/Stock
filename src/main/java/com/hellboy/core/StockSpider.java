package com.hellboy.core;

import com.google.common.collect.Lists;
import com.hellboy.entity.Money;
import com.hellboy.entity.Stock;
import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.spi.LoggerFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.TypeFactory;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hellboy on 2015/10/3.
 */
public class StockSpider {
    private static CloseableHttpClient client;
    private static ObjectMapper mapper = new ObjectMapper();
    private static CollectionType collectionType;
    private static RequestConfig requestConfig;
    static Logger logger = org.slf4j.LoggerFactory.getLogger(StockSpider.class);
    static{
            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            TypeFactory typeFactory = mapper.getTypeFactory();
            collectionType = typeFactory.constructCollectionType(List.class,Money.class);

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
    public static List<Stock> run(int start,int end) {
        try{
            List<Stock> stocks = StockBase.stocks.subList(start, end);
            List<Stock> stockR = stocks.stream()
                    .filter(u -> {
                        Stock stock = httpGet(u);
                        if (stock == null)
                            return false;
                        return true;
                    })
                    .collect(Collectors.toList());
            System.out.println("start:"+start+" "+"end:"+end);
            System.out.println(stockR.size());
            return stockR;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Stock httpGet(Stock stock){
        HttpGet httpGet  = new HttpGet("http://money.finance.sina.com.cn/quotes_service/api/json_v2.php/MoneyFlow.ssx_ggzj_fszs?page=1&num=60&daima="+stock.getNum()+"&sort=time");
        httpGet.setConfig(requestConfig);
        List<Money> moneys = null;
        try {
            CloseableHttpResponse response = client.execute(httpGet);
            HttpEntity httpEntity = response.getEntity();
            String value = EntityUtils.toString(httpEntity);
            moneys = buildMoney(value);
            response.close();
            if(moneys == null){
                // logger.error(stock.getName()+"___"+stock.getNum());
                System.out.println(stock.getNum());
                return null;
            }
            if(stock.getMoneylist()!=null){
                stock.getMoneylist().clear();
            }
            //StockBase.astocks.add(stock);
            stock.setMoneylist(moneys);
            return stock;
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


    public static List<Money> buildMoney(String value) throws IOException {
        List<Money> money = null;
        try{
            int start = value.indexOf(",");
            int end = value.lastIndexOf("]");
            DecimalFormat fnum   =   new   DecimalFormat("##0.00");
            money = ((List<Money>)mapper.readValue(value.substring(start+1,end), collectionType)).stream()
                    .map(u -> {
                        u.setRatioamount(Double.valueOf(fnum.format(u.getRatioamount() * 100)));
                        u.setChangeratio(Double.valueOf(fnum.format(u.getChangeratio() * 100)));
                        u.setR0_ratio(u.getR0_ratio() * 100);
                        u.setR3_ratio(u.getR3_ratio() * 100);
                        u.setNetamount(u.getNetamount() / 10000.00);
                        u.setInamount(u.getInamount() / 10000.00);
                        u.setOutamount(u.getOutamount() / 10000.00);
                        return u;
                    }).collect(Collectors.toList());
        }catch (Exception e){
            return null;
        }
        return money;
    }



    public static void close(){
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
