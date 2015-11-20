package com.hellboy.core;

import com.google.common.collect.Lists;
import com.hellboy.entity.Money;
import com.hellboy.entity.Stock;
import static java.util.Arrays.asList;

import java.io.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by hellboy on 2015/10/3.
 */
public class StockRun  implements Callable<Stock> {
    private static final int THRESHOLD = 100;
    private int start;
    private int end;
    public StockRun(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public Stock call() throws Exception {
        StockBase.resultStocks.addAll(StockSpider.run(start, end));
        return null;
    }
}
