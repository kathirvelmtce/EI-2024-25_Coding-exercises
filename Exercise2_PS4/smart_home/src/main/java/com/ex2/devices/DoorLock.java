package com.ex2.devices;

import java.util.logging.*;

import com.ex2.core.Device;

public class DoorLock implements Device {
    private static final Logger logger = Logger.getLogger(DoorLock.class.getName());
    private final String id;
    private boolean isLocked = true;
    
    public DoorLock(String id) {
        this.id = id;
        logger.info("Created new DoorLock with ID: " + id);
    }
    
    @Override
    public String getId() { return id; }
    
    @Override
    public String getType() { return "door lock"; }
    
    @Override
    public void turnOn() { 
        isLocked = true; 
        logger.info("Door lock " + id + " locked");
    }
    
    @Override
    public void turnOff() { 
        isLocked = false; 
        logger.info("Door lock " + id + " unlocked");
    }
    
    @Override
    public String getStatus() { return isLocked ? "locked" : "unlocked"; }
}