package com.hellboy.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.hellboy.core.*;
import com.hellboy.entity.AutoComplte;
import com.hellboy.entity.Money;
import com.hellboy.entity.MoneyFlow;
import com.hellboy.entity.Stock;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.http.HttpRequest;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;


/**
 * User: fengping
 * Date: 2015/3/26 11:01
 * Description:
 */
@Controller
@RequestMapping("/")
public class StockController {
    private Logger logger = LoggerFactory.getLogger(StockController.class);
    ObjectMapper mapper = new ObjectMapper();
    public void init(){
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        TypeFactory typeFactory = mapper.getTypeFactory();
        CollectionType collectionType = typeFactory.constructCollectionType(List.class, MoneyFlow.class);
    }
    @RequestMapping(value="",produces = "text/plain;charset=UTF-8")
    public String get(Model model,HttpServletRequest req) throws IOException, ClassNotFoundException ,InterruptedException{
        String name = req.getParameter("sname");
        List<MoneyFlow> moneyFlows = Lists.newArrayList();
        if(Strings.isNullOrEmpty(name)){
            moneyFlows = MongoUtil.getMoneyFlow().stream()
                    .filter(x->{
                        return x.getMainnetmount()>10000;
                    })
                    .sorted((f1, f2) -> Double.compare(f2.getMainnetmount(), f1.getMainnetmount()))
                            //.sorted((f1, f2) -> Double.compare(f2.getChangeratio(), f1.getChangeratio()))
                    .collect(Collectors.toList());
        }else{
            moneyFlows = MongoUtil.getMoneyFlowByName(name);
        }
        model.addAttribute("moneyflows", moneyFlows);
        String flows = mapper.writeValueAsString(moneyFlows);
        model.addAttribute("flows",flows);
        return "index";
    }

    @RequestMapping(value="chart")
    public String chart(Model model) {
        return "chart";
    }

    @RequestMapping(value="getchart", produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String getChart(Model model,String num,HttpServletRequest req) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        List<MoneyFlow> moneyFlows = MongoUtil.getMoneyFlowHistory(num)
                .stream()
                .sorted((f1, f2) -> {
                    try {
                        return sdf.parse(f1.getTime()).compareTo(sdf.parse(f2.getTime()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return 0;
                })
                .collect(Collectors.toList());
        String value = mapper.writeValueAsString(moneyFlows);
        return value;
    }

    @RequestMapping(value="getstock", produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String getStock(Model model,String num,HttpServletRequest req) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        List<Stock> stocks = StockBase.stocks
                .stream()
                .filter(x->{
                    return x.getNum().indexOf(num)>-1;
                })
                .collect(Collectors.toList());
        List<AutoComplte> autoCompltes = Lists.newArrayList();
        for (Stock stock : stocks) {
            AutoComplte autoComplte = new AutoComplte();
            autoComplte.setName(stock.getName());
            autoComplte.setValue(stock.getNum());
            autoComplte.setTokens(Arrays.asList(ArrayUtils.toString(stock.getNum().toCharArray())));
            autoCompltes.add(autoComplte);
        }
        String value = mapper.writeValueAsString(autoCompltes);
        return value;
    }



}
