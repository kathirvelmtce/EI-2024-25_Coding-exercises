package com.ex2.core;

/**
 * Represents a trigger in the smart home system.
 */
class Trigger {
    private final String condition;
    private final String action;
    private boolean hasFired;
    
    /**
     * Constructor for Trigger.
     * @param condition The condition for the trigger.
     * @param action The action to perform when the condition is met.
     */
    public Trigger(String condition, String action) {
        this.condition = condition;
        this.action = action;
        this.hasFired = false;
    }
    
    public String getCondition() { return condition; }
    public String getAction() { return action; }
    public boolean hasFired() { return hasFired; }
    public void setFired(boolean fired) { this.hasFired = fired; }
}