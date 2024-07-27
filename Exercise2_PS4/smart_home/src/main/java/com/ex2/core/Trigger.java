package com.ex2.core;

class Trigger {
    private final String condition;
    private final String action;
    
    public Trigger(String condition, String action) {
        this.condition = condition;
        this.action = action;
    }
    
    public String getCondition() { return condition; }
    public String getAction() { return action; }
}