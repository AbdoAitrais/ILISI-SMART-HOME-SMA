module ma.ilisi.smarthome {
    requires javafx.controls;
    requires javafx.fxml;
    requires weka.stable;
    requires jade;
    requires opencv;
    requires spring.web;
    requires commons.fileupload;
    requires commons.io;
    requires spring.test;


    opens ma.ilisi.smarthome to javafx.fxml;
    exports ma.ilisi.smarthome;
    exports ma.ilisi.smarthome.platform3.containers;
    exports ma.ilisi.smarthome.platform3.agents;
    exports ma.ilisi.smarthome.platform2.containers;
    exports ma.ilisi.smarthome.platform2.agents;
    exports ma.ilisi.smarthome.platform1.agents;
    exports ma.ilisi.smarthome.platform1.containers;
}