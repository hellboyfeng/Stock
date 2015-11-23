package com.hellboy.controller;

import com.hellboy.core.*;
import com.hellboy.entity.Money;
import com.hellboy.entity.MoneyFlow;
import com.hellboy.entity.Stock;
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
    @RequestMapping(value="")
    public String get(Model model) throws IOException, ClassNotFoundException ,InterruptedException{
        List<MoneyFlow> moneyFlows = MongoUtil.getMoneyFlow().stream()
                .filter(x->{
                    return true;
                    //return x.getMainnetmount()>60000;
                })
                .sorted((f1, f2) -> Double.compare(f2.getMainnetmount(), f1.getMainnetmount()))
                //.sorted((f1, f2) -> Double.compare(f2.getChangeratio(), f1.getChangeratio()))
                .collect(Collectors.toList());
        model.addAttribute("moneyflows", moneyFlows);
        String flows = mapper.writeValueAsString(moneyFlows);
        model.addAttribute("flows",flows);
        return "index";
    }

    @RequestMapping(value="chart")
    public String chart(Model model) {
        return "chart";
    }

    @RequestMapping(value="getchart")
    @ResponseBody
    public String getChart(Model model,String num,HttpServletRequest req) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
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


}
