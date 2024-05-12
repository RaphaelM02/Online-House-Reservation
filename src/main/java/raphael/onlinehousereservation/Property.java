package raphael.onlinehousereservation;

import java.sql.Date;
import java.util.ArrayList;

public class Property {
    public int property_ID;
    public String property_Name;
    public String property_Host;
    public String property_Address;
    public Date property_Build_Date;
    public int property_No_Bedrooms, property_No_Bathrooms;
    public double property_Price_Per_Night;
    public Boolean is_Property_Available;
    public String property_Amenities;
    public ArrayList<Reservation> reservations;

    public Property(int propertyID, String propertyName, String host, String propertyAddress, Date propertyBuildDate, int numBedrooms, int numBathrooms, double pricePerNight, Boolean isAvailable, String amenities){
        this.property_ID = propertyID;
        this.property_Name = propertyName;
        this.property_Host = host;
        this.property_Address = propertyAddress;
        this.property_Build_Date = propertyBuildDate;
        this.property_No_Bedrooms = numBedrooms;
        this.property_No_Bathrooms = numBathrooms;
        this.property_Price_Per_Night = pricePerNight;
        this.is_Property_Available = isAvailable;
        this.property_Amenities = amenities;
        this.reservations = new ArrayList<>();
    }
    public Property(String propertyName, String host, String propertyAddress, Date propertyBuildDate, int numBedrooms, int numBathrooms, double pricePerNight, Boolean isAvailable, String amenities){
        this.property_Name = propertyName;
        this.property_Host = host;
        this.property_Address = propertyAddress;
        this.property_Build_Date = propertyBuildDate;
        this.property_No_Bedrooms = numBedrooms;
        this.property_No_Bathrooms = numBathrooms;
        this.property_Price_Per_Night = pricePerNight;
        this.is_Property_Available = true;
        this.property_Amenities = amenities;
    }

    public Property(String propertyName){
        this.property_Name = propertyName;
    }

    //Methods :
    public void addReservation(Reservation reservation){reservations.add(reservation);}
    public void removeReservation(Reservation reservation){reservations.remove(reservation);}


    //Getters & Setters :

    public int getProperty_ID() {return property_ID;}
    public void setProperty_ID(int property_ID) {this.property_ID = property_ID;}

    public void setProperty_Name(String property_Name) {this.property_Name = property_Name;}
    public String getProperty_Name() {return property_Name;}

    public void setProperty_Host(String property_Host) {this.property_Host = property_Host;}
    public String getProperty_Host() {return property_Host;}

    public void setProperty_Address(String property_Address) {this.property_Address = property_Address;}
    public String getProperty_Address() {return property_Address;}

    public void setProperty_Build_Date(Date property_Build_Date) {this.property_Build_Date = property_Build_Date;}
    public Date getProperty_Build_Date() {return property_Build_Date;}

    public void setProperty_No_Bedrooms(int property_No_Bedrooms) {this.property_No_Bedrooms = property_No_Bedrooms;}
    public int getProperty_No_Bedrooms() {return property_No_Bedrooms;}

    public void setProperty_No_Bathrooms(int property_No_Bathrooms) {this.property_No_Bathrooms = property_No_Bathrooms;}
    public int getProperty_No_Bathrooms() {return property_No_Bathrooms;}

    public void setProperty_Price_Per_Night(double property_Price_Per_Night) {this.property_Price_Per_Night = property_Price_Per_Night;}
    public double getProperty_Price_Per_Night() {return property_Price_Per_Night;}

    public void setIs_Property_Available(boolean is_Property_Available) {this.is_Property_Available = is_Property_Available;}
    public Boolean getIs_Property_Available() {return is_Property_Available;}

    public void setProperty_Amenities(String property_Amenities) {this.property_Amenities = property_Amenities;}
    public String getProperty_Amenities() {return property_Amenities;}

    public void setReservations(ArrayList<Reservation> reservations) {this.reservations = reservations;}
    public ArrayList<Reservation> getReservations() {return reservations;}

}
