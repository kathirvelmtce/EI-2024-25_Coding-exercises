package com.ex2.core;

/**
 * Represents a generic device in the smart home system.
 */
public interface Device {
    /**
     * Get the unique identifier of the device.
     * @return The device ID.
     */
    String getId();

    /**
     * Get the type of the device.
     * @return The device type.
     */
    String getType();

    /**
     * Turn on the device.
     */
    void turnOn();

    /**
     * Turn off the device.
     */
    void turnOff();

    /**
     * Get the current status of the device.
     * @return The device status.
     */
    String getStatus();
}