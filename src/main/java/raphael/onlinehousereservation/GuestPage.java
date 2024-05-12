package raphael.onlinehousereservation;

import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.sql.*;

public class GuestPage{
    Connection connection = DBConnection.ConnectDBUser();
    Settings settings = new Settings();
    Stage window, window2;
    Scene guestScene;
    String loggedInUser;
    int loggedInUserID;
    private static final double BUTTON_HEIGHT = 30;
    ObservableList<Property> propertyObservableList;
    private final TableView<Property> propertyTableView = new TableView<>();
    String selectedProperty;
    TextField propertyNameTF = new TextField();
    TextField propertyOwnerTF = new TextField();
    TextField propertyAddressTF = new TextField();
    TextField propertyBuildDateTF = new TextField();
    TextField noOfBedroomsTF = new TextField();
    TextField noOfBathroomsTF = new TextField();
    TextField pricePerNightTF = new TextField();
    TextField amenitiesTF = new TextField();
    DatePicker startDatePicker = new DatePicker();
    DatePicker endDatePicker = new DatePicker();
    Rectangle2D screenBounds = Screen.getPrimary().getBounds();
    Image bgImg = new Image(new File("Images/background.png").toURI().toString());
    TextField subjectTF = new TextField();
    TextArea messageArea = new TextArea();
    TextArea replyArea = new TextArea();
    int hostId;
    int propertyID;
    Date startReservationDate, endReservationDate;

