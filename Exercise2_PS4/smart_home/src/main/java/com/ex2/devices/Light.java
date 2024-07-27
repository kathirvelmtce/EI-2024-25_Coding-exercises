package com.ex2.devices;

import java.util.logging.*;

import com.ex2.core.Device;

public class Light implements Device {
    private static final Logger logger = Logger.getLogger(Light.class.getName());
    private final String id;
    private boolean isOn = false;
    
    public Light(String id) {
        this.id = id;
        logger.info("Created new Light with ID: " + id);
    }
    
    @Override
    public String getId() { return id; }
    
    @Override
    public String getType() { return "light"; }
    
    @Override
    public void turnOn() { 
        isOn = true; 
        logger.info("Light " + id + " turned on");
    }
    
    @Override
    public void turnOff() { 
        isOn = false; 
        logger.info("Light " + id + " turned off");
    }
    
    @Override
    public String getStatus() { return isOn ? "on" : "off"; }
}

