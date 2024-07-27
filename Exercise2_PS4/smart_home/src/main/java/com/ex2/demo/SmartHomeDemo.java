package com.ex2.demo;

import java.time.LocalTime;

import com.ex2.core.SmartHomeSystem;

public class SmartHomeDemo {
    public static void main(String[] args) {
        SmartHomeSystem system = SmartHomeSystem.getInstance();
        
        try {
            system.addDevice("light", "living_room_light");
            system.addDevice("thermostat", "main_thermostat");
            system.addDevice("door lock", "front_door");
            
            system.turnOn("living_room_light");
            system.setSchedule("front_door", LocalTime.of(22, 0), true);
            
            system.addObserver(device -> 
                System.out.println("Device updated: " + device.getType() + " " + device.getId() + " is " + device.getStatus())
            );
            
            system.addTrigger("main_thermostat > 75", "turnOn(living_room_light)");
            
            system.start();
        } catch (Exception e) {
            System.err.println("Error in SmartHomeDemo: " + e.getMessage());
        }
    }
}