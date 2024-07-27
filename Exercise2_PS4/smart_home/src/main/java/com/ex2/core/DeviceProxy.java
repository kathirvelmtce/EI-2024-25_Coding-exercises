package com.ex2.core;

import java.util.logging.Logger;

public class DeviceProxy implements Device {
    private static final Logger logger = Logger.getLogger(DeviceProxy.class.getName());
    private Device device;
    
    public DeviceProxy(Device device) {
        this.device = device;
        logger.info("Created proxy for device: " + device.getId());
    }
    
    @Override
    public String getId() {
        return device.getId();
    }
    
    @Override
    public String getType() {
        return device.getType();
    }
    
    @Override
    public void turnOn() {
        logger.info("Turning on " + device.getType() + " " + device.getId());
        device.turnOn();
    }
    
    @Override
    public void turnOff() {
        logger.info("Turning off " + device.getType() + " " + device.getId());
        device.turnOff();
    }
    
    @Override
    public String getStatus() {
        return device.getStatus();
    }
    
    // This method is added to allow access to the underlying device when needed
    public Device getDevice() {
        return device;
    }
}
