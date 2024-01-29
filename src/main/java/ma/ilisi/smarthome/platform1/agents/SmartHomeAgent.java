package ma.ilisi.smarthome.platform1.agents;

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
import ma.ilisi.smarthome.models.AirQualityPredictionModel;
import ma.ilisi.smarthome.models.ShutterPredictionModel;
import ma.ilisi.smarthome.platform1.containers.SmartHomeContainer;

import java.util.Date;

public class SmartHomeAgent extends GuiAgent {
    private ShutterPredictionModel shutterPredictionModel;
    private AirQualityPredictionModel airQualityPredictionModel;
    private SmartHomeContainer smartHomeContainer;

    protected void setup() {
        smartHomeContainer = (SmartHomeContainer) getArguments()[0];
        smartHomeContainer.smartHomeAgent = this;

        System.out.println("SmartHomeAgent " + getAID().getLocalName() + " is ready.");

        try {
            shutterPredictionModel = new ShutterPredictionModel();
            airQualityPredictionModel = new AirQualityPredictionModel();
        } catch (Exception e) {
            e.printStackTrace();
            doDelete();
        }

        addBehaviour(new PublishServiceBehaviour(this, 1000));
        addBehaviour(new DecisionBehaviour());
    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {
        // Handle GUI events if needed
    }

    private class DecisionBehaviour extends CyclicBehaviour {
        public void action() {
            ACLMessage msg = receive();
            if (msg != null) {
                System.out.println("Smart home received message: " + msg.getContent() + " from " + msg.getSender().getLocalName());

                String content = msg.getContent();
                String[] splitContent = content.split(",");
             //   double lightIntensity = Double.parseDouble(splitContent[0]);
               // double temperature = Double.parseDouble(splitContent[1]);
                double predictedPosition = 0;
                String decision = "";

                switch (msg.getSender().getLocalName()) {
                    case "shutter_control":
                        try {
                            predictedPosition = shutterPredictionModel.predictShutterPosition( Double.parseDouble(splitContent[0]),
                                    Double.parseDouble(splitContent[1]));
                            smartHomeContainer.showMessage("Predicted position: " + predictedPosition);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case "air_quality_sensor":
                        System.out.println("im heeere pleaaze");
                        try {
                            decision = airQualityPredictionModel.predict(
                                    Double.parseDouble(splitContent[0]),
                                    Double.parseDouble(splitContent[1]),
                                    Double.parseDouble(splitContent[2]),
                                    Double.parseDouble(splitContent[3]),
                                    Double.parseDouble(splitContent[4]),
                                    Double.parseDouble(splitContent[5]),
                                    Double.parseDouble(splitContent[6]),
                                    Double.parseDouble(splitContent[7]),
                                    Double.parseDouble(splitContent[8]),
                                    Double.parseDouble(splitContent[9]),
                                    Double.parseDouble(splitContent[10]),
                                    Double.parseDouble(splitContent[11])
                            );
                            System.out.println(decision);
                            smartHomeContainer.showMessage("The response is " + decision);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        System.out.println("Unknown sender: " + msg.getSender().getLocalName());
                }

                ACLMessage reply = msg.createReply();
                reply.setContent(String.valueOf(msg.getSender().getLocalName().equals("shutter_control") ? predictedPosition : decision));
                send(reply);
            } else {
                block();
            }
        }
    }

    private class PublishServiceBehaviour extends WakerBehaviour {
        public PublishServiceBehaviour(Agent a, Date wakeupDate) {
            super(a, wakeupDate);
        }

        public PublishServiceBehaviour(Agent a, long timeout) {
            super(a, timeout);
        }

        @Override
        public void onWake() {
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName(getAID());

            ServiceDescription sd = new ServiceDescription();
            sd.setType("smart_home");
            sd.setName("smart_home");
            dfd.addServices(sd);

            try {
                DFService.register(myAgent, dfd);
            } catch (FIPAException e) {
                e.printStackTrace();
            }
        }
    }
}
