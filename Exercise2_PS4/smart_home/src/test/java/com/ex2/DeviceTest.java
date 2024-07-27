package com.ex2;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ex2.devices.DoorLock;
import com.ex2.devices.Light;
import com.ex2.devices.Thermostat;
import com.ex2.core.Device;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("unused")
@ExtendWith(MockitoExtension.class)
class DeviceTest {

    @Test
    void testLightDevice() {
        Light light = new Light("test_light");
        assertEquals("light", light.getType());
        assertEquals("test_light", light.getId());
        assertEquals("off", light.getStatus());
        light.turnOn();
        assertEquals("on", light.getStatus());
        light.turnOff();
        assertEquals("off", light.getStatus());
    }

    @Test
    void testThermostatDevice() {
        Thermostat thermostat = new Thermostat("test_thermostat");
        assertEquals("thermostat", thermostat.getType());
        assertEquals("test_thermostat", thermostat.getId());
        assertEquals("set to 70°F", thermostat.getStatus());
        thermostat.setTemperature(75);
        assertEquals("set to 75°F", thermostat.getStatus());
        assertThrows(IllegalArgumentException.class, () -> thermostat.setTemperature(100));
    }

    @Test
    void testDoorLockDevice() {
        DoorLock doorLock = new DoorLock("test_door_lock");
        assertEquals("door lock", doorLock.getType());
        assertEquals("test_door_lock", doorLock.getId());
        assertEquals("locked", doorLock.getStatus());
        doorLock.turnOff();
        assertEquals("unlocked", doorLock.getStatus());
        doorLock.turnOn();
        assertEquals("locked", doorLock.getStatus());
    }
}