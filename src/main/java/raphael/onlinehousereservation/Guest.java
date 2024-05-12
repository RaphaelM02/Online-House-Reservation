package raphael.onlinehousereservation;

public class Guest {
    private int guestID, guestPhoneNumber;
    private String guestFirstName, guestLastName, guestFullName, guestContactInfo;

    public Guest(int guestPhoneNumber, String guestFirstName, String guestLastName, String guestContactInfo){
        this.guestPhoneNumber = guestPhoneNumber;
        this.guestFirstName = guestFirstName;
        this.guestLastName = guestLastName;
        this.guestFullName = guestFirstName + guestLastName;
        this.guestContactInfo = guestContactInfo;
    }

    public int getGuestID(){return this.guestID;}
    public void setGuestID(int guestID){this.guestID = guestID;}

    public int getGuestPhoneNumber(){return this.guestPhoneNumber;}
    public void setGuestPhoneNumber(int guestPhoneNumber){this.guestPhoneNumber = guestPhoneNumber;}

    public String getGuestFirstName(){return this.guestFirstName;}
    public void setGuestFirstName(String guestFirstName){this.guestFirstName = guestFirstName;}

    public String getGuestLastName(){return this.guestLastName;}
    public void setGuestLastName(String guestLastName){this.guestLastName = guestLastName;}

    public String getGuestFullName(){return this.guestFullName;}
    public void setGuestFullName(String guestFullName){this.guestFullName = this.guestFirstName+" "+this.guestLastName;}

    public String getGuestContactInfo(){return this.guestContactInfo;}
    public void setGuestContactInfo(String guestContactInfo){this.guestContactInfo = guestContactInfo;}
}