    //Methods :
    public void LogOut(){
        try {
            PreparedStatement copyToLoggedOut = connection.prepareStatement("INSERT INTO logged_out_user(user_id, user_name, user_email, login_date)  (SELECT * from temp_user)");
            copyToLoggedOut.execute();
            PreparedStatement logOutDate = connection.prepareStatement("UPDATE temp_user, logged_out_user set logged_out_user.logout_date = NOW() where (logged_out_user.user_id = temp_user.user_token AND logged_out_user.login_date = temp_user.login_date)");
            logOutDate.execute();
            PreparedStatement deleteTemp = connection.prepareStatement("DELETE from temp_user");
            deleteTemp.execute();
            System.exit(0);
        }catch (Exception noConnection){
            noConnection.getMessage();
            SaveExceptions.saveExceptionToXml(noConnection);
        }
    }
    public String getLoggedIn(){
        try {
            PreparedStatement getLoggedInUser = connection.prepareStatement("SELECT user_name from temp_user");
            ResultSet loggedInUserName = getLoggedInUser.executeQuery();
            while (loggedInUserName.next()){
                loggedInUser = loggedInUserName.getString("user_name");
            }
            return loggedInUser;
        }catch (Exception noConnection){
            noConnection.getMessage();
            SaveExceptions.saveExceptionToXml(noConnection);
            return "Error";
        }
    }
    public int getLoggedInUserID(){
        try {
            PreparedStatement getLoggedInUserID = connection.prepareStatement("SELECT user_token from temp_user");
            ResultSet loggedInUserIDRS = getLoggedInUserID.executeQuery();
            while (loggedInUserIDRS.next()){
                loggedInUserID = loggedInUserIDRS.getInt("user_token");
            }
            return loggedInUserID;
        }catch (Exception noConnection){
            noConnection.getMessage();
            SaveExceptions.saveExceptionToXml(noConnection);
            return 0;
        }
    }
    public void Get_Property_Data(String property){
        try {
            PreparedStatement getPropertyData = connection.prepareStatement("SELECT * FROM property WHERE property_Name = ?");
            getPropertyData.setString(1, selectedProperty);
            ResultSet getPropertyDataRS = getPropertyData.executeQuery();
            while (getPropertyDataRS.next()){
                propertyID = getPropertyDataRS.getInt("property_ID");
                System.out.println(propertyID);
                propertyNameTF.setText(getPropertyDataRS.getString("property_Name"));
                propertyOwnerTF.setText(getPropertyDataRS.getString("property_Host"));
                propertyAddressTF.setText(getPropertyDataRS.getString("property_Address"));
                propertyBuildDateTF.setText(String.valueOf(getPropertyDataRS.getDate("property_Build_Date")));
                noOfBedroomsTF.setText(String.valueOf(getPropertyDataRS.getInt("property_No_Bedrooms")));
                noOfBathroomsTF.setText(String.valueOf(getPropertyDataRS.getInt("property_No_Bathrooms")));
                pricePerNightTF.setText(String.valueOf(getPropertyDataRS.getDouble("property_Price_Per_Night")));
                amenitiesTF.setText(getPropertyDataRS.getString("property_Amenities"));
            }
        }catch (SQLException Get_Property_Data_Exception){
            Get_Property_Data_Exception.getMessage();
            SaveExceptions.saveExceptionToXml(Get_Property_Data_Exception);
        }
    }
    public void Send_Message(){
        try {
            PreparedStatement getHostIDStmt = connection.prepareStatement("SELECT host_ID FROM property, testuser WHERE property_Host = ?;");
            getHostIDStmt.setString(1, propertyOwnerTF.getText());
            ResultSet getHostIDRS = getHostIDStmt.executeQuery();
            while (getHostIDRS.next()){
                hostId = getHostIDRS.getInt("host_ID");
            }
            PreparedStatement sendMessageStmt = connection.prepareStatement("INSERT INTO messages(host_ID, guest_ID, host_Name, guest_Name, message_Subject, message_Content) VALUES (?,?,?,?,?,?)");
            sendMessageStmt.setInt(1, hostId);
            sendMessageStmt.setInt(2, getLoggedInUserID());
            sendMessageStmt.setString(3, propertyOwnerTF.getText());
            sendMessageStmt.setString(4, getLoggedIn());
            sendMessageStmt.setString(5, subjectTF.getText());
            sendMessageStmt.setString(6, messageArea.getText());
            sendMessageStmt.execute();
        }catch (Exception Send_Message_Exception){
            Send_Message_Exception.getMessage();
            SaveExceptions.saveExceptionToXml(Send_Message_Exception);
        }
    }
    public void Get_Reply(){
        try {
            PreparedStatement getReplyStmt = connection.prepareStatement("SELECT * FROM messages WHERE host_Name=? AND guest_Name=?");
            getReplyStmt.setString(1, propertyOwnerTF.getText());
            getReplyStmt.setString(2, getLoggedIn());
            ResultSet getReplyRS = getReplyStmt.executeQuery();
            while (getReplyRS.next()){
                subjectTF.setText(getReplyRS.getString("message_Subject"));
                messageArea.setText(getReplyRS.getString("message_Content"));
                replyArea.setText(getReplyRS.getString("reply"));
                if (!subjectTF.getText().isEmpty() && !messageArea.getText().isEmpty()){
                    subjectTF.setEditable(false);
                    messageArea.setEditable(false);
                }
            }
        }catch (Exception Get_Reply_Exception){
            Get_Reply_Exception.getMessage();
            SaveExceptions.saveExceptionToXml(Get_Reply_Exception);
        }
    }
    public void Reserve(){
        try {
            Get_Property_Data(selectedProperty);
            PreparedStatement getHostIDStmt = connection.prepareStatement("SELECT host_ID FROM property, testuser WHERE property_Host = ?;");
            getHostIDStmt.setString(1, propertyOwnerTF.getText());
            ResultSet getHostIDRS = getHostIDStmt.executeQuery();
            if(getHostIDRS.next()){
                hostId = Integer.parseInt(getHostIDRS.getString("host_ID"));
            }
            PreparedStatement reserveStmt = connection.prepareStatement("INSERT INTO requests(property_ID, property_Name, property_Host, host_ID, guest_Name, guest_ID, property_Start_Reservation, property_End_Reservation) VALUES (?,?,?,?,?,?,?,?)");
            reserveStmt.setInt(1, propertyID);
            reserveStmt.setString(2, propertyNameTF.getText());
            reserveStmt.setString(3, propertyOwnerTF.getText());
            reserveStmt.setInt(4, hostId);
            reserveStmt.setString(5, getLoggedIn());
            reserveStmt.setInt(6, getLoggedInUserID());
            reserveStmt.setDate(7, Date.valueOf(startDatePicker.getValue()));
            reserveStmt.setDate(8, Date.valueOf(endDatePicker.getValue()));
            reserveStmt.execute();
        }catch (Exception Reserve_Exception){
            Reserve_Exception.getMessage();
            SaveExceptions.saveExceptionToXml(Reserve_Exception);
        }
    }
    public void Check_Reservation(){
        try {
            Get_Property_Data(selectedProperty);
            PreparedStatement checkIfReserved = connection.prepareStatement("SELECT * FROM reservations WHERE property_Name = ?");
            checkIfReserved.setString(1, selectedProperty);
            ResultSet checkIfReservedRS = checkIfReserved.executeQuery();
            checkIfReservedRS.next();
            do {
                startReservationDate = checkIfReservedRS.getDate("property_Start_Reservation");
                System.out.println(startReservationDate);
                endReservationDate = checkIfReservedRS.getDate("property_End_Reservation");
                System.out.println(endReservationDate);
                if ((startReservationDate.equals(Date.valueOf(startDatePicker.getValue())) && endReservationDate.equals(Date.valueOf(endDatePicker.getValue())))) {
                    JOptionPane.showMessageDialog(null, "Property reserved in that time");
                    break;
                } else {
                    Reserve();
                }
            } while (checkIfReservedRS.next());
        }catch (Exception Check_Reservation_Exception){
            Check_Reservation_Exception.getMessage();
            SaveExceptions.saveExceptionToXml(Check_Reservation_Exception);
        }
    }

