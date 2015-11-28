package com.hellboy.core;

import com.hellboy.entity.Money;
import com.hellboy.entity.MoneyFlow;
import com.hellboy.entity.Stock;
import org.apache.commons.lang.time.DateFormatUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

/**
 * Created by hellboy on 2015/10/5.
 */
public class StockMain {
    public static void main(String[] args) throws InterruptedException, IOException, SchedulerException {

        schedule();
     /*   StockBase.resultStocks.clear();
        StockBase.moneyFlows.clear();
        long startTime=System.currentTimeMillis();   //获取开始时间
        //save();
        //read();
        //main();
        moneyFlow();
        StockBase.executor.shutdown();
        long endTime=System.currentTimeMillis(); //获取结束时间
        System.out.println("程序运行时间： " + (endTime - startTime) / 1000 + "ms");
        System.exit(1);*/
    }

    public static void schedule() throws SchedulerException {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(2);
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                StockBase.resultStocks.clear();
                StockBase.moneyFlows.clear();
                long startTime=System.currentTimeMillis();   //获取开始时间
                try {
                    moneyFlow();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //StockBase.executor.shutdown();
                long endTime=System.currentTimeMillis(); //获取结束时间
                System.out.println("程序运行时间： " + (endTime - startTime) / 1000 + "ms");
            }
        }, 1, 300, TimeUnit.SECONDS);

    }



    //实时出数据
    public static void moneyFlow() throws InterruptedException {
        List<Future<MoneyFlow>> results = StockBase.executor.invokeAll(asList(
                new MoneyFlowRun(0, 600), new MoneyFlowRun(600, 1200), new MoneyFlowRun(1200, 2296)
        ));
        String date = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        String path = "e:\\stock\\moneyflow.xls";
        List<MoneyFlow> moneyFlows = StockBase.moneyFlows
                .stream()
            /*    .filter(x->{
                    return x.getMainnetmount()>3000;
                })*/
                .sorted((p1,p2)->Double.compare(p2.getChangeratio(),p1.getChangeratio()))
                .collect(Collectors.toList());

        //PoiExcel.exportMoneyFlowExcel(moneyFlows, "资金流向", path, date);
        MongoUtil.saveMoneyFlow(moneyFlows, "moneyflow");
        MongoUtil.saveMoneyFlowHistory(StockBase.moneyFlows, "moneyflowhistory");
    }


    //实时出数据
    public static void main() throws InterruptedException {
        List<Future<Stock>> results = StockBase.executor.invokeAll(asList(
                new StockRun(0, 600), new StockRun(600, 1200), new StockRun(1200, 2296)
        ));
        String date = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        String path = "e:\\stock\\money.xls";
        List<Stock> data =  StockBase.resultStocks.stream()
                .filter(u -> {
                            Money money = u.getMoneylist().get(0);
                            return
                                    money.trade < 60
                                            //净流入率
                                             && money.getRatioamount() > 30
                                            //主力流入率
                                            && money.getR0_ratio() >= 10
                                            //散户流入率
                                            // && money.getR3_ratio() <0
                                            //涨跌幅大于0
                                            //&& money.getChangeratio() >= 0
                                            //净流入资金
                                            //&& money.getNetamount() > 5000
                                            //流入资金
                                            && money.getInamount() > 2000
                                            ;
                        }
                )
                .sorted((p1, p2) -> Double.compare(p1.getMoneylist().get(0).getChangeratio(), p2.getMoneylist().get(0).getChangeratio()))
                .collect(Collectors.toList());
        PoiExcel.exportExcel(data, "资金流入", path, 0);
        // MongoUtil.save(data);
        //StockBase.serialize();
    }





    //历史数据保存
    public static void save() throws InterruptedException {
        List<Future<Stock>> results = StockBase.executor.invokeAll(asList(
                new StockRun(0, 600), new StockRun(600, 1200), new StockRun(1200, 2296)
        ));
       // MongoUtil.save(StockBase.resultStocks,"stock");
        //StockBase.serialize();
    }
}
