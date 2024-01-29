package ma.ilisi.smarthome.platform1.agents;

import jade.core.AID;
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
import ma.ilisi.smarthome.platform1.containers.AirQualityContainer;

import java.util.Date;

public class AirQualitySensorAgent extends GuiAgent {
    private AirQualityContainer airQualityContainer;
    private AID smartHomeAgentAID;
    protected void setup() {
        airQualityContainer=(AirQualityContainer) getArguments()[0];
        airQualityContainer.airQualitySensorAgent = this;
        System.out.println("airQualityContainer " + getAID().getLocalName() + " is ready.");
        // Add behaviors
        // Register the shutter service in the yellowpages
        addBehaviour(new PublishServiceBehaviour(this, 1000));
        // Generate sensor data
        addBehaviour(new SensorDataGenerationBehavior());
    }
    @Override
    public void onGuiEvent(GuiEvent guiEvent) {
        // Implement behavior logic here
        if (guiEvent.getType() == 1) {
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            if (smartHomeAgentAID == null) {
                smartHomeAgentAID = getSmartHomeAgentAID();
            }
            msg.addReceiver(smartHomeAgentAID);

            // extract parameters from the event
            double PM25 = (double) guiEvent.getParameter(0);
            double PM10 = (double) guiEvent.getParameter(1);
            double NO = (double) guiEvent.getParameter(2);
            double NO2 = (double) guiEvent.getParameter(3);
            double NOx = (double) guiEvent.getParameter(4);
            double NH3 = (double) guiEvent.getParameter(5);
            double CO = (double) guiEvent.getParameter(6);
            double SO2 = (double) guiEvent.getParameter(7);
            double O3 = (double) guiEvent.getParameter(8);
            double Benzene = (double) guiEvent.getParameter(9);
            double Toluene = (double) guiEvent.getParameter(10);
            double Xylene = (double) guiEvent.getParameter(11);

            // convert double values to string
            String PM25Str = String.valueOf(PM25);
            String PM10Str = String.valueOf(PM10);
            String NOStr = String.valueOf(NO);
            String NO2Str = String.valueOf(NO2);
            String NOxStr = String.valueOf(NOx);
            String NH3Str = String.valueOf(NH3);
            String COStr = String.valueOf(CO);
            String SO2Str = String.valueOf(SO2);
            String O3Str = String.valueOf(O3);
            String BenzeneStr = String.valueOf(Benzene);
            String TolueneStr = String.valueOf(Toluene);
            String XyleneStr = String.valueOf(Xylene);

            // send the message to the smart home agent
            msg.setContent(PM25Str + "," + PM10Str + "," + NOStr + "," + NO2Str + "," + NOxStr + ","
                    + NH3Str + "," + COStr + "," + SO2Str + "," + O3Str + "," + BenzeneStr + ","
                    + TolueneStr + "," + XyleneStr);
            send(msg);
        }
    }

    private class SensorDataGenerationBehavior extends CyclicBehaviour {
        public void action() {
            // wait for reply from the smart home agent
            ACLMessage msg = receive();
            if (msg != null) {
                // Process received messages
                System.out.println("AirQualityControlAgent received message: " + msg.getContent() + " from " + msg.getSender().getLocalName());
                // receive predicted position from the smart home agent and show it in the GUI
                airQualityContainer.showMessage("Predicted position: " + msg.getContent());

            } else {
                block();
            }
        }

        private double simulateSensorReading() {
            // Simulate sensor reading logic (replace with actual sensor logic)
            return Math.random() * 100;
        }
    }

    private AID getSmartHomeAgentAID() {
        // Retry a few times with a delay
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("smart_home");
        template.addServices(sd);
        for (int i = 0; i < 3; i++) {
            try {
                DFAgentDescription[] result = DFService.search(this, template);
                if (result.length > 0) {
                    return result[0].getName();
                }
            } catch (FIPAException e) {
                e.printStackTrace();
            }

            // Delay before retry
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        return null;
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
            DFAgentDescription dfd=new DFAgentDescription();
            dfd.setName(getAID());

            ServiceDescription sd=new ServiceDescription();
            sd.setType("air_control");
            sd.setName("air_control");
            dfd.addServices(sd);

            try {
                DFService.register(myAgent,dfd);
            } catch (FIPAException e) {
                e.printStackTrace();
            }
        }
    }
}