    public Stage GuestStage(){
        Text login = new Text("Your Dashboard");
        login.setId("welcome-text");
        Stage guestStage = new Stage();
        window = guestStage;

        BorderPane guestBorder = new BorderPane();

        VBox guestVBox = new VBox(10);
        guestVBox.setPadding(new Insets(10,10,10,10));

        GridPane guestGrid = new GridPane(10,10);
        guestGrid.setPadding(new Insets(10,10,10,10));
        guestGrid.setAlignment(Pos.CENTER);
        guestGrid.add(login,0,0);
        guestGrid.setId("host-grid");

        Button btHome = new Button("Home");
        Button btList = new Button("Properties");
        Button btSettings = new Button("Account Settings");
        Button btLogout = new Button("Logout");

        //Home button
        btHome.setPrefSize(Region.USE_COMPUTED_SIZE, BUTTON_HEIGHT);
        File homeFile = new File("Images/home-1-svgrepo-com-test.png");
        Image homeImage = new Image(homeFile.toURI().toString());
        ImageView homeIV = new ImageView(homeImage);
        homeIV.setFitHeight(BUTTON_HEIGHT);
        homeIV.setPreserveRatio(true);
        btHome.setId("home-button");
        btHome.setGraphic(homeIV);

        //List button
        btList.setPrefSize(Region.USE_COMPUTED_SIZE, BUTTON_HEIGHT);
        File listFile = new File("Images/list-1-svgrepo-com.png");
        Image listImage = new Image(listFile.toURI().toString());
        ImageView listIV = new ImageView(listImage);
        listIV.setFitHeight(BUTTON_HEIGHT);
        listIV.setPreserveRatio(true);
        btList.setId("list-button");
        btList.setGraphic(listIV);

        //Settings button
        btSettings.setPrefSize(Region.USE_COMPUTED_SIZE, BUTTON_HEIGHT);
        File settingsFile = new File("Images/settings-svgrepo-com-test.png");
        Image settingsImage = new Image(settingsFile.toURI().toString());
        ImageView settingsIV = new ImageView(settingsImage);
        settingsIV.setFitHeight(BUTTON_HEIGHT);
        settingsIV.setPreserveRatio(true);
        btSettings.setId("settings-button");
        btSettings.setGraphic(settingsIV);

        //Logout button
        btLogout.setPrefSize(Region.USE_COMPUTED_SIZE, BUTTON_HEIGHT);
        File logoutFile = new File("Images/logout-2-svgrepo-com.png");
        Image logoutImage = new Image(logoutFile.toURI().toString());
        ImageView logoutIV = new ImageView(logoutImage);
        logoutIV.setFitHeight(BUTTON_HEIGHT);
        logoutIV.setPreserveRatio(true);
        btLogout.setId("logout-button");
        btLogout.setGraphic(logoutIV);

        guestVBox.getChildren().addAll(btHome, btList, btSettings, btLogout);
        guestVBox.setAlignment(Pos.CENTER);

        guestBorder.setTop(guestGrid);
        guestBorder.setLeft(guestVBox);
        guestBorder.setCenter(GuestHomeBorderPane());

        btHome.setOnAction(showHome -> {
            propertyTableView.getColumns().clear();
            guestBorder.setCenter(GuestHomeBorderPane());
        });
        btList.setOnAction(showProperties -> {
            propertyTableView.getColumns().clear();
            guestBorder.setCenter(GuestPropertiesBorderPane());
        });
        btSettings.setOnAction(showSettings -> guestBorder.setCenter(settings.SettingsBorderPane()));
        btLogout.setOnAction(logOut -> LogOut());

        Image bgImg = new Image(new File("Images/background.png").toURI().toString());
        guestBorder.setBackground(new Background(new BackgroundImage(bgImg,BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,BackgroundSize.DEFAULT)));
        guestScene = new Scene(guestBorder);
        guestScene.getStylesheets().add(new File("Css/guestpage.css").toURI().toString());
        window.setScene(guestScene);
        window.setMaximized(true);
        window.show();
        return window;
    }

