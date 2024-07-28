package com.ex2.factory;

import com.ex2.core.Device;
import com.ex2.devices.Light;
import com.ex2.devices.Thermostat;
import com.ex2.devices.DoorLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Factory class for creating different types of devices.
 */
public class DeviceFactory {
    private static final Logger logger = LogManager.getLogger(DeviceFactory.class.getName());

    /**
     * Create a device based on the given type and ID.
     * @param type The type of device to create.
     * @param id The unique identifier for the device.
     * @return The created Device object.
     * @throws IllegalArgumentException if an unknown device type is provided.
     */
    public static Device createDevice(String type, String id) {
        logger.info("Creating device of type: " + type + " with ID: " + id);
        switch (type.toLowerCase()) {
            case "light": return new Light(id);
            case "thermostat": return new Thermostat(id);
            case "doorlock": return new DoorLock(id);
            default: 
                throw new IllegalArgumentException("Unknown device type: " + type);
        }
    }
}