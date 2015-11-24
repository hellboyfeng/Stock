package com.hellboy.entity;

import java.util.List;

/**
 * Created by hellboy on 2015/7/29.
 */
public class AutoComplte {
    private String name;
    private String value;
    private List<String> tokens;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }
}
