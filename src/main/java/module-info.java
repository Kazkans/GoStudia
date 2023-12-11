module com.example.gostudia {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.gostudia to javafx.fxml;
    exports com.example.gostudia;
}