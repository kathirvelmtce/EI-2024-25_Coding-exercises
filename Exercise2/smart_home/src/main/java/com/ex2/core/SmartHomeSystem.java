package com.ex2.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.*;

import com.ex2.factory.DeviceFactory;
import com.ex2.devices.Thermostat;

/**
 * Main class for the Smart Home System.
 * Implements the Singleton pattern.
 */
public class SmartHomeSystem {
    private static SmartHomeSystem instance;
    private final Map<String, DeviceProxy> devices = new ConcurrentHashMap<>();
    private final List<DeviceObserver> observers = new CopyOnWriteArrayList<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final List<Trigger> triggers = new CopyOnWriteArrayList<>();
    private static final Logger logger = LogManager.getLogger(SmartHomeSystem.class);

    private SmartHomeSystem() {
        logger.info("SmartHomeSystem initialized");
        startTriggerChecker();
    }

    /**
     * Get the singleton instance of SmartHomeSystem.
     * @return The SmartHomeSystem instance.
     */
    public static synchronized SmartHomeSystem getInstance() {
        if (instance == null) {
            instance = new SmartHomeSystem();
        }
        return instance;
    }

    /**
     * Stop the scheduler and shutdown the system.
     */
    public void stop() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }

    /**
     * Start the periodic trigger checker.
     */
    private void startTriggerChecker() {
        scheduler.scheduleAtFixedRate(this::checkTriggers, 0, 5, TimeUnit.SECONDS);
    }

    /**
     * Add a new device to the system.
     * @param type The type of the device.
     * @param id The unique identifier for the device.
     */
    public void addDevice(String type, String id) {
        if (devices.containsKey(id)) {
            throw new IllegalArgumentException("Device with ID " + id + " already exists");
        }
        Device device = DeviceFactory.createDevice(type, id);
        DeviceProxy proxy = new DeviceProxy(device);
        devices.put(id, proxy);
        notifyObservers(proxy);
        logger.info("Added " + type + " with ID " + id);
    }

    /**
     * Remove triggers associated with a specific device.
     * @param deviceId The ID of the device.
     */
    private void removeTriggersByDevice(String deviceId) {
        triggers.removeIf(trigger -> 
            trigger.getCondition().startsWith(deviceId) || 
            trigger.getAction().contains("(" + deviceId + ")")
        );
        logger.info("Removed triggers associated with device: " + deviceId);
    }

    /**
     * Remove a device from the system.
     * @param id The ID of the device to remove.
     */
    public void removeDevice(String id) {
        DeviceProxy proxy = devices.remove(id);
        if (proxy != null) {
            notifyObservers(proxy);
            removeTriggersByDevice(id);
            logger.info("Removed " + proxy.getType() + " with ID " + id);
            System.out.println("Device removed: " + proxy.getType() + " with ID " + id);
        } else {
            throw new IllegalArgumentException("Device with ID " + id + " does not exist");
        }
    }

    /**
     * Turn on a device.
     * @param id The ID of the device to turn on.
     */
    public void turnOn(String id) {
        DeviceProxy proxy = getDeviceProxyById(id);
        proxy.turnOn();
        notifyObservers(proxy);
        resetTriggers();
    }
    
    /**
     * Turn off a device.
     * @param id The ID of the device to turn off.
     */
    public void turnOff(String id) {
        DeviceProxy proxy = getDeviceProxyById(id);
        proxy.turnOff();
        notifyObservers(proxy);
        resetTriggers();
    }

    /**
     * Set a schedule for a device to turn on or off at a specific time.
     * @param id The ID of the device.
     * @param time The time to schedule the action.
     * @param turnOn True to turn on, false to turn off.
     */
    public void setSchedule(String id, LocalTime time, boolean turnOn) {
        DeviceProxy proxy = getDeviceProxyById(id);
        LocalTime now = LocalTime.now();
        long initialDelay = now.until(time, java.time.temporal.ChronoUnit.SECONDS);
        if (initialDelay < 0) initialDelay += 24 * 60 * 60;

        scheduler.scheduleAtFixedRate(() -> {
            if (turnOn) turnOn(id);
            else turnOff(id);
        }, initialDelay, 24 * 60 * 60, TimeUnit.SECONDS);

        logger.info("Scheduled " + proxy.getType() + " " + id + " to turn " + (turnOn ? "on" : "off") + " at " + time);
    }

    /**
     * Set the temperature for a thermostat device.
     * @param id The ID of the thermostat.
     * @param temperature The temperature to set.
     */
    public void setTemperature(String id, int temperature) {
        DeviceProxy proxy = getDeviceProxyById(id);
        if (proxy.getDevice() instanceof Thermostat) {
            ((Thermostat) proxy.getDevice()).setTemperature(temperature);
            logger.info("Set temperature of {} to {}°F", id, temperature);
            notifyObservers(proxy);
            checkTriggers();
            resetTriggers();
        } else {
            throw new IllegalArgumentException("Device " + id + " is not a thermostat");
        }
    }

    /**
     * Add an observer to the system.
     * @param observer The observer to add.
     */
    public void addObserver(DeviceObserver observer) {
        observers.add(observer);
        logger.info("Added new observer");
    }

    /**
     * Notify all observers about a device update.
     * @param device The updated device.
     */
    public void notifyObservers(Device device) {
        for (DeviceObserver observer : observers) {
            observer.update(device);
        }
    }

    /**
     * Get the status of all devices in the system.
     * @return A string representation of all device statuses.
     */
    public String getStatus() {
        StringBuilder status = new StringBuilder();
        for (DeviceProxy proxy : devices.values()) {
            status.append(proxy.getType()).append(" ").append(proxy.getId())
                  .append(" is ").append(proxy.getStatus()).append("\n");
        }
        return status.toString();
    }

    /**
     * Add a trigger to the system.
     * @param condition The condition for the trigger.
     * @param action The action to perform when the condition is met.
     */
    public void addTrigger(String condition, String action) {
        if (!isValidCondition(condition)) {
            throw new IllegalArgumentException("Invalid condition format: " + condition);
        }
        if (!isValidAction(action)) {
            throw new IllegalArgumentException("Invalid action format: " + action);
        }
        triggers.add(new Trigger(condition, action));
        logger.info("Added trigger: " + condition + " -> " + action);
    }

    /**
     * Remove a trigger from the system.
     * @param condition The condition of the trigger to remove.
     * @param action The action of the trigger to remove.
     */
    public void removeTrigger(String condition, String action) {
        Trigger triggerToRemove = null;
        for (Trigger trigger : triggers) {
            if (trigger.getCondition().equals(condition) && trigger.getAction().equals(action)) {
                triggerToRemove = trigger;
                break;
            }
        }
        if (triggerToRemove != null) {
            triggers.remove(triggerToRemove);
            logger.info("Removed trigger: " + condition + " -> " + action);
        } else {
            throw new IllegalArgumentException("Trigger not found: " + condition + " -> " + action);
        }
    }

    /**
     * List all triggers in the system.
     * @return A string representation of all triggers.
     */
    public String listTriggers() {
        StringBuilder sb = new StringBuilder();
        for (Trigger trigger : triggers) {
            sb.append(trigger.getCondition()).append(" -> ").append(trigger.getAction()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Validate the condition format for a trigger.
     * @param condition The condition to validate.
     * @return True if the condition is valid, false otherwise.
     */
    private boolean isValidCondition(String condition) {
        String[] parts = condition.split(" ");
        if (parts.length != 3) {
            return false;
        }
        String deviceId = parts[0];
        String operator = parts[1];
        String value = parts[2];
    
        return devices.containsKey(deviceId) &&
               (operator.equals(">") || operator.equals("<") || operator.equals("==")) &&
               value.matches("-?\\d+");
    }

    /**
     * Validate the action format for a trigger.
     * @param action The action to validate.
     * @return True if the action is valid, false otherwise.
     */
    private boolean isValidAction(String action) {
        String[] parts = action.split("\\(");
        if (parts.length != 2 || !parts[1].endsWith(")")) {
            return false;
        }
        String command = parts[0];
        String deviceId = parts[1].substring(0, parts[1].length() - 1);
    
        return (command.equals("turnOn") || command.equals("turnOff") || command.equals("setTemperature")) &&
               devices.containsKey(deviceId);
    }

     /**
     * Check and execute triggers based on current device states.
     */
    public void checkTriggers() {
        Iterator<Trigger> iterator = triggers.iterator();
        while (iterator.hasNext()) {
            Trigger trigger = iterator.next();
            try {
                if (!trigger.hasFired() && evaluateCondition(trigger.getCondition())) {
                    executeAction(trigger.getAction());
                    trigger.setFired(true);
                    logger.info("Trigger fired: " + trigger.getCondition() + " -> " + trigger.getAction());
                }
            } catch (IllegalArgumentException e) {
                logger.warn("Removing invalid trigger: " + trigger.getCondition() + " -> " + trigger.getAction());
                iterator.remove();
            }
        }
    }

    /**
     * Reset triggers that have fired and whose conditions are no longer met.
     */
    public void resetTriggers() {
        for (Trigger trigger : triggers) {
            if (trigger.hasFired() && !evaluateCondition(trigger.getCondition())) {
                trigger.setFired(false);
                logger.info("Trigger reset: " + trigger.getCondition());
            }
        }
    }

    /**
     * Evaluate a trigger condition.
     * @param condition The condition to evaluate.
     * @return True if the condition is met, false otherwise.
     */
    private boolean evaluateCondition(String condition) {
        String[] parts = condition.split(" ");
        String deviceId = parts[0];
        String operator = parts[1];
        String value = parts[2];

        DeviceProxy proxy = getDeviceProxyById(deviceId);

        if (proxy == null) {
            throw new IllegalArgumentException("Device does not exist: " + deviceId);
        }

        if (proxy.getDevice() instanceof Thermostat) {
            int temp = ((Thermostat) proxy.getDevice()).getTemperature();
            int threshold = Integer.parseInt(value);
            switch (operator) {
                case ">": return temp > threshold;
                case "<": return temp < threshold;
                case "==": return temp == threshold;
                default:
                    throw new IllegalArgumentException("Invalid operator in condition: " + operator);
            }
        }
        throw new IllegalArgumentException("Unsupported device type for condition: " + proxy.getType());
    }

    /**
     * Execute a trigger action.
     * @param action The action to execute.
     */
    private void executeAction(String action) {
        String[] parts = action.split("\\(");
        String command = parts[0];
        String deviceId = parts[1].replace(")", "");

        switch (command) {
            case "turnOn": turnOn(deviceId); break;
            case "turnOff": turnOff(deviceId); break;
            default:
                throw new IllegalArgumentException("Unknown command in action: " + command);
        }
    }

    /**
     * Get a device proxy by its ID.
     * @param id The ID of the device.
     * @return The DeviceProxy for the specified ID.
     * @throws IllegalArgumentException if the device does not exist.
     */
    private DeviceProxy getDeviceProxyById(String id) {
        DeviceProxy proxy = devices.get(id);
        if (proxy == null) {
            throw new IllegalArgumentException("Device with ID " + id + " does not exist");
        }
        return proxy;
    }

    /**
     * Start the interactive command-line interface for the smart home system.
     */
    public void start() {
        boolean running = true;
        System.out.print("\n.........................................................................");
        System.out.print("\n.................Welcome To Smart_Home_System Control Hub................");
        System.out.print("\n.........................................................................\n");
        try (Scanner scanner = new Scanner(System.in)) {
            while (running) {
                System.out.print("\n.........................................................................\n");
                System.out.print("Enter command: ");
                String input = scanner.nextLine();
                System.out.print(".........................................................................\n");
                running = processCommand(input);
                System.out.print(".........................................................................\n");
            }
        }
        System.out.print("\n.........................................................................");
        System.out.print("\n.................Thank You! Exiting......................................");
        System.out.print("\n.........................................................................\n");
    }

    private boolean processCommand(String input) {
        String[] parts = input.split(" ");    
        try {
            switch (parts[0]) {
                case "add":
                    if (parts.length != 3) throw new IllegalArgumentException("Usage: add <type> <id>");
                    addDevice(parts[1], parts[2]);
                    break;
                case "remove":
                    if (parts.length != 2) throw new IllegalArgumentException("Usage: remove <id>");
                    removeDevice(parts[1]);
                    break;
                case "turnOn":
                    if (parts.length != 2) throw new IllegalArgumentException("Usage: turnOn <id>");
                    turnOn(parts[1]);
                    break;
                case "turnOff":
                    if (parts.length != 2) throw new IllegalArgumentException("Usage: turnOff <id>");
                    turnOff(parts[1]);
                    break;
                case "schedule":
                    if (parts.length != 4) throw new IllegalArgumentException("Usage: schedule <id> <time> <true/false>");
                    setSchedule(parts[1], LocalTime.parse(parts[2]), Boolean.parseBoolean(parts[3]));
                    break;
                case "setTemperature":
                    if (parts.length != 3) throw new IllegalArgumentException("Usage: setTemperature <id> <temperature>");
                    setTemperature(parts[1], Integer.parseInt(parts[2]));
                    break;
                case "status":
                    System.out.println(getStatus());
                    break;
                case "listTriggers":
                    System.out.println("Current triggers:");
                    System.out.println(listTriggers());
                    break;
                case "exit":
                    stop();
                    return false;
                default:
                    System.out.println("Unknown command. Available commands: add, remove, turnOn, turnOff, setTemperature, schedule, status, listTriggers, exit");
            }
            Thread.sleep(100);
        } catch (Exception e) {
            logger.warn("Error processing command", e);
            System.out.println("Error: " + e.getMessage());
        }
        return true;
    }
}
