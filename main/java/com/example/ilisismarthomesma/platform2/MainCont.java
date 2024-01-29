package com.example.ilisismarthomesma.platform2;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;
public class MainCont {
    public static void main(String[] args) throws ControllerException  {

    	        
             Runtime runtime = Runtime.instance();
             System.out.println("Container started");
             ProfileImpl profileImpl = new ProfileImpl(false);        
             profileImpl.setParameter(ProfileImpl.MAIN_PORT,"1099"); 
             profileImpl.setParameter(ProfileImpl.GUI, "true");
             profileImpl.setParameter(ProfileImpl.CONTAINER_NAME, "Main");        
             runtime.createMainContainer(profileImpl).start();
     }
}
