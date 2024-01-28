module com.example.ilisismarthomesma {
    requires javafx.controls;
    requires javafx.fxml;
    requires commons.math3;
    requires weka.stable;
    requires jade;


    opens com.example.ilisismarthomesma to javafx.fxml;
    exports com.example.ilisismarthomesma;
}