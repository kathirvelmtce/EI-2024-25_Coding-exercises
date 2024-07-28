package com.ex2.devices;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ex2.core.Device;
import com.ex2.core.SmartHomeSystem;

/**
 * Represents a thermostat device in the smart home system.
 */
public class Thermostat implements Device {
    private static final Logger logger = LogManager.getLogger(Thermostat.class.getName());
    private final String id;
    private int temperature = 70;
    
    /**
     * Constructor for Thermostat.
     * @param id The unique identifier for the thermostat.
     */
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
    
    /**
     * Set the temperature of the thermostat.
     * @param temp The temperature to set.
     * @throws IllegalArgumentException if the temperature is out of range.
     */
    public void setTemperature(int temp) {
        if (temp < 40 || temp > 90) {
            throw new IllegalArgumentException("Temperature must be between 40°F and 90°F");
        }
        temperature = temp;
        logger.info("Thermostat " + id + " temperature set to " + temp + "°F");
        SmartHomeSystem.getInstance().notifyObservers(this);
    }
    
    /**
     * Get the current temperature of the thermostat.
     * @return The current temperature.
     */
    public int getTemperature() { return temperature; }
}