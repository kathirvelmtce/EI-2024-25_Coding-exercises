package com.ex2.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Proxy class for devices, providing logging and additional functionality.
 */
public class DeviceProxy implements Device {
    private static final Logger logger = LogManager.getLogger(DeviceProxy.class);
    private Device device;
    
    /**
     * Constructor for DeviceProxy.
     * @param device The actual device to proxy.
     */
    public DeviceProxy(Device device) {
        this.device = device;
        logger.info("Created proxy for device: {}", device.getId());
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
        logger.info("Turning on {} {}", device.getType(), device.getId());
        device.turnOn();
    }
    
    @Override
    public void turnOff() {
        logger.info("Turning off {} {}", device.getType(), device.getId());
        device.turnOff();
    }
    
    @Override
    public String getStatus() {
        return device.getStatus();
    }
    
    /**
     * Get the underlying device.
     * @return The actual device.
     */
    public Device getDevice() {
        return device;
    }
}
