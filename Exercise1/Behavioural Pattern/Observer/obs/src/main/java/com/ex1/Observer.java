package com.ex1;

import java.util.ArrayList;
import java.util.List;

// Subject interface
interface BatteryMonitor {
    void registerObserver(BatteryObserver observer);
    void removeObserver(BatteryObserver observer);
    void notifyObservers();
}

// Observer interface
interface BatteryObserver {
    void update(String deviceId, int batteryLevel, String healthStatus);
}

// Concrete Subject
class BatteryHealthMonitor implements BatteryMonitor {
    private List<BatteryObserver> observers;
    private String deviceId;
    private int batteryLevel;
    private String healthStatus;

    public BatteryHealthMonitor(String deviceId) {
        this.deviceId = deviceId;
        observers = new ArrayList<>();
    }

    @Override
    public void registerObserver(BatteryObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(BatteryObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (BatteryObserver observer : observers) {
            observer.update(deviceId, batteryLevel, healthStatus);
        }
    }

    public void setBatteryStatus(int batteryLevel, String healthStatus) {
        this.batteryLevel = batteryLevel;
        this.healthStatus = healthStatus;
        notifyObservers();
    }
}

// Concrete Observer
class BatteryHealthDisplay implements BatteryObserver {
    @Override
    public void update(String deviceId, int batteryLevel, String healthStatus) {
        System.out.println("Device " + deviceId + " - Battery Level: " + batteryLevel + "%, Health Status: " + healthStatus);
    }
}

// Concrete Observer
class BatteryAlertSystem implements BatteryObserver {
    @Override
    public void update(String deviceId, int batteryLevel, String healthStatus) {
        if (batteryLevel < 20 || "Poor".equals(healthStatus)) {
            System.out.println("ALERT: Device " + deviceId + " needs attention! Battery Level: " + batteryLevel + "%, Health: " + healthStatus);
        }
    }
}