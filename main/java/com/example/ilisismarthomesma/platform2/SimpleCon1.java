package com.example.ilisismarthomesma.platform2;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class SimpleCon1 {
    public static void main(String[] args) throws StaleProxyException {
    	Runtime runtime = Runtime.instance();
        System.out.println("Container started");
        ProfileImpl profileImpl = new ProfileImpl(false);        
        profileImpl.setParameter(ProfileImpl.MAIN_PORT,"1099");
        profileImpl.setParameter(ProfileImpl.MAIN_HOST,"localhost"); 
        profileImpl.setParameter(ProfileImpl.GUI, "false");      
        profileImpl.setParameter(ProfileImpl.CONTAINER_NAME, "ContainerHome");  
        AgentContainer agentContainer = runtime.createAgentContainer(profileImpl);
        AgentController agentController = agentContainer.createNewAgent
                        ("HomeAgent", 
                        "com.example.ilisismarthomesma.platform2.HomeAgent",null);
        agentController.start();
}
}