package com.ex2.demo;

import java.time.LocalTime;
import com.ex2.core.SmartHomeSystem;

/**
 * Demo class to showcase the functionality of the Smart Home System.
 */
public class SmartHomeDemo {
    public static void main(String[] args) {
        SmartHomeSystem system = SmartHomeSystem.getInstance();
        
        try {
            // Add devices
            system.addDevice("light", "living_room_light");
            system.addDevice("thermostat", "main_thermostat");
            system.addDevice("doorlock", "front_door");
            
            // Turn on a device
            system.turnOn("living_room_light");
            
            // Set a schedule
            system.setSchedule("front_door", LocalTime.of(22, 0), true);
            
            // Add an observer
            system.addObserver(device -> 
                System.out.println("Device updated: " + device.getType() + " " + device.getId() + " is " + device.getStatus())
            );
            
            // Add triggers
            system.addTrigger("main_thermostat > 75", "turnOn(living_room_light)");
            system.addTrigger("main_thermostat < 50", "turnOff(living_room_light)");
            
            // Start the system
            system.start();
        } catch (Exception e) {
            System.err.println("Error in SmartHomeDemo: " + e.getMessage());
        }
    }
}
