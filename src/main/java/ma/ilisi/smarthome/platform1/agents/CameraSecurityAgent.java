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
import ma.ilisi.smarthome.models.AirQualityPredictionModel;
import ma.ilisi.smarthome.models.ShutterPredictionModel;
import ma.ilisi.smarthome.platform1.containers.CameraSecurityContainer;

import java.util.Date;

public class CameraSecurityAgent extends GuiAgent {
    private ShutterPredictionModel shutterPredictionModel;
    private AirQualityPredictionModel airQualityPredictionModel;
    private CameraSecurityContainer cameraSecurityContainer;

    protected void setup() {
        cameraSecurityContainer = (CameraSecurityContainer) getArguments()[0];
        cameraSecurityContainer.cameraSecurityAgent = this;

        System.out.println("CameraAgent " + getAID().getLocalName() + " is ready.");


        addBehaviour(new CameraSecurityAgent.PublishServiceBehaviour(this, 1000));
        addBehaviour(new CameraSecurityAgent.DecisionBehaviour());
    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {
        // Handle GUI events if needed
    }

    private class DecisionBehaviour extends CyclicBehaviour {
        public void action() {
           System.out.println("im heeeere");
        }
    }
    public void detectImage(String imagePath) {
        System.out.println("dd"+imagePath);
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
