module com.example.tpcanvas {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires junit;
    requires org.testng;
    requires org.junit.jupiter.api;

    opens com.example.tpcanvas to javafx.fxml;
    exports com.example.tpcanvas;
    exports vue;
    exports modele;
    exports controleur;
}