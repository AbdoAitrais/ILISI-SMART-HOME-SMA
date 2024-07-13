# Smart Home Multi-Agent System

This repository contains the code for a Smart Multi-Agent System designed to manage a smart home environment using the JADE (Java Agent DEvelopment Framework) and various machine learning models. The system is distributed across three platforms and includes multiple agents representing different IoT devices.

## Project Description

The goal of this project is to create a smart home system that leverages multi-agent systems and machine learning to optimize the coordination of services, improve service quality, optimize resource usage, and minimize energy costs.

### Agents and Their Roles

1. **SmartHomeAgent**
   - Responsible for decision-making based on data from other IoT agents.
   - Utilizes machine learning models to make predictions and decisions.

2. **AirQualitySensorAgent**
   - Represents an air quality sensor.
   - Interacts with SmartHomeAgent.

3. **SecurityCameraAgent**
   - Represents a security camera.
   - Interacts with SmartHomeAgent and uses a pre-trained convolutional neural network model for facial recognition.

4. **HVACAgent**
   - Represents a heating/ventilation/air conditioning actuator.
   - Interacts with SmartHomeAgent.

5. **LightControlAgent**
   - Represents a light control actuator.
   - Interacts with SmartHomeAgent.

6. **WateringControlAgent**
   - Represents a watering control device.
   - Interacts with SmartHomeAgent.

7. **SmartLockAgent**
   - Represents a smart lock device.
   - Uses the BARD (Behavior-Aided Reasoning for Chatbots) library for enhanced decision-making.

8. **ShutterControlAgent**
   - Represents a shutter control device.
   - Interacts with SmartHomeAgent.

9. **SmokeSensorAgent**
   - Represents a smoke sensor.
   - Interacts with SmartHomeAgent.

### Platforms Distribution

- **Platform1**: SmartHomeAgent, AirQualitySensorAgent, SecurityCameraAgent
- **Platform2**: HVACAgent, LightControlAgent, WateringControlAgent
- **Platform3**: SmartLockAgent, ShutterControlAgent, SmokeSensorAgent

## Objectives

1. **Modeling Agents**: Efficiently model agents to represent IoT devices and their capabilities.
2. **Inter-Agent Communication**: Implement effective communication mechanisms for distributed coordination.
3. **Data Collection**: Collect data from various sensors for decision-making.
4. **Adaptability and Continuous Learning**: Enable agents to dynamically adapt to changes and continuously learn from the environment.
5. **Machine Learning Algorithms**: Utilize suitable machine learning algorithms to predict device behavior and anticipate user needs.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- JADE Framework
- Machine Learning models from Hugging Face
- Apache Maven

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/AbdoAitrais/ILISI-SMART-HOME-SMA.git
   ```

2. Navigate to the project directory:
   ```bash
   cd ILISI-SMART-HOME-SMA
   ```

3. Build the project using Maven:
   ```bash
   mvn clean install
   ```

### Running the Application

1. Start the JADE runtime environment:
   ```bash
   java -cp target/your-jar-file.jar jade.Boot
   ```

2. Deploy agents on the specified platforms by running the respective classes.

### Usage

- A graphical user interface (GUI) acts as the dashboard for the application and is associated with the SmartHomeAgent.

### Contributions

Contributions are welcome. Please fork the repository and create a pull request with your changes.
