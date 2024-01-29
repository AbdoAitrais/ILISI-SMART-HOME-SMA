package ma.ilisi.smarthome.platform3.agents;

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
import ma.ilisi.smarthome.platform3.containers.ShutterControlContainer;

import java.util.Date;

public class ShutterControlAgent extends GuiAgent {
    private ShutterControlContainer shutterControlContainer;
    private AID smartHomeAgentAID;
    protected void setup() {
        shutterControlContainer=(ShutterControlContainer)getArguments()[0];
        shutterControlContainer.shutterControlAgent = this;
        System.out.println("HvacAgent " + getAID().getLocalName() + " is ready.");
        // Add behaviors
        // Register the shutter service in the yellowpages
//        addBehaviour(new PublishServiceBehaviour(this, 10));
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
            // extract prameters lightIntensity and temperature from the event
            String lightIntensity = (String) guiEvent.getParameter(0);
            String temperature = (String) guiEvent.getParameter(1);
            // send the message to the smart home agent
            msg.setContent(lightIntensity + "," + temperature);
            send(msg);
        }
    }

    private class SensorDataGenerationBehavior extends CyclicBehaviour {
        public void action() {
            // wait for reply from the smart home agent
            ACLMessage msg = receive();
            if (msg != null) {
                // Process received messages
                System.out.println("HvacAgent received message: " + msg.getContent() + " from " + msg.getSender().getLocalName());
                // receive predicted position from the smart home agent and show it in the GUI
                shutterControlContainer.showMessage("Predicted position: " + msg.getContent());

            } else {
                block();
            }
        }

    }

//    private AID getSmartHomeAgentAID() {
//        // Retry a few times with a delay
//        DFAgentDescription template = new DFAgentDescription();
//        ServiceDescription sd = new ServiceDescription();
//        sd.setType("smart_home");
//        template.addServices(sd);
//        for (int i = 0; i < 3; i++) {
//            try {
//                DFAgentDescription[] result = DFService.search(this, template);
//                if (result.length > 0) {
//                    return result[0].getName();
//                }
//            } catch (FIPAException e) {
//                e.printStackTrace();
//            }
//
//            // Delay before retry
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException ex) {
//                ex.printStackTrace();
//            }
//        }
//        return null;
//    }

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
            DFAgentDescription dfd=new DFAgentDescription();
            dfd.setName(getAID());

            ServiceDescription sd=new ServiceDescription();
            sd.setType("shutter_control");
            sd.setName("shutter_control");
            dfd.addServices(sd);

            try {
                DFService.register(myAgent,dfd);
            } catch (FIPAException e) {
                e.printStackTrace();
            }
        }
    }
}
