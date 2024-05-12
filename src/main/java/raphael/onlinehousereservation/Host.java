package raphael.onlinehousereservation;

import java.util.ArrayList;

public class Host {
    private int hostID, hostPhoneNumber;
    private String hostFirstName, hostLastName, hostFullName, hostContactInfo;
    private ArrayList<Property> properties;

    public Host(int hostPhoneNumber, String hostFirstName, String hostLastName, String hostContactInfo){
        this.hostPhoneNumber = hostPhoneNumber;
        this.hostFirstName = hostFirstName;
        this.hostLastName = hostLastName;
        this.hostFullName = hostFirstName + hostLastName;
        this.hostContactInfo = hostContactInfo;
        this.properties = new ArrayList<>();
    }

    //Methods :
    public void addProperty(Property property){properties.add(property);}
    public void removeProperty(Property property){properties.remove(property);}


    //Getters & Setters :
    public int getHostID(){return this.hostID;}
    public void setHostID(int hostID){this.hostID = hostID;}

    public int getHostPhoneNumber(){return this.hostPhoneNumber;}
    public void setHostPhoneNumber(int hostPhoneNumber){this.hostPhoneNumber = hostPhoneNumber;}

    public String getHostFirstName(){return this.hostFirstName;}
    public void setHostFirstName(String hostFirstName){this.hostFirstName = hostFirstName;}

    public String getHostLastName(){return this.hostLastName;}
    public void setHostLastName(String hostLastName){this.hostLastName = hostLastName;}

    public String getHostFullName(){return this.hostFullName;}
    public void setHostFullName(String hostFullName) {this.hostFullName = this.hostFirstName + this.hostLastName;}

    public String getHostContactInfo(){return this.hostContactInfo;}
    public void setHostContactInfo(String hostContactInfo){this.hostContactInfo = hostContactInfo;}

    public ArrayList<Property> getProperties() {return properties;}
    public void setProperties(ArrayList<Property> properties) {this.properties = properties;}
}
