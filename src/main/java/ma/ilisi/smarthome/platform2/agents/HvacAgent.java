package ma.ilisi.smarthome.platform2.agents;

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
import ma.ilisi.smarthome.platform2.containers.HvacContainer;

import java.util.Date;

public class HvacAgent extends GuiAgent {
    private HvacContainer hvacContainer;
    private AID smartHomeAgentAID;

    protected void setup() {
        hvacContainer = (HvacContainer) getArguments()[0];
        hvacContainer.hvacAgent = this;
        System.out.println("HvacAgent " + getAID().getLocalName() + " is ready.");


        // Add behaviors
        addBehaviour(new SensorDataGenerationBehavior());
        addBehaviour(new PublishServiceBehaviour(this, 1000));
    }

    @Override
    public void onGuiEvent(GuiEvent guiEvent) {
        if (guiEvent.getType() == 1) {
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            if (smartHomeAgentAID == null) {
                smartHomeAgentAID = getSmartHomeAgentAID();
            }
            msg.addReceiver(smartHomeAgentAID);

            // extract temperature , humidity , wind , outlook from the event
            String temperature = (String) guiEvent.getParameter(0);
            String humidity = (String) guiEvent.getParameter(1);
            String wind = (String) guiEvent.getParameter(2);
            String outlook = (String) guiEvent.getParameter(3);

            // send the message to the smart home agent
            msg.setContent(temperature + "," + humidity + "," + wind + "," + outlook);
            send(msg);
        }
    }

    private class SensorDataGenerationBehavior extends CyclicBehaviour {
        public void action() {
            // wait for reply from the smart home agent
            ACLMessage msg = receive();
            if (msg != null) {
                // Process received messages
                System.out.println("HvacAgent received message: " + msg.getContent() +
                        " from " + msg.getSender().getLocalName());
                // receive predicted position from the smart home agent and show it in the GUI
                hvacContainer.showMessage("Turn on heater prediction: " + msg.getContent());
            } else {
                block();
            }
        }
    }

    private AID getSmartHomeAgentAID() {
        // get smart home agent AID from nickname smart_home
        return new AID("smart_home", AID.ISLOCALNAME);
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
            sd.setType("hvac");
            sd.setName("hvac");
            dfd.addServices(sd);

            try {
                DFService.register(myAgent, dfd);
            } catch (FIPAException e) {
                e.printStackTrace();
            }
        }
    }
}
