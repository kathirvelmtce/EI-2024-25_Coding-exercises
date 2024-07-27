package com.ex2;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ex2.core.SmartHomeSystem;

import java.time.LocalTime;
import java.util.concurrent.ScheduledExecutorService;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SmartHomeSystemTest {

    private SmartHomeSystem system;

    @Mock
    private ScheduledExecutorService schedulerMock;

    @BeforeEach
    void setUp() {
        system = SmartHomeSystem.getInstance();
    }

    @Test
    void testAddDevice() {
        system.addDevice("light", "test_light");
        assertDoesNotThrow(() -> system.turnOn("test_light"));
    }

    @Test
    void testAddDuplicateDevice() {
        system.addDevice("light", "duplicate_light");
        assertThrows(IllegalArgumentException.class, () -> system.addDevice("light", "duplicate_light"));
    }

    @Test
    void testRemoveDevice() {
        system.addDevice("light", "remove_light");
        system.removeDevice("remove_light");
        assertThrows(IllegalArgumentException.class, () -> system.turnOn("remove_light"));
    }

    @Test
    void testRemoveNonExistentDevice() {
        assertThrows(IllegalArgumentException.class, () -> system.removeDevice("non_existent_device"));
    }

    @Test
    void testTurnOnDevice() {
        system.addDevice("light", "on_light");
        system.turnOn("on_light");
        assertEquals("on", system.getStatus().split("\n")[0].split(" ")[3]);
    }

    @Test
    void testTurnOffDevice() {
        system.addDevice("light", "off_light");
        system.turnOn("off_light");
        system.turnOff("off_light");
        assertEquals("off", system.getStatus().split("\n")[0].split(" ")[3]);
    }

    @Test
    void testSetSchedule() {
        system.addDevice("light", "schedule_light");
        assertDoesNotThrow(() -> system.setSchedule("schedule_light", LocalTime.now().plusMinutes(1), true));
    }

    @Test
    void testAddTrigger() {
        system.addDevice("thermostat", "trigger_thermostat");
        system.addDevice("light", "trigger_light");
        system.addTrigger("trigger_thermostat > 75", "turnOn(trigger_light)");
        assertDoesNotThrow(() -> system.checkTriggers());
    }

    @Test
    void testInvalidTriggerCondition() {
        assertThrows(IllegalArgumentException.class, () -> system.addTrigger("invalid_condition", "turnOn(some_device)"));
    }

    @Test
    void testInvalidTriggerAction() {
        system.addDevice("thermostat", "action_thermostat");
        assertThrows(IllegalArgumentException.class, () -> system.addTrigger("action_thermostat > 75", "invalidAction(some_device)"));
    }
}