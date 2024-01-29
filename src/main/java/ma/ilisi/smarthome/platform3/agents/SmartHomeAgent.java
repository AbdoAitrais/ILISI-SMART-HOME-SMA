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
import ma.ilisi.smarthome.models.HeatingCoolingPredictionModel;
import ma.ilisi.smarthome.models.ShutterPredictionModel;
import ma.ilisi.smarthome.models.SmokeDetectionModel;
import ma.ilisi.smarthome.platform3.containers.SmartHomeContainer;

import java.util.Date;

public class SmartHomeAgent extends GuiAgent {
    private ShutterPredictionModel shutterPredictionModel;
    private HeatingCoolingPredictionModel heatingCoolingPredictionModel;
    private SmokeDetectionModel smokeDetectionModel;
    private SmartHomeContainer smartHomeContainer;
    protected void setup() {
        smartHomeContainer = (SmartHomeContainer) getArguments()[0];
        smartHomeContainer.smartHomeAgent = this;

        System.out.println("SmartHomeAgent " + getAID().getLocalName() + " is ready.");
        // Initialize the Shutter Prediction Model
        try {
            shutterPredictionModel = new ShutterPredictionModel();
            heatingCoolingPredictionModel = new HeatingCoolingPredictionModel();
            smokeDetectionModel = new SmokeDetectionModel();
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

                switch (msg.getSender().getLocalName()){
                    case "hvac":
                        // receive data from HvacAgent about weather
                        String content = msg.getContent();
                        String prediction = predictHeatingCooling(content);
                        smartHomeContainer.showMessage("Predicted Heating/Cooling Decision: " + prediction);

                        // send the prediction to the HvacAgent
                        ACLMessage reply = msg.createReply();
                        reply.setContent(prediction);
                        send(reply);
                        break;
                    case "shutter_control":
                        // receive data from ShutterControlAgent about weather
                        String shutterContent = msg.getContent();
                        double predictedPosition = predictShutterPosition(shutterContent);
                        smartHomeContainer.showMessage("Predicted position: " + predictedPosition);

                        // send the prediction to the ShutterControlAgent
                        ACLMessage shutterReply = msg.createReply();
                        shutterReply.setContent(String.valueOf(predictedPosition));
                        send(shutterReply);
                        break;
                        case "smoke_sensor":
                            // receive data from SmokeSensorAgent about weather
                            String smokeContent = msg.getContent();
                            boolean smokeExistence = predictSmokeExistance(smokeContent);
                            smartHomeContainer.showMessage("Predicted smoke existance: " + smokeExistence);

                            // send the prediction to the SmokeSensorAgent
                            ACLMessage smokeReply = msg.createReply();
                            smokeReply.setContent(String.valueOf(smokeExistence));
                            send(smokeReply);
                            break;
                    default:
                        break;
                }

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

    private double predictShutterPosition(String content) {
        String[] splitContent = content.split(",");
        double lightIntensity = Double.parseDouble(splitContent[0]);
        double temperature = Double.parseDouble(splitContent[1]);
        smartHomeContainer.showMessage("Light intensity: " + lightIntensity + ", Temperature: " + temperature);
        double predictedPosition;
        try {
            predictedPosition = shutterPredictionModel.predictShutterPosition(lightIntensity, temperature);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return predictedPosition;
    }
    private String predictHeatingCooling(String content) {
        String[] splitContent = content.split(",");
    /*
    @attribute temperature numeric
    @attribute humidity numeric
    @attribute wind {low, medium, high}
    @attribute outlook {sunny, overcast, rainy}
    */
        double temperature = Double.parseDouble(splitContent[0]);
        double humidity = Double.parseDouble(splitContent[1]);
        String wind = splitContent[2];
        String outlook = splitContent[3];
        smartHomeContainer.showMessage("Temperature: " + temperature + ", Humidity: " + humidity + ", Wind: " + wind + ", Outlook: " + outlook);

        // use HeatingCoolingPredictionModel to predict the heating/cooling
        try {
            return heatingCoolingPredictionModel.predictHeatingCoolingDecision(temperature, humidity, wind, outlook);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean predictSmokeExistance(String content) {
        String[] splitContent = content.split(",");
        double temperature = Double.parseDouble(splitContent[0]);
        double humidity = Double.parseDouble(splitContent[1]);
        double tvoc = Double.parseDouble(splitContent[2]);
        double eco2 = Double.parseDouble(splitContent[3]);
        double rawH2 = Double.parseDouble(splitContent[4]);
        double rawEthanol = Double.parseDouble(splitContent[5]);
        double pressure = Double.parseDouble(splitContent[6]);
        smartHomeContainer.showMessage("Temperature: " + temperature + ", Humidity: " + humidity + ", TVOC: " + tvoc + ", eCO2: " + eco2 + ", raw H2: " + rawH2 + ", raw Ethanol: " + rawEthanol + ", Pressure: " + pressure);

        // use HeatingCoolingPredictionModel to predict the heating/cooling
        try {
            return smokeDetectionModel.predictSmokeExistence(temperature, humidity, tvoc, eco2, rawH2, rawEthanol, pressure);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
