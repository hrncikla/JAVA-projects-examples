module com.example.jj207 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.jj207 to javafx.fxml;
    exports com.example.jj207;
}