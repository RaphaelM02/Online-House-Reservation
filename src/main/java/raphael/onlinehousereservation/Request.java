package raphael.onlinehousereservation;

import java.sql.Date;

public class Request {
    public int requestID;
    public int propertyID;
    public String propertyName;
    public String propertyHost;
    public String guestName;
    public int guestID;
    Date property_Start_Reservation;
    Date property_End_Reservation;

    //Constructors :
    public Request(int requestID, int propertyID, String propertyName, String propertyHost, String guestName, int guestID, Date property_Start_Reservation, Date property_End_Reservation){
        this.requestID = requestID;
        this.propertyID = propertyID;
        this.propertyName = propertyName;
        this.propertyHost = propertyHost;
        this.guestName = guestName;
        this.guestID = guestID;
        this.property_Start_Reservation = property_Start_Reservation;
        this.property_End_Reservation = property_End_Reservation;
    }

    public Request(String propertyHost, String propertyName, String guestName, Date property_Start_Reservation, Date property_End_Reservation){
        this.propertyHost = propertyHost;
        this.propertyName = propertyName;
        this.guestName = guestName;
        this.property_Start_Reservation = property_Start_Reservation;
        this.property_End_Reservation = property_End_Reservation;
    }

    //Getters & Setters :
    public int getRequestID() {return requestID;}
    public void setRequestID(int requestID) {this.requestID = requestID;}

    public int getPropertyID() {return propertyID;}
    public void setPropertyID(int propertyID) {this.propertyID = propertyID;}

    public String getPropertyName() {return propertyName;}
    public void setPropertyName(String propertyName) {this.propertyName = propertyName;}

    public String getPropertyHost() {return propertyHost;}
    public void setPropertyHost(String propertyHost) {this.propertyHost = propertyHost;}

    public String getGuestName() {return guestName;}
    public void setGuestName(String guestName) {this.guestName = guestName;}

    public int getGuestID() {return guestID;}
    public void setGuestID(int guestID) {this.guestID = guestID;}

    public Date getProperty_Start_Reservation() {return property_Start_Reservation;}
    public void setProperty_Start_Reservation(Date property_Start_Reservation) {this.property_Start_Reservation = property_Start_Reservation;}

    public Date getProperty_End_Reservation() {return property_End_Reservation;}
    public void setProperty_End_Reservation(Date property_End_Reservation) {this.property_End_Reservation = property_End_Reservation;}
}