    public BorderPane GuestHomeBorderPane(){
        Text welcomeText = new Text("Welcome back, "+getLoggedIn());
        welcomeText.setStyle("-fx-text-alignment: center; -fx-fill: limegreen; -fx-font-family: 'Arial Black'; -fx-font-size: 24px");

        BorderPane guestHomeBorderPane = new BorderPane();
        guestHomeBorderPane.setTop(welcomeText);
        return guestHomeBorderPane;
    }

    public BorderPane GuestPropertiesBorderPane(){
        Text welcomeText = new Text("Reservation is one button away !");
        welcomeText.setStyle("-fx-text-alignment: center; -fx-fill: limegreen; -fx-font-family: 'Arial Black'; -fx-font-size: 24px");

        TableColumn<Property, String> propertyNameCol = new TableColumn<>("Properties");
        propertyNameCol.setCellValueFactory(new PropertyValueFactory<>("property_Name"));

        propertyObservableList = DBConnection.Get_Property_By_Guest();
        propertyTableView.setItems(propertyObservableList);

        propertyTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        propertyNameCol.minWidthProperty().bind(propertyTableView.widthProperty().multiply(1));
        propertyTableView.getColumns().add(propertyNameCol);

        VBox guestPropertiesVBox = new VBox(10);
        guestPropertiesVBox.setPadding(new Insets(100,10,5,10));
        guestPropertiesVBox.getChildren().add(propertyTableView);

        propertyTableView.setRowFactory(event -> {
            TableRow<Property> row = new TableRow<>();
            row.setOnMouseClicked(getInfo -> {
                if (getInfo.getButton() == MouseButton.PRIMARY && getInfo.getClickCount() == 2 && !row.isEmpty()) {
                    Property rowData = row.getItem();
                    selectedProperty = rowData.getProperty_Name();
                    System.out.println(selectedProperty);
                    window.hide();
                    PropertyStage();
                    Get_Property_Data(selectedProperty);
                }
            });
            return row;
        });

        BorderPane guestPropertiesBorderPane = new BorderPane();
        guestPropertiesBorderPane.setTop(welcomeText);
        guestPropertiesBorderPane.setCenter(guestPropertiesVBox);
        return guestPropertiesBorderPane;
    }

