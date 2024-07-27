package com.ex2;

import org.junit.jupiter.api.*;

import com.ex2.devices.DoorLock;
import com.ex2.devices.Light;
import com.ex2.devices.Thermostat;
import com.ex2.factory.DeviceFactory;
import com.ex2.core.Device;

import static org.junit.jupiter.api.Assertions.*;

class DeviceFactoryTest {

    @Test
    void testCreateLight() {
        Device device = DeviceFactory.createDevice("light", "factory_light");
        assertTrue(device instanceof Light);
        assertEquals("light", device.getType());
        assertEquals("factory_light", device.getId());
    }

    @Test
    void testCreateThermostat() {
        Device device = DeviceFactory.createDevice("thermostat", "factory_thermostat");
        assertTrue(device instanceof Thermostat);
        assertEquals("thermostat", device.getType());
        assertEquals("factory_thermostat", device.getId());
    }

    @Test
    void testCreateDoorLock() {
        Device device = DeviceFactory.createDevice("door lock", "factory_door_lock");
        assertTrue(device instanceof DoorLock);
        assertEquals("door lock", device.getType());
        assertEquals("factory_door_lock", device.getId());
    }

    @Test
    void testCreateInvalidDevice() {
        assertThrows(IllegalArgumentException.class, () -> DeviceFactory.createDevice("invalid_type", "invalid_device"));
    }
}