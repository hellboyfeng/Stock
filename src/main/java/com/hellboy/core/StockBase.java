package com.hellboy.core;

import com.google.common.collect.Lists;
import com.hellboy.entity.Money;
import com.hellboy.entity.MoneyFlow;
import com.hellboy.entity.Stock;

import java.io.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hellboy on 2015/10/4.
 */
public class StockBase {
    public static ExecutorService executor = null;
    public static List<Stock> stocks = Lists.newArrayList();
    public static List<Stock> astocks = Lists.newArrayList();
    public static List<Stock> resultStocks = Lists.newArrayList();
    public static List<MoneyFlow> moneyFlows = Lists.newArrayList();
    private static final String stockData = "e:\\stock\\stock.csv";
    private static final String serializeData = "e:\\stock\\stock.data";
    static{
        init();
    }
    public static void init(){
            executor = Executors.newFixedThreadPool(3);
            reSerialize();
           //getAllStock();
    }
    public static void serialize(){
        try {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(serializeData));
            output.writeObject(StockBase.astocks);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reSerialize() {
        try {
            FileInputStream fileIn = new FileInputStream(serializeData);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            stocks  = (List<Stock>) in.readObject();
            System.out.println(stocks.size());
            in.close();
            fileIn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getAllStock(){
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(stockData),"utf-8"));
            reader.readLine();
            String line = null;
            while((line=reader.readLine())!=null){
                Stock stock = new Stock();
                String num = "";
                String[] values = line.split(",");
                if(values[0].startsWith("3")){
                    continue;
                }
                if(values[0].startsWith("0")){
                    num = "sz"+values[0];
                }else{
                    num = "sh"+values[0];
                }
                stock.setName(values[1]);
                stock.setNum(num);
                stock.setIndustry(values[2]);
                stock.setArea(values[3]);
                stocks.add(stock);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
