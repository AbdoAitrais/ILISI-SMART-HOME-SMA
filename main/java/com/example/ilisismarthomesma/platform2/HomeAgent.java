package com.example.ilisismarthomesma.platform2;


import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import weka.classifiers.functions.LinearRegression;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class HomeAgent extends Agent {

    private LinearRegression weatherPredictionModel;

    protected void setup() {
        // Initialize the weather prediction model
        weatherPredictionModel = new LinearRegression();

        // Add a behavior to handle incoming messages from ClimatAgent
        addBehaviour(new HandleWeatherDataBehavior());
    }

    private class HandleWeatherDataBehavior extends CyclicBehaviour {
        public void action() {
            ACLMessage msg = receive();
            if (msg != null) {
                // Receive weather data from ClimatAgent
                String weatherData = msg.getContent();
                System.out.println("Début de reception");

                System.out.println("Received weather data: " + weatherData);

                // Parse weather data and make prediction
                double prediction = predict(weatherData);

                // Send the prediction to appropriate agent or perform further actions
                System.out.println("Weather prediction: " + prediction);
            } else {
                block();
            }
        }
    }

    private double predict(String weatherData) {
        try {
            // Parse weather data and create an instance for prediction
            Instance newInstance = new DenseInstance(5);
            String[] data = weatherData.split(","); // Assuming data format: "temperature,humidity,wind,outlook,play"
            for (int i = 0; i < data.length; i++) {
                newInstance.setValue(i, Double.parseDouble(data[i]));
            }

            // Make prediction using the model
            return weatherPredictionModel.classifyInstance(newInstance);
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Handle the error appropriately
        }
    }
}
