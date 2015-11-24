package com.hellboy.entity;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hellboy on 2015/10/3.
 */
public class Stock implements Serializable {
    private String name;
    private String num;
    private String industry;
    private String area;
    private List<Money> moneylist;


    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }
    @JsonIgnore
    public List<Money> getMoneylist() {
        return moneylist;
    }

    public void setMoneylist(List<Money> moneylist) {
        this.moneylist = moneylist;
    }

}
