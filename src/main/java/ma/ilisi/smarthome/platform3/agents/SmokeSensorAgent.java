package ma.ilisi.smarthome.platform3.agents;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import ma.ilisi.smarthome.platform3.containers.SmokeSensorContainer;

public class SmokeSensorAgent extends GuiAgent {
    private SmokeSensorContainer smokeSensorContainer;
    private AID smartHomeAgentAID;

    protected void setup() {
        smokeSensorContainer=(SmokeSensorContainer)getArguments()[0];
        smokeSensorContainer.smokeSensorAgent = this;
        System.out.println("SmokeSensorAgent " + getAID().getLocalName() + " is ready.");
        // Add behaviors
        // Register the smoke sensor service in the yellowpages
//        addBehaviour(new PublishServiceBehaviour(this, 10));
        // Generate sensor data
        addBehaviour(new SensorDataGenerationBehavior());
    }
    @Override
    public void onGuiEvent(GuiEvent guiEvent) {
        if (guiEvent.getType() == 1) {
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            if (smartHomeAgentAID == null) {
                smartHomeAgentAID = getSmartHomeAgentAID();
            }
            msg.addReceiver(smartHomeAgentAID);
            // extract parameters temperature , humidity , tvoc , eco2 , rawH2 , rawEthanol , pressure from the event
            String temperature = (String) guiEvent.getParameter(0);
            String humidity = (String) guiEvent.getParameter(1);
            String tvoc = (String) guiEvent.getParameter(2);
            String eco2 = (String) guiEvent.getParameter(3);
            String rawH2 = (String) guiEvent.getParameter(4);
            String rawEthanol = (String) guiEvent.getParameter(5);
            String pressure = (String) guiEvent.getParameter(6);
            // send the message to the smart home agent
            msg.setContent(temperature + "," + humidity + "," + tvoc + "," + eco2 + "," + rawH2 + "," + rawEthanol + "," + pressure);
            send(msg);
        }
    }

    private class SensorDataGenerationBehavior extends CyclicBehaviour {
        @Override
        public void action() {
            // wait for reply from the smart home agent
            ACLMessage msg = receive();
            if (msg != null) {
                // Process received messages
                System.out.println("SmokeSensorAgent received message: " + msg.getContent() + " from " + msg.getSender().getLocalName());
                // receive predicted position from the smart home agent and show it in the GUI
                smokeSensorContainer.showMessage("Predicted smoke existance: " + msg.getContent());

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
}
