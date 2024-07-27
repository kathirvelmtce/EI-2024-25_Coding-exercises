package com.ex2.core;

import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;

import com.ex2.factory.DeviceFactory;
import com.ex2.devices.Thermostat;

public class SmartHomeSystem {
    private static SmartHomeSystem instance;
    private final Map<String, DeviceProxy> devices = new ConcurrentHashMap<>();
    private final List<DeviceObserver> observers = new CopyOnWriteArrayList<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final List<Trigger> triggers = new CopyOnWriteArrayList<>();
    private static final Logger logger = Logger.getLogger(SmartHomeSystem.class.getName());

    private SmartHomeSystem() {
        logger.setLevel(Level.ALL);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        logger.addHandler(handler);
    }

    public static synchronized SmartHomeSystem getInstance() {
        if (instance == null) {
            instance = new SmartHomeSystem();
        }
        return instance;
    }

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

    public void removeDevice(String id) {
        DeviceProxy proxy = devices.remove(id);
        if (proxy != null) {
            notifyObservers(proxy);
            logger.info("Removed " + proxy.getType() + " with ID " + id);
        } else {
            throw new IllegalArgumentException("Device with ID " + id + " does not exist");
        }
    }

    public void turnOn(String id) {
        DeviceProxy proxy = getDeviceProxyById(id);
        proxy.turnOn();
        notifyObservers(proxy);
    }

    public void turnOff(String id) {
        DeviceProxy proxy = getDeviceProxyById(id);
        proxy.turnOff();
        notifyObservers(proxy);
    }

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

    public void addObserver(DeviceObserver observer) {
        observers.add(observer);
        logger.info("Added new observer");
    }

    private void notifyObservers(Device device) {
        for (DeviceObserver observer : observers) {
            observer.update(device);
        }
    }

    public String getStatus() {
        StringBuilder status = new StringBuilder();
        for (DeviceProxy proxy : devices.values()) {
            status.append(proxy.getType()).append(" ").append(proxy.getId())
                  .append(" is ").append(proxy.getStatus()).append("\n");
        }
        return status.toString();
    }

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

    private boolean isValidAction(String action) {
        String[] parts = action.split("\\(");
        if (parts.length != 2 || !parts[1].endsWith(")")) {
            return false;
        }
        String command = parts[0];
        String deviceId = parts[1].substring(0, parts[1].length() - 1);

        return (command.equals("turnOn") || command.equals("turnOff")) &&
               devices.containsKey(deviceId);
    }

    public void checkTriggers() {
        for (Trigger trigger : triggers) {
            try {
                if (evaluateCondition(trigger.getCondition())) {
                    executeAction(trigger.getAction());
                }
            } catch (IllegalArgumentException e) {
                logger.warning("Error processing trigger: " + e.getMessage());
                // Optionally, you might want to remove invalid triggers:
                // triggers.remove(trigger);
            }
        }
    }

    private boolean evaluateCondition(String condition) {
        String[] parts = condition.split(" ");
        String deviceId = parts[0];
        String operator = parts[1];
        String value = parts[2];

        DeviceProxy proxy = getDeviceProxyById(deviceId);

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

    private DeviceProxy getDeviceProxyById(String id) {
        DeviceProxy proxy = devices.get(id);
        if (proxy == null) {
            throw new IllegalArgumentException("Device with ID " + id + " does not exist");
        }
        return proxy;
    }

    public void start() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("Enter command: ");
                String input = scanner.nextLine();
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
                        case "status":
                            System.out.println(getStatus());
                            break;
                        case "exit":
                            System.out.println("Exiting...");
                            System.exit(0);
                        default:
                            System.out.println("Unknown command. Available commands: add, remove, turnOn, turnOff, schedule, status, exit");
                    }
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Error processing command", e);
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }
}