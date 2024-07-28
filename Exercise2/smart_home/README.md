# Smart Home System

## Overview

The Smart Home System is a Java-based application that simulates the control and management of various smart home devices. It provides a framework for adding, removing, and controlling devices such as lights, thermostats, and door locks. The system also supports scheduling and trigger-based automation.

## Features

- Device Management: Add, remove, and control various smart devices
- Scheduling: Set schedules for device operations
- Trigger System: Create and manage triggers based on device states
- Command-line Interface: Interact with the system through a simple CLI
- Extensible design for adding new device types
- Logging of system actions
- Unit tests for core functionality

## Project Structure

```
smart-home/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── ex2/
│   │   │           ├── core/
│   │   │           │   ├── Device.java
│   │   │           │   ├── DeviceObserver.java
│   │   │           │   ├── DeviceProxy.java
│   │   │           │   ├── SmartHomeSystem.java
│   │   │           │   └── Trigger.java
│   │   │           ├── devices/
│   │   │           │   ├── DoorLock.java
│   │   │           │   ├── Light.java
│   │   │           │   └── Thermostat.java
│   │   │           ├── factory/
│   │   │           │   └── DeviceFactory.java
│   │   │           └── demo/
│   │   │               └── SmartHomeDemo.java
│   │   └── resources/
│   │       └── log4j2.properties
│   │
│   └── test/
│       └── java/
│           └── com/
│               └── ex2/
│                   ├── DeviceFactoryTest.java
│                   ├── DeviceTest.java
│                   └── SmartHomeSystemTest.java
│
├── pom.xml
│
├── logs/
│   └── smart_home.log
│
├── target/
│
└── README.md
```

## Prerequisites

- Java 8+
- Maven 3.6+

## Setup

1. Clone the repository:
```
git clone https://github.com/kathirvelmtce/EI-2024-25_Coding-exercises.git
```

2. Navigate to the project directory:
```
cd smart_home
```

3. Build the project:
```
mvn clean install
```

## Running the Application

To run the demo application:
```
mvn exec:java -Dexec.mainClass="com.ex2.demo.SmartHomeDemo"
```

## Running Tests

To run the unit tests:
```
mvn test
```

## Usage

The `SmartHomeDemo` class provides an example of how to use the Smart Home System. Here's a basic usage:

```java
SmartHomeSystem system = SmartHomeSystem.getInstance();

// Add devices
system.addDevice("light", "living_room_light");
system.addDevice("thermostat", "main_thermostat");

// Control devices
system.turnOn("living_room_light");

// Set a schedule
system.setSchedule("front_door", LocalTime.of(22, 0), true);

// Add a trigger
system.addTrigger("main_thermostat > 75", "turnOn(living_room_light)");

// Start the system
system.start();


```

The system provides a command-line interface with the following commands:
- `add <type> <id>`: Add a new device
- `remove <id>`: Remove a device
- `turnOn <id>`: Turn on a device
- `turnOff <id>`: Turn off a device
- `setTemperature <id> <temperature>`: Set thermostat temperature
- `schedule <id> <time> <true/false>`: Schedule a device operation
- `status`: Display status of all devices
- `listTriggers`: List all triggers
- `exit`: Exit the application


## Contributing
Contributions are welcome! Please feel free to submit a Pull Request.

## License
This project is licensed under the MIT License - see the LICENSE.md file for details.

## Acknowledgments
Inspired by various smart home technologies and IoT concepts.
