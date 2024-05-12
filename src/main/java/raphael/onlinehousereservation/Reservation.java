package raphael.onlinehousereservation;

import java.sql.Date;

public class Reservation {
    int reservation_ID;
    int property_ID;
    String property_Name;
    String property_Host;
    String property_Address;
    Date property_Build_Date;
    int property_No_Bedrooms;
    int property_No_Bathrooms;
    double property_Price_Per_Night;
    boolean is_Property_Available;
    String property_Amenities;
    Date property_Start_Reservation;
    Date property_End_Reservation;

    public Reservation(int reservation_ID, int property_ID, String property_Name, String property_Host, boolean is_Property_Available, Date property_Start_Reservation, Date property_End_Reservation){
        this.reservation_ID = reservation_ID;
        this.property_ID = property_ID;
        this.property_Name = property_Name;
        this.property_Host = property_Host;
        this.is_Property_Available = is_Property_Available;
        this.property_Start_Reservation = property_Start_Reservation;
        this.property_End_Reservation = property_End_Reservation;
    }

    public Reservation(String property_Name){
        this.property_Name = property_Name;
    }

    public int getReservation_ID() {return reservation_ID;}
    public void setReservation_ID(int reservation_ID) {this.reservation_ID = reservation_ID;}

    public int getProperty_ID() {return property_ID;}
    public void setProperty_ID(int property_ID) {this.property_ID = property_ID;}

    public String getProperty_Name() {return property_Name;}
    public void setProperty_Name(String property_Name) {this.property_Name = property_Name;}

    public String getProperty_Host() {return property_Host;}
    public void setProperty_Host(String property_Host) {this.property_Host = property_Host;}

    public String getProperty_Address() {return property_Address;}
    public void setProperty_Address(String property_Address) {this.property_Address = property_Address;}

    public Date getProperty_Build_Date() {return property_Build_Date;}
    public void setProperty_Build_Date(Date property_Build_Date) {this.property_Build_Date = property_Build_Date;}

    public int getProperty_No_Bedrooms() {return property_No_Bedrooms;}
    public void setProperty_No_Bedrooms(int property_No_Bedrooms) {this.property_No_Bedrooms = property_No_Bedrooms;}

    public int getProperty_No_Bathrooms() {return property_No_Bathrooms;}
    public void setProperty_No_Bathrooms(int property_No_Bathrooms) {this.property_No_Bathrooms = property_No_Bathrooms;}

    public double getProperty_Price_Per_Night() {return property_Price_Per_Night;}
    public void setProperty_Price_Per_Night(double property_Price_Per_Night) {this.property_Price_Per_Night = property_Price_Per_Night;}

    public boolean isIs_Property_Available() {return is_Property_Available;}
    public void setIs_Property_Available(boolean is_Property_Available) {this.is_Property_Available = is_Property_Available;}

    public String getProperty_Amenities() {return property_Amenities;}
    public void setProperty_Amenities(String property_Amenities) {this.property_Amenities = property_Amenities;}

    public Date getProperty_Start_Reservation() {return property_Start_Reservation;}
    public void setProperty_Start_Reservation(Date property_Start_Reservation) {this.property_Start_Reservation = property_Start_Reservation;}

    public Date getProperty_End_Reservation() {return property_End_Reservation;}
    public void setProperty_End_Reservation(Date property_End_Reservation) {this.property_End_Reservation = property_End_Reservation;}
}