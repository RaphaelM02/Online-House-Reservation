package raphael.onlinehousereservation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.swing.*;
import java.sql.*;

public class DBConnection {
    public static Connection ConnectDBUser(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection =  DriverManager.getConnection("jdbc:mysql://172.16.130.140:3306/testdb","RAPHAEL_UNI","raphadmin@123");
            return connection;
        }catch (Exception noConnection){
            noConnection.printStackTrace();
            SaveExceptions.saveExceptionToXml(noConnection);
            return ConnectDBUser();
        }
    }

    public static ObservableList<Property> Get_Property_By_Host(){
        Connection connection = ConnectDBUser();
        ObservableList<Property> hostProperties = FXCollections.observableArrayList();
        try {
            assert connection != null;
            PreparedStatement getHostProperties = connection.prepareStatement("SELECT property_ID, property_Name, property_Host, property_Address, property_Build_Date, property_No_Bedrooms, property_No_Bathrooms, property_Price_Per_Night, is_Property_Available, property_Amenities FROM property, temp_user tst where tst.user_name = property_Host;");
            ResultSet hostPropertiesRS = getHostProperties.executeQuery();
            while (hostPropertiesRS.next()){
                hostProperties.add(new Property(hostPropertiesRS.getInt("property_ID"),hostPropertiesRS.getString("property_Name"),hostPropertiesRS.getString("property_Host"),hostPropertiesRS.getString("property_Address"),hostPropertiesRS.getDate("property_Build_Date"),hostPropertiesRS.getInt("property_No_Bedrooms"),hostPropertiesRS.getInt("property_No_Bathrooms"),hostPropertiesRS.getDouble("property_Price_Per_Night"),hostPropertiesRS.getBoolean("is_Property_Available"),hostPropertiesRS.getString("property_Amenities")));
            }
            return hostProperties;
        }catch (Exception Get_Property_By_Host_Exception){
            JOptionPane.showMessageDialog(null, Get_Property_By_Host_Exception.getMessage());
            SaveExceptions.saveExceptionToXml(Get_Property_By_Host_Exception);
            return null;
        }
    }

    public static ObservableList<Reservation> Get_Reservations_By_Host(){
        Connection connection = ConnectDBUser();
        ObservableList<Reservation> HOST_PROPERTIES_RESERVATIONS = FXCollections.observableArrayList();
        try {
            assert connection != null;
            PreparedStatement getHostReservations = connection.prepareStatement("SELECT reservation_ID, property_ID, property_Name,property_Host,is_Property_Available,property_Start_Reservation,property_End_Reservation FROM reservations r, temp_user temp WHERE temp.user_name = r.property_Host;");
            ResultSet hostReservationsRS = getHostReservations.executeQuery();
            while (hostReservationsRS.next()){
                HOST_PROPERTIES_RESERVATIONS.add(new Reservation(hostReservationsRS.getInt("reservation_ID"), hostReservationsRS.getInt("property_ID"),hostReservationsRS.getString("property_Name"),hostReservationsRS.getString("property_Host"),hostReservationsRS.getBoolean("is_Property_Available"), hostReservationsRS.getDate("property_Start_Reservation"), hostReservationsRS.getDate("property_End_Reservation")));
            }
            return HOST_PROPERTIES_RESERVATIONS;
        }catch (SQLException sqlException){
            JOptionPane.showMessageDialog(null, sqlException.getMessage());
            SaveExceptions.saveExceptionToXml(sqlException);
            return null;
        }
    }

    public static ObservableList<Request> Get_Requests_By_Host(){
        Connection connection = ConnectDBUser();
        ObservableList<Request> HOST_PROPERTIES_REQUESTS = FXCollections.observableArrayList();
        try {
            assert connection != null;
            PreparedStatement getHostRequests = connection.prepareStatement("SELECT property_Name, property_Host, guest_Name, property_Start_Reservation, property_End_Reservation FROM requests, temp_user WHERE temp_user.user_name = requests.property_Host;");
            ResultSet hostRequestsRS = getHostRequests.executeQuery();
            while (hostRequestsRS.next()){
                HOST_PROPERTIES_REQUESTS.add(new Request(hostRequestsRS.getString("property_Host"),hostRequestsRS.getString("property_Name"),hostRequestsRS.getString("guest_Name"),hostRequestsRS.getDate("property_Start_Reservation"),hostRequestsRS.getDate("property_End_Reservation")));
            }
            return HOST_PROPERTIES_REQUESTS;
        }catch (SQLException sqlException){
            JOptionPane.showMessageDialog(null,sqlException.getMessage());
            SaveExceptions.saveExceptionToXml(sqlException);
            return null;
        }
    }

    public static ObservableList<Message> Get_Messages_By_Host(){
        Connection connection = ConnectDBUser();
        ObservableList<Message> HOST_MESSAGES = FXCollections.observableArrayList();
        try {
            assert connection != null;
            PreparedStatement getHostMessages = connection.prepareStatement("SELECT guest_Name FROM messages, temp_user WHERE temp_user.user_name = messages.host_Name;");
            ResultSet hostMessagesRS = getHostMessages.executeQuery();
            while (hostMessagesRS.next()){
                HOST_MESSAGES.add(new Message(hostMessagesRS.getString("guest_Name")));
            }
            return HOST_MESSAGES;
        }catch (Exception getMessagesByHostException){
            getMessagesByHostException.getMessage();
            SaveExceptions.saveExceptionToXml(getMessagesByHostException);
            return null;
        }
    }

    public static ObservableList<Property> Get_Property_By_Guest(){
        Connection connection = ConnectDBUser();
        ObservableList<Property> GUEST_PROPERTIES = FXCollections.observableArrayList();
        try {
            assert connection != null;
            PreparedStatement getPropertiesStatement = connection.prepareStatement("SELECT property_Name FROM property");
            ResultSet guestPropertiesRS = getPropertiesStatement.executeQuery();
            while (guestPropertiesRS.next()){
                GUEST_PROPERTIES.add(new Property(guestPropertiesRS.getString("property_Name")));
            }
            return GUEST_PROPERTIES;
        }catch (Exception getPropertyByGuestException){
            getPropertyByGuestException.getMessage();
            SaveExceptions.saveExceptionToXml(getPropertyByGuestException);
            return null;
        }
    }
}