    public Stage PropertyStage(){
        Stage propertyStage = new Stage();
        window2 = propertyStage;

        GridPane guestPropertiesGrid = new GridPane(10,10);
        guestPropertiesGrid.setAlignment(Pos.CENTER);
        guestPropertiesGrid.setPadding(new Insets(10,10,10,10));

        Label propertyNameLabel = new Label("Property Name : ");
        Label propertyOwnerLabel = new Label("Owner : ");
        Label propertyAddressLabel =  new Label("Address : ");
        Label propertyBuildDateLabel = new Label("Build Date : ");
        Label noOfBedroomsLabel = new Label("No Of Bedrooms : ");
        Label noOfBathroomsLabel = new Label("No Of Bathrooms : ");
        Label pricePerNightLabel = new Label("Price Per Night : ");
        Label amenitiesLabel = new Label("Amenities : ");

        propertyNameTF.setEditable(false);
        propertyOwnerTF.setEditable(false);
        propertyAddressTF.setEditable(false);
        propertyBuildDateTF.setEditable(false);
        noOfBedroomsTF.setEditable(false);
        noOfBathroomsTF.setEditable(false);
        pricePerNightTF.setEditable(false);
        amenitiesTF.setEditable(false);
        startDatePicker.setEditable(false);
        endDatePicker.setEditable(false);
        startDatePicker.setId("custom-date-picker");
        startDatePicker.setPromptText("Start Date");
        endDatePicker.setId("custom-date-picker");
        endDatePicker.setPromptText("End Date");

        Button btReserve = new Button("Reserve");
        btReserve.setId("bt-Reserve");
        Button btChat = new Button("Chat with owner");
        btChat.setId("bt-Chat");
        Button btBack = new Button("Back");
        btBack.setId("bt-Back");

        btReserve.setOnAction(reserve -> Check_Reservation());
        btChat.setOnAction(chat -> {
            window2.setScene(ChatScene());
            Get_Reply();
        });
        btBack.setOnAction(goBack -> {
            window2.hide();
            GuestStage();
        });

        VBox buttonsVBox = new VBox(10);
        buttonsVBox.setAlignment(Pos.BOTTOM_CENTER);
        buttonsVBox.getChildren().addAll(btReserve, btChat, btBack);

        guestPropertiesGrid.add(propertyNameLabel, 0, 0);
        guestPropertiesGrid.add(propertyNameTF, 1, 0);
        guestPropertiesGrid.add(propertyOwnerLabel, 0, 1);
        guestPropertiesGrid.add(propertyOwnerTF, 1, 1);
        guestPropertiesGrid.add(propertyAddressLabel, 0, 2);
        guestPropertiesGrid.add(propertyAddressTF, 1, 2);
        guestPropertiesGrid.add(propertyBuildDateLabel, 0, 3);
        guestPropertiesGrid.add(propertyBuildDateTF, 1, 3);
        guestPropertiesGrid.add(noOfBedroomsLabel, 0, 4);
        guestPropertiesGrid.add(noOfBedroomsTF, 1, 4);
        guestPropertiesGrid.add(noOfBathroomsLabel, 0, 5);
        guestPropertiesGrid.add(noOfBathroomsTF, 1, 5);
        guestPropertiesGrid.add(pricePerNightLabel, 0, 6);
        guestPropertiesGrid.add(pricePerNightTF, 1, 6);
        guestPropertiesGrid.add(amenitiesLabel, 0, 7);
        guestPropertiesGrid.add(amenitiesTF, 1, 7);
        guestPropertiesGrid.add(startDatePicker, 0, 8);
        guestPropertiesGrid.add(endDatePicker, 1, 8);
        guestPropertiesGrid.add(buttonsVBox, 1, 9);
        GridPane.setHalignment(buttonsVBox, HPos.CENTER);

        guestPropertiesGrid.setBackground(new Background(new BackgroundImage(bgImg,BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,BackgroundSize.DEFAULT)));
        Scene guestPropertiesScene = new Scene(guestPropertiesGrid,screenBounds.getMaxX(),screenBounds.getMaxX());
        window2.setTitle("Property Reservation");
        guestPropertiesScene.getStylesheets().add(new File("Css/guestProperties.css").toURI().toString());
        window2.setScene(guestPropertiesScene);
        window2.setMaximized(true);
        window2.setOnCloseRequest(logOut -> LogOut());
        window2.show();
        return window2;
    }

    public Scene ChatScene(){
        Text chatText = new Text("Chat with owner !");
        chatText.setStyle("-fx-text-alignment: center; -fx-fill: limegreen; -fx-font-family: 'Arial Black'; -fx-font-size: 24px");

        Label subjectLabel = new Label("Subject : ");
        Label messageLabel = new Label("Message : ");
        Label replyLabel = new Label("Reply : ");
        subjectTF.setPromptText("Subject");
        messageArea.setPromptText("Message");
        messageArea.setPrefHeight(100);
        replyArea.setPrefHeight(100);
        replyArea.setEditable(false);
        Button btSend = new Button("Send");
        Button btBack = new Button("Back");

        GridPane chatGrid = new GridPane(10,10);
        chatGrid.setAlignment(Pos.CENTER);
        chatGrid.setPadding(new Insets(10,10,10,10));

        chatGrid.add(chatText,1,0);
        chatGrid.add(subjectLabel,0,1);
        chatGrid.add(subjectTF,1,1);
        chatGrid.add(messageLabel,0,2);
        chatGrid.add(messageArea,1,2);
        chatGrid.add(replyLabel,0,3);
        chatGrid.add(replyArea,1,3);
        chatGrid.add(btSend,1,4);
        chatGrid.add(btBack,1,5);
        GridPane.setHalignment(chatText, HPos.CENTER);
        GridPane.setHalignment(btSend, HPos.CENTER);
        GridPane.setHalignment(btBack, HPos.CENTER);

        btSend.setOnAction(sendMessage -> Send_Message());
        btBack.setOnAction(goBack -> {
            window2.hide();
            PropertyStage();
        });

        chatGrid.setBackground(new Background(new BackgroundImage(bgImg,BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,BackgroundSize.DEFAULT)));
        Scene chatScene = new Scene(chatGrid, screenBounds.getMaxX(), screenBounds.getMaxY());
        chatScene.getStylesheets().add(new File("Css/chatScene.css").toURI().toString());
        return chatScene;
    }
}
