module com.example.sketching {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;
    requires java.net.http;


    opens com.example.sketching to javafx.fxml;
    exports com.example.sketching;
    exports paint;
    opens paint to javafx.fxml;
}