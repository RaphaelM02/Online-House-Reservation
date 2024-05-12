module raphael.onlinehousereservation {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires mysql.connector.j;
    requires java.mail;
    requires protobuf.java;

    opens raphael.onlinehousereservation to javafx.fxml;
    exports raphael.onlinehousereservation;
}