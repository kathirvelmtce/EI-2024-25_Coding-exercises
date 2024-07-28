package com.ex2.devices;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ex2.core.Device;

/**
 * Represents a door lock device in the smart home system.
 */
public class DoorLock implements Device {
    private static final Logger logger = LogManager.getLogger(DoorLock.class.getName());
    private final String id;
    private boolean isLocked = true;
    
    /**
     * Constructor for DoorLock.
     * @param id The unique identifier for the door lock.
     */
    public DoorLock(String id) {
        this.id = id;
        logger.info("Created new DoorLock with ID: " + id);
    }
    
    @Override
    public String getId() { return id; }
    
    @Override
    public String getType() { return "doorlock"; }
    
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