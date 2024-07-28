package com.ex2.core;

/**
 * Observer interface for device updates.
 */
public interface DeviceObserver {
    /**
     * Called when a device is updated.
     * @param device The updated device.
     */
    void update(Device device);
}
