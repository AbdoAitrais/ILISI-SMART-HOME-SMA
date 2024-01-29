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
import ma.ilisi.smarthome.platform1.containers.CameraSecurityContainer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CameraSecurityAgent extends GuiAgent {
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
            ACLMessage msg = receive();
            if (msg != null) {
                // Process received messages
                System.out.println("AirQualityControlAgent received message: " + msg.getContent() + " from " + msg.getSender().getLocalName());
                // receive predicted position from the smart home agent and show it in the GUI
                System.out.println("im heeeere");

            } else {
                block();
            }

        }
    }
    public void detectImage(String imagePath) {
        System.out.println("dd"+imagePath);
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        AID senderAID = msg.getSender();
        msg.addReceiver(senderAID);
        // image path
        // C:\Users\ElMehdi\Documents\smaProject\face-recognition-java\core\src\test\resources\images\train\madonna\httpiamediaimdbcomimagesMMVBMTANDQNTAxNDVeQTJeQWpwZBbWUMDIMjQOTYVUXCRALjpg.jpg
        // C:\Users\ElMehdi\Documents\smaProject\face-recognition-java\core\src\test\resources\images\train\madonna\httpiamediaimdbcomimagesMMVBMTANDQNTAxNDVeQTJeQWpwZBbWUMDIMjQOTYVUXCRALjpg_result.jpg
        // imagePath+_result+extension

        //java -jar cli/target/face-recognition-java-${VERSION}.jar predict -e embeddings.dat -p /path/to/image

        List<String> commands = List.of("java","-jar","C:\\Users\\ElMehdi\\Documents\\smaProject\\face-recognition-java\\cli\\target\\face-recognition-cli-0.3.1.jar","predict","-e",
                "C:\\Users\\ElMehdi\\Documents\\smaProject\\face-recognition-java\\embeddings.dat" , "-p",imagePath);
        ProcessBuilder pb = new ProcessBuilder(commands);



        try {
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            int exitCode = process.waitFor();
            System.out.println("Exited with error code: " + exitCode);
        }catch (Exception e){
            System.out.println("Error: "  + e.getMessage());
        }
        send(msg);
        String[] paths = imagePath.split("\\.");
        System.out.println("Array: " + Arrays.toString(paths));
        String mot = paths[0] + "_result." + paths[1];
        System.out.println("mott"+mot);
        cameraSecurityContainer.processMessage(mot);



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
