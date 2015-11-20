package com.hellboy.core;

import com.hellboy.entity.MoneyFlow;
import com.hellboy.entity.Stock;

import java.util.concurrent.Callable;

/**
 * Created by hellboy on 2015/10/3.
 */
public class MoneyFlowRun implements Callable<MoneyFlow> {
    private static final int THRESHOLD = 100;
    private int start;
    private int end;
    public MoneyFlowRun(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public MoneyFlow call() throws Exception {
        StockBase.moneyFlows.addAll(MoneyFlowSpider.run(start, end));
        return null;
    }
}
