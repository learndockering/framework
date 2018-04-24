package com.hisun.lemon.gateway.zuul.filter;

public enum FilterType {
    PRE, ROUTING, POST, ERROR;
    
    public String lowerCaseName() {
        return this.name().toLowerCase();
    }
}
