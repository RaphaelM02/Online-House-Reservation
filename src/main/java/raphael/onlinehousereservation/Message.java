package raphael.onlinehousereservation;

public class Message {
    int message_ID;
    int host_ID;
    int guest_ID;
    String hostName;
    String guestName;
    String messageContent;
    boolean isAddressed;

    //Constructors :
    public Message(String hostName, String guestName, String messageContent, boolean isAddressed) {
        this.hostName = hostName;
        this.guestName = guestName;
        this.messageContent = messageContent;
        this.isAddressed = isAddressed;
    }

    public Message(String guestName){
        this.guestName = guestName;
    }

    //Getters & Setters :
    public int getMessage_ID() {return message_ID;}
    public void setMessage_ID(int message_ID) {this.message_ID = message_ID;}

    public int getHost_ID() {return host_ID;}
    public void setHost_ID(int host_ID) {this.host_ID = host_ID;}

    public int getGuest_ID() {return guest_ID;}
    public void setGuest_ID(int guest_ID) {this.guest_ID = guest_ID;}

    public String getHostName() {return hostName;}
    public void setHostName(String hostName) {this.hostName = hostName;}

    public String getGuestName() {return guestName;}
    public void setGuestName(String guestName) {this.guestName = guestName;}

    public String getMessageContent() {return messageContent;}
    public void setMessageContent(String messageContent) {this.messageContent = messageContent;}

    public boolean isAddressed() {return isAddressed;}
    public void setAddressed(boolean addressed) {isAddressed = addressed;
    }
}
