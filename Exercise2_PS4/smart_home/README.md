# Smart Home System

## Overview

The Smart Home System is a Java-based application that simulates the control and management of various smart home devices. It provides a framework for adding, removing, and controlling devices such as lights, thermostats, and door locks. The system also supports scheduling and trigger-based automation.

## Features

- Add and remove smart home devices
- Control devices (turn on/off)
- Schedule device actions
- Set up triggers for automated control
- Extensible design for adding new device types
- Logging of system actions
- Unit tests for core functionality

## Prerequisites

- Java JDK 18 or higher
- Maven 3.6 or higher

## Setup

1. Clone the repository:
```
git clone https://github.com/kathirvelmtce/EI-2024-25_Coding-exercises.git
```

2. Navigate to the project directory:
```
cd smart-home-system
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
## Contributing
Contributions are welcome! Please feel free to submit a Pull Request.

## License
This project is licensed under the MIT License - see the LICENSE.md file for details.

## Acknowledgments
Inspired by various smart home technologies and IoT concepts.
