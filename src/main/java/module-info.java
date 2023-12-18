module com.example.gostudia {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.gostudia to javafx.fxml;
    exports com.example.gostudia;
    exports com.example.gostudia.Client;
    opens com.example.gostudia.Client to javafx.fxml;
    exports com.example.gostudia.Server;
    opens com.example.gostudia.Server to javafx.fxml;
}