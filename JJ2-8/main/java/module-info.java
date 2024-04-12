module lab08.myapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens lab08.myapp to javafx.fxml;
    exports lab08.myapp;
}