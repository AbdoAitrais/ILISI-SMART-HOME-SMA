package com.example.ilisismarthomesma.platform3;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class SmartHomeAgent extends Agent {

    protected void setup() {
        System.out.println("SmartHomeAgent " + getAID().getName() + " is ready.");

        // Add behaviors if needed
        addBehaviour(new DecisionBehaviour());
    }

    private class DecisionBehaviour extends CyclicBehaviour {
        public void action() {
            // Implement decision-making logic here
            ACLMessage msg = receive();
            if (msg != null) {
                // Process received messages and make decisions
                System.out.println("SmartHomeAgent received message: " + msg.getContent());
            } else {
                // Block until a message is received
                block();
            }
        }
    }
}
