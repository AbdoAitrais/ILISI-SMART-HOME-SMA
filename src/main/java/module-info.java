module ma.ilisi.smarthome {
    requires javafx.controls;
    requires javafx.fxml;
    requires weka.stable;
    requires jade;


    opens ma.ilisi.smarthome to javafx.fxml;
    exports ma.ilisi.smarthome;
    exports ma.ilisi.smarthome.platform3.containers;
    exports ma.ilisi.smarthome.platform3.agents;
    exports ma.ilisi.smarthome.platform1.containers;
    exports ma.ilisi.smarthome.platform1.agents;
}