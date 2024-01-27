module com.example.gostudia {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    opens com.example.gostudia to javafx.fxml;
    exports com.example.gostudia;
    exports com.example.gostudia.Client;
    opens com.example.gostudia.Client to javafx.fxml;
    exports com.example.gostudia.Server;
    opens com.example.gostudia.Server to javafx.fxml;
    exports com.example.gostudia.Server.InputOperations;
    opens com.example.gostudia.Server.InputOperations to javafx.fxml;
    exports com.example.gostudia.Server.Players;
    opens com.example.gostudia.Server.Players to javafx.fxml;
    exports com.example.gostudia.Database;
    opens com.example.gostudia.Database to org.hibernate.orm.core;

}