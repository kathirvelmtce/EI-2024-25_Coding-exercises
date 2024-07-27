package com.ex2.devices;

import java.util.logging.*;

import com.ex2.core.Device;

public class Thermostat implements Device {
    private static final Logger logger = Logger.getLogger(Thermostat.class.getName());
    private final String id;
    private int temperature = 70;
    
    public Thermostat(String id) {
        this.id = id;
        logger.info("Created new Thermostat with ID: " + id);
    }
    
    @Override
    public String getId() { return id; }
    
    @Override
    public String getType() { return "thermostat"; }
    
    @Override
    public void turnOn() { 
        logger.info("Thermostat " + id + " operation not applicable");
    }
    
    @Override
    public void turnOff() { 
        logger.info("Thermostat " + id + " operation not applicable");
    }
    
    @Override
    public String getStatus() { return "set to " + temperature + "°F"; }
    
    public void setTemperature(int temp) {
        if (temp < 40 || temp > 90) {
            throw new IllegalArgumentException("Temperature must be between 40°F and 90°F");
        }
        temperature = temp;
        logger.info("Thermostat " + id + " temperature set to " + temp + "°F");
    }
    
    public int getTemperature() { return temperature; }
}