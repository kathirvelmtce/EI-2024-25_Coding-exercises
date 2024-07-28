package com.ex2.devices;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ex2.core.Device;


/**
 * Represents a light device in the smart home system.
 */
public class Light implements Device {
    private static final Logger logger = LogManager.getLogger(Light.class.getName());
    private final String id;
    private boolean isOn = false;
    
    /**
     * Constructor for Light.
     * @param id The unique identifier for the light.
     */
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
        if (!isOn) {
            isOn = true; 
            logger.info("Light " + id + " turned on");
        }
    }
    
    @Override
    public void turnOff() { 
        if (isOn) {
            isOn = false; 
            logger.info("Light " + id + " turned off");
        }
    }
    
    @Override
    public String getStatus() { return isOn ? "on" : "off"; }
}