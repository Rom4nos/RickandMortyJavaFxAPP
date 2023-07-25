module com.example.assignment2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.net.http;
    requires org.json;

    opens com.example.assignment2 to javafx.fxml;
    exports com.example.assignment2;
}