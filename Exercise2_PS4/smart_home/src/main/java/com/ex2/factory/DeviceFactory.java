package com.ex2.factory;

import com.ex2.core.Device;
import com.ex2.devices.Light;
import com.ex2.devices.Thermostat;
import com.ex2.devices.DoorLock;

import java.util.logging.*;

public class DeviceFactory {
    private static final Logger logger = Logger.getLogger(DeviceFactory.class.getName());

    public static Device createDevice(String type, String id) {
        logger.info("Creating device of type: " + type + " with ID: " + id);
        switch (type.toLowerCase()) {
            case "light": return new Light(id);
            case "thermostat": return new Thermostat(id);
            case "door lock": return new DoorLock(id);
            default: 
                throw new IllegalArgumentException("Unknown device type: " + type);
        }
    }
}