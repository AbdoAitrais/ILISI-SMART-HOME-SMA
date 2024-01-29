package com.example.ilisismarthomesma.platform3;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class ShutterControlAgent extends Agent {

    private ShutterPredictionModel shutterPredictionModel; // Assuming you have this class

    protected void setup() {
        System.out.println("ShutterControlAgent " + getAID().getName() + " is ready.");

        // Initialize the Shutter Prediction Model
        try {
            shutterPredictionModel = new ShutterPredictionModel();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Add behaviors
        addBehaviour(new ShutterControlBehavior());
    }

    private class ShutterControlBehavior extends CyclicBehaviour {
        public void action() {
            // Implement behavior logic here
            ACLMessage msg = receive();
            if (msg != null) {
                // Process received messages
                System.out.println("ShutterControlAgent received message: " + msg.getContent());

                // Assuming the content of the message contains relevant data
                // For example, you might receive sensor data or a request for shutter control

                // Use the Shutter Prediction Model to predict the shutter position
                String content = msg.getContent();
                String[] splitContent = content.split(",");
                double lightIntensity = Double.parseDouble(splitContent[0]);
                double temperature = Double.parseDouble(splitContent[1]);
                double predictedPosition;
                try {
                    predictedPosition = shutterPredictionModel.predictShutterPosition(lightIntensity,temperature);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                // send the predicted position to the Smart Home Agent
                ACLMessage reply = msg.createReply();
                reply.setContent(String.valueOf(predictedPosition));
                send(reply);

            } else {
                // Block until a message is received
                block();
            }
        }
    }
}
