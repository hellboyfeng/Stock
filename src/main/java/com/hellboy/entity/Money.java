package com.hellboy.entity;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Created by hellboy on 2015/10/3.
 */
public class Money {
    private String opendate;
    private String ticktime;
    //价格
    public double trade;
    //涨跌幅
    private double changeratio;
    //流入资金/万
    private double inamount;
    //流出资金/万
    private double outamount;
    //净流入/万
    private double netamount;
    //净流入率
    private double ratioamount;
    //主力流入率
    private double r0_ratio;
    //散户流入率
    private double r3_ratio;

    public String getOpendate() {
        return opendate;
    }

    public void setOpendate(String opendate) {
        this.opendate = opendate;
    }

    public String getTicktime() {
        return ticktime;
    }

    public void setTicktime(String ticktime) {
        this.ticktime = ticktime;
    }

    public double getTrade() {
        return trade;
    }

    public void setTrade(double trade) {
        this.trade = trade;
    }
    public double getChangeratio() {
        return changeratio;
    }

    public void setChangeratio(double changeratio) {
        this.changeratio = changeratio;
    }

    public double getInamount() {
        return inamount;
    }

    public void setInamount(double inamount) {
        this.inamount = inamount;
    }

    public double getOutamount() {
        return outamount;
    }

    public void setOutamount(double outamount) {
        this.outamount = outamount;
    }

    public double getNetamount() {
        return netamount;
    }

    public void setNetamount(double netamount) {
        this.netamount = netamount;
    }

    public double getRatioamount() {
        return ratioamount;
    }

    public void setRatioamount(double ratioamount) {
        this.ratioamount = ratioamount;
    }

    public double getR0_ratio() {
        return r0_ratio;
    }

    public void setR0_ratio(double r0_ratio) {
        this.r0_ratio = r0_ratio;
    }

    public double getR3_ratio() {
        return r3_ratio;
    }

    public void setR3_ratio(double r3_ratio) {
        this.r3_ratio = r3_ratio;
    }

}
