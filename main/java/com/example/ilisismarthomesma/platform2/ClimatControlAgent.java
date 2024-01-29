package com.example.ilisismarthomesma.platform2;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.core.AID;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.IOException;

public class ClimatControlAgent extends Agent {

    private Instances dataset;

    protected void setup() {
        System.out.println("ClimatControlAgent " + getAID().getName() + " is ready.");

        // Load dataset
        loadDataset();

        // Add behavior to send climate data to HomeAgent
        addBehaviour(new SendClimateDataBehavior());
    }

    private void loadDataset() {
        try {
            // Load dataset from ARFF file
            String datasetPath = "src/main/resources/datasets/climat_dataset.arff";
            DataSource source = new DataSource(datasetPath);
            dataset = source.getDataSet();
            dataset.setClassIndex(dataset.numAttributes() - 1); // Assuming the last attribute is the class
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class SendClimateDataBehavior extends CyclicBehaviour {
        private int currentIndex = 0;

        public void action() {
            if (currentIndex < dataset.numInstances()) {
                // Send climate data to HomeAgent
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.addReceiver(getAID("HomeAgent")); // Change "HomeAgent" to the correct name of your HomeAgent
                
                // Assuming climate data format: "temperature,humidity"
                Instance instance = dataset.instance(currentIndex);
                String climateData = instance.toString(); // Get the current instance from the dataset
                System.out.println("les données: " + climateData);
                msg.setContent(climateData);
                send(msg);

                currentIndex++;
            } else {
                // No more instances to send, terminate the behavior
                myAgent.removeBehaviour(this);
            }
        }
    }
}
