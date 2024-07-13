Smart Home Multi-Agent System

This repository contains the code for a Smart Multi-Agent System designed to manage a smart home environment using the JADE (Java Agent DEvelopment Framework) and various machine learning models. The system is distributed across three platforms and includes multiple agents representing different IoT devices.
Project Description

The goal of this project is to create a smart home system that leverages multi-agent systems and machine learning to optimize the coordination of services, improve service quality, optimize resource usage, and minimize energy costs.
Agents and Their Roles

    SmartHomeAgent
        Responsible for decision-making based on data from other IoT agents.
        Utilizes machine learning models to make predictions and decisions.

    AirQualitySensorAgent
        Represents an air quality sensor.
        Interacts with SmartHomeAgent.

    SecurityCameraAgent
        Represents a security camera.
        Interacts with SmartHomeAgent and uses a pre-trained convolutional neural network model for facial recognition.

    HVACAgent
        Represents a heating/ventilation/air conditioning actuator.
        Interacts with SmartHomeAgent.

    LightControlAgent
        Represents a light control actuator.
        Interacts with SmartHomeAgent.

    WateringControlAgent
        Represents a watering control device.
        Interacts with SmartHomeAgent.

    SmartLockAgent
        Represents a smart lock device.
        Uses the BARD (Behavior-Aided Reasoning for Chatbots) library for enhanced decision-making.

    ShutterControlAgent
        Represents a shutter control device.
        Interacts with SmartHomeAgent.

    SmokeSensorAgent
        Represents a smoke sensor.
        Interacts with SmartHomeAgent.

Platforms Distribution

    Platform1: SmartHomeAgent, AirQualitySensorAgent, SecurityCameraAgent
    Platform2: HVACAgent, LightControlAgent, WateringControlAgent
    Platform3: SmartLockAgent, ShutterControlAgent, SmokeSensorAgent

Objectives

    Modeling Agents: Efficiently model agents to represent IoT devices and their capabilities.
    Inter-Agent Communication: Implement effective communication mechanisms for distributed coordination.
    Data Collection: Collect data from various sensors for decision-making.
    Adaptability and Continuous Learning: Enable agents to dynamically adapt to changes and continuously learn from the environment.
    Machine Learning Algorithms: Utilize suitable machine learning algorithms to predict device behavior and anticipate user needs.

Getting Started
Prerequisites

    Java Development Kit (JDK) 8 or higher
    JADE Framework
    Machine Learning models from Hugging Face
    Apache Maven

Installation

    Clone the repository:

    bash

git clone https://github.com/AbdoAitrais/ILISI-SMART-HOME-SMA.git

Navigate to the project directory:

bash

cd ILISI-SMART-HOME-SMA

Build the project using Maven:

bash

    mvn clean install

Running the Application

    Start the JADE runtime environment:

    bash

    java -cp target/your-jar-file.jar jade.Boot

    Deploy agents on the specified platforms by running the respective classes.

Usage

    A graphical user interface (GUI) acts as the dashboard for the application and is associated with the SmartHomeAgent.

Contributions

Contributions are welcome. Please fork the repository and create a pull request with your changes.
