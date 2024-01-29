package ma.ilisi.smarthome.platform3.agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import ma.ilisi.smarthome.models.ShutterPredictionModel;
import ma.ilisi.smarthome.platform3.containers.SmartHomeContainer;

import java.util.Date;

public class SmartHomeAgent extends GuiAgent {
    private ShutterPredictionModel shutterPredictionModel; // Assuming you have this class
    private SmartHomeContainer smartHomeContainer;
    protected void setup() {
        smartHomeContainer = (SmartHomeContainer) getArguments()[0];
        smartHomeContainer.smartHomeAgent = this;

        System.out.println("SmartHomeAgent " + getAID().getLocalName() + " is ready.");
        // Initialize the Shutter Prediction Model
        try {
            shutterPredictionModel = new ShutterPredictionModel();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Add behaviors
        // Register the shutter service in the yellowpages
//        addBehaviour(new PublishServiceBehaviour(this, 10));
        // Receive sensor data and send it to the Decision Agent
        addBehaviour(new DecisionBehaviour());

    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {

    }

    private class DecisionBehaviour extends CyclicBehaviour {
        public void action() {
            // Implement behavior logic here
            ACLMessage msg = receive();
            if (msg != null) {
                // Process received messages
                System.out.println("Smart home received message: " + msg.getContent() + " from " + msg.getSender().getLocalName());

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

                // show the predicted position in the GUI
                smartHomeContainer.showMessage("Predicted position: " + predictedPosition);

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

    // register service
    private class PublishServiceBehaviour extends WakerBehaviour {
        public PublishServiceBehaviour(Agent a, Date wakeupDate) {
            super(a, wakeupDate);
        }

        public PublishServiceBehaviour(Agent a, long timeout) {
            super(a, timeout);
        }

        @Override
        public void onWake() {
            DFAgentDescription dfd=new DFAgentDescription();
            dfd.setName(getAID());

            ServiceDescription sd=new ServiceDescription();
            sd.setType("smart_home");
            sd.setName("smart_home");
            dfd.addServices(sd);

            try {
                DFService.register(myAgent,dfd);
            } catch (FIPAException e) {
                e.printStackTrace();
            }
        }
    }

}
