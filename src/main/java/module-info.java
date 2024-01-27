module com.example.ilisismarthomesma {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.ilisismarthomesma to javafx.fxml;
    exports com.example.ilisismarthomesma;
}