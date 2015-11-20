package com.hellboy.entity;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created by ERP-05 on 2015/10/27.
 */
@JsonIgnoreProperties({ "r0", "r1_in","r1_out", "r1","r2_in", "r2_out","r2", "r3_in","r3_out", "r3","curr_capital"})
public class MoneyFlow {
    private Stock stock;
    private String name;
    private String num;
    //价格
    private double trade;
    //涨幅
    private double changeratio;
    //成交量
    private double volume;
    //换手率
    private double turnover;
    //主力罗盘
    private double zllp;
    //净流入
    private double netamount;
    //主力流入
    private double mainin;
    //主力流出
    private double mainout;
    //主力净流入
    private double mainnetmount;

    private String date;

    private String time;

    //主力流入
    private double r0_in;
    //主力流出
    private double r0_out;
    //主力罗盘
    private double r0x_ratio;

    @JsonIgnore
    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
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

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getTurnover() {
        return turnover;
    }

    public void setTurnover(double turnover) {
        this.turnover = turnover;
    }
    @JsonIgnore
    public double getZllp() {
        return zllp;
    }

    public void setZllp(double zllp) {
        this.zllp = zllp;
    }

    public double getNetamount() {
        return netamount;
    }

    public void setNetamount(double netamount) {
        this.netamount = netamount;
    }
    @JsonIgnore
    public double getMainin() {
        return mainin;
    }

    public void setMainin(double mainin) {
        this.mainin = mainin;
    }
    @JsonIgnore
    public double getMainout() {
        return mainout;
    }

    public void setMainout(double mainout) {
        this.mainout = mainout;
    }
    @JsonIgnore
    public double getMainnetmount() {
        return mainnetmount;
    }

    public void setMainnetmount(double mainnetmount) {
        this.mainnetmount = mainnetmount;
    }

    public double getR0_in() {
        return r0_in;
    }

    public void setR0_in(double r0_in) {
        this.r0_in = r0_in;
    }

    public double getR0_out() {
        return r0_out;
    }

    public void setR0_out(double r0_out) {
        this.r0_out = r0_out;
    }

    public double getR0x_ratio() {
        return r0x_ratio;
    }

    public void setR0x_ratio(double r0x_ratio) {
        this.r0x_ratio = r0x_ratio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @JsonIgnore
    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
