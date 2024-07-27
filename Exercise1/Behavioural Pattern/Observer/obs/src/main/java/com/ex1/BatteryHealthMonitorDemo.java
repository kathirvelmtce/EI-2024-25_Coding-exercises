package com.ex1;


// Usage example
public class BatteryHealthMonitorDemo {
    public static void main(String[] args) {
        BatteryHealthMonitor device1Monitor = new BatteryHealthMonitor("Device1");
        BatteryHealthMonitor device2Monitor = new BatteryHealthMonitor("Device2");

        BatteryHealthDisplay display = new BatteryHealthDisplay();
        BatteryAlertSystem alertSystem = new BatteryAlertSystem();

        device1Monitor.registerObserver(display);
        device1Monitor.registerObserver(alertSystem);
        device2Monitor.registerObserver(display);
        device2Monitor.registerObserver(alertSystem);

        // Simulate battery status changes
        device1Monitor.setBatteryStatus(75, "Good");
        device2Monitor.setBatteryStatus(30, "Fair");
        device1Monitor.setBatteryStatus(15, "Poor");
        device2Monitor.setBatteryStatus(50, "Good");

        // Remove alert system from Device2
        device2Monitor.removeObserver(alertSystem);
        device2Monitor.setBatteryStatus(10, "Poor");
    }
}