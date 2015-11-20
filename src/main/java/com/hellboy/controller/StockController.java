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
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

    @RequestMapping(value="")
    public String get(Model model) throws IOException, ClassNotFoundException ,InterruptedException{
        List<MoneyFlow> moneyFlows = MongoUtil.getMoneyFlow();
        model.addAttribute("moneyflows",moneyFlows);
        return "index";
    }

    @RequestMapping(value="chart")
    public String chart(Model model) {
        return "chart";
    }

    @RequestMapping(value="getx")
    @ResponseBody
    public String getX(Model model,String num,HttpServletRequest req) throws IOException {
        List<MoneyFlow> moneyFlows = MongoUtil.getMoneyFlowHistory(num);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        TypeFactory typeFactory = mapper.getTypeFactory();
        CollectionType collectionType = typeFactory.constructCollectionType(List.class, MoneyFlow.class);
        String value = mapper.writeValueAsString(moneyFlows);
        return value;
    }


}
