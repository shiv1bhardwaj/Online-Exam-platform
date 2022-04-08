module com.example.taex {
    requires javafx.controls;
    requires javafx.fxml;
    requires  java.sql;


    opens com.example.taex to javafx.fxml;
    exports com.example.taex;
}