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
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class HostPage{
    Stage window;
    Scene hostScene;
    Connection connection = DBConnection.ConnectDBUser();
    String loggedInUser;
    int loggedInUserID;
    ObservableList<Property> hostPropertiesObservableList;
    ObservableList<Reservation> hostReservationsObservableList;
    ObservableList<Request> hostRequestsObservableList;
    ObservableList<Message> hostMessagesObservableList;
    private final TableView<Property> hostPropertiesTableView = new TableView<>();
    private final TableView<Reservation> hostReservationsTableView = new TableView<>();
    private final TableView<Request> hostRequestsTableView = new TableView<>();
    private final TableView<Message> hostMessagesTableView = new TableView<>();
    private static final double BUTTON_HEIGHT = 30;
    TextField propertyNameTF = new TextField();
    TextField propertyAddressTF = new TextField();
    DatePicker propertyBuildDateTF = new DatePicker();
    TextField propertyNoOfBedroomsTF = new TextField();
    TextField propertyNoOfBathroomsTF = new TextField();
    TextField propertyPricePerNightTF = new TextField();
    TextField propertyAmenitiesTF = new TextField();
    int houseID;
    TextField subjectTF = new TextField();
    TextArea messageArea  = new TextArea();
    TextArea replyArea = new TextArea();
    Image bgImg = new Image(new File("Images/background.png").toURI().toString());
    Rectangle2D screenBounds = Screen.getPrimary().getBounds();
    String selectedMessage;
    int propertyID, guestID, requestID;
    String selectedProperty, guestName;
    Date startDate, endDate;

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
    public void Add_House(){
        try{
            PreparedStatement addHouseStatement = connection.prepareStatement("INSERT INTO property(property_Name, property_Host, property_Address, property_Build_Date, property_No_Bedrooms, property_No_Bathrooms, property_Price_Per_Night, is_Property_Available, property_Amenities, host_ID) values (?,?,?,?,?,?,?,?,?,?);");
            addHouseStatement.setString(1,propertyNameTF.getText());
            addHouseStatement.setString(2, getLoggedIn());
            addHouseStatement.setString(3,propertyAddressTF.getText());
            addHouseStatement.setDate(4, Date.valueOf(propertyBuildDateTF.getValue()));
            addHouseStatement.setInt(5, Integer.parseInt(propertyNoOfBedroomsTF.getText()));
            addHouseStatement.setInt(6,Integer.parseInt(propertyNoOfBathroomsTF.getText()));
            addHouseStatement.setInt(7, Integer.parseInt(propertyPricePerNightTF.getText()));
            addHouseStatement.setBoolean(8,true);
            addHouseStatement.setString(9,propertyAmenitiesTF.getText());
            addHouseStatement.setInt(10, getLoggedInUserID());
            addHouseStatement.execute();
            JOptionPane.showMessageDialog(null, "Property added");
        }catch (Exception exception){
            exception.getMessage();
            SaveExceptions.saveExceptionToXml(exception);
        }
    }
    public void Delete_House_Using_Name(String propertyName){
        try {
            PreparedStatement getPropertyHost = connection.prepareStatement("SELECT property.property_Host from property");
            ResultSet propertyHostRS = getPropertyHost.executeQuery();
            while (propertyHostRS.next()){
                String propertyHost = propertyHostRS.getString("property_Host");
            }
            PreparedStatement deleteHouseStatement = connection.prepareStatement("DELETE FROM property where property_Name = ?");
            deleteHouseStatement.setString(1,propertyName);
            deleteHouseStatement.execute();
            JOptionPane.showMessageDialog(null,"Property successfully deleted");
        }catch (Exception exception){
            exception.getMessage();
            SaveExceptions.saveExceptionToXml(exception);
            JOptionPane.showMessageDialog(null,"Property could not be deleted");
        }
    }
    public void Get_Message_Data(){
        try {
            PreparedStatement getMessageDataStmt = connection.prepareStatement("SELECT message_Subject, message_Content FROM messages WHERE guest_Name = ?");
            getMessageDataStmt.setString(1, selectedMessage);
            ResultSet getMessageDataRS = getMessageDataStmt.executeQuery();
            while (getMessageDataRS.next()){
                subjectTF.setText(getMessageDataRS.getString("message_Subject"));
                messageArea.setText(getMessageDataRS.getString("message_Content"));
            }
        }catch (Exception Get_Message_Data_Exception){
            Get_Message_Data_Exception.getMessage();
            SaveExceptions.saveExceptionToXml(Get_Message_Data_Exception);
        }
    }
    public void Send_Reply(){
        try {
            Get_Message_Data();
            PreparedStatement replyStmt = connection.prepareStatement("UPDATE messages SET is_Addressed = 1, reply = ? WHERE guest_Name = ?");
            replyStmt.setString(1, replyArea.getText());
            replyStmt.setString(2, selectedMessage);
            replyStmt.execute();
        }catch (Exception reply_Exception){
            reply_Exception.getMessage();
            SaveExceptions.saveExceptionToXml(reply_Exception);
        }
    }
    public void Approve_Request(){
        try {
            PreparedStatement getPropertyInfo = connection.prepareStatement("SELECT * FROM requests WHERE property_Name = ?");
            getPropertyInfo.setString(1, selectedProperty);
            ResultSet propertyInfoRS = getPropertyInfo.executeQuery();
            while (propertyInfoRS.next()){
                propertyID = propertyInfoRS.getInt("property_ID");
                guestID = propertyInfoRS.getInt("guest_ID");
            }
            PreparedStatement approveStmt = connection.prepareStatement("INSERT INTO reservations(property_ID, host_ID, guest_ID, property_Name, property_Host, guest_Name, is_Property_Available, property_Start_Reservation, property_End_Reservation) VALUES (?,?,?,?,?,?,?,?,?)");
            approveStmt.setInt(1, propertyID);
            approveStmt.setInt(2, getLoggedInUserID());
            approveStmt.setInt(3, guestID);
            approveStmt.setString(4, selectedProperty);
            approveStmt.setString(5, getLoggedIn());
            approveStmt.setString(6, guestName);
            approveStmt.setBoolean(7, false);
            approveStmt.setDate(8, startDate);
            approveStmt.setDate(9, endDate);
            approveStmt.execute();
            JOptionPane.showMessageDialog(null,"Property reserved");
            PreparedStatement deleteRequest = connection.prepareStatement("DELETE FROM requests WHERE request_ID = (SELECT MAX(request_ID));");
            deleteRequest.execute();
            hostRequestsTableView.refresh();
        }catch (Exception Approve_Request_Exception){
            Approve_Request_Exception.getMessage();
            SaveExceptions.saveExceptionToXml(Approve_Request_Exception);
        }
    }
    public void Reject_Request(){
        try {
            PreparedStatement getRequestID = connection.prepareStatement("SELECT request_ID FROM requests WHERE property_Name = ? AND guest_Name = ?");
            getRequestID.setString(1, selectedProperty);
            getRequestID.setString(2, guestName);
            ResultSet requestIDRS = getRequestID.executeQuery();
            System.out.println(selectedProperty);
            System.out.println(guestName);
            while (requestIDRS.next()){
                requestID = requestIDRS.getInt("request_ID");
            }
            System.out.println(requestID);
            PreparedStatement rejectStmt = connection.prepareStatement("DELETE FROM requests WHERE request_ID = ?");
            rejectStmt.setInt(1, requestID);
            rejectStmt.execute();
            JOptionPane.showMessageDialog(null,"Rejected");
            hostRequestsTableView.refresh();
        }catch (Exception Reject_Request_Exception){
            Reject_Request_Exception.getMessage();
            SaveExceptions.saveExceptionToXml(Reject_Request_Exception);
        }
    }

    public Stage HostStage(){
        Text login = new Text("Your Dashboard");
        login.setId("welcome-text");
        Stage hostStage = new Stage();
        window = hostStage;

        BorderPane hostBorder = new BorderPane();

        VBox hostVBox = new VBox(10);
        hostVBox.setPadding(new Insets(10,10,10,10));

        GridPane hostGrid = new GridPane(10,10);
        hostGrid.setPadding(new Insets(10,10,10,10));
        hostGrid.setAlignment(Pos.CENTER);
        hostGrid.add(login,0,0);
        hostGrid.setId("host-grid");

        Button btHome = new Button("Home");
        Button btList = new Button("Properties");
        Button btReservations = new Button("Reservations");
        Button btRequests = new Button("Booking requests");
        Button btMessages = new Button("Messages");
        Button btReviews = new Button("Reviews");
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

        //Reservation button
        btReservations.setPrefSize(Region.USE_COMPUTED_SIZE, BUTTON_HEIGHT);
        File reservationFile = new File("Images/check-in-pin-svgrepo-com.png");
        Image reservationImage = new Image(reservationFile.toURI().toString());
        ImageView reservationIV = new ImageView(reservationImage);
        reservationIV.setFitHeight(BUTTON_HEIGHT);
        reservationIV.setPreserveRatio(true);
        btReservations.setId("reservation-button");
        btReservations.setGraphic(reservationIV);

        //Requests button
        btRequests.setPrefSize(Region.USE_COMPUTED_SIZE,BUTTON_HEIGHT);
        File requestsFile = new File("Images/check-square-svgrepo-com.png");
        Image requestsImage = new Image(requestsFile.toURI().toString());
        ImageView requestsIV = new ImageView(requestsImage);
        requestsIV.setFitHeight(BUTTON_HEIGHT);
        requestsIV.setPreserveRatio(true);
        btRequests.setId("requests-button");
        btRequests.setGraphic(requestsIV);

        //Messages button
        btMessages.setPrefSize(Region.USE_COMPUTED_SIZE, BUTTON_HEIGHT);
        File messagesFile = new File("Images/bubble-1-svgrepo-com.png");
        Image messagesImage = new Image(messagesFile.toURI().toString());
        ImageView messagesIV = new ImageView(messagesImage);
        messagesIV.setFitHeight(BUTTON_HEIGHT);
        messagesIV.setPreserveRatio(true);
        btMessages.setId("messages-button");
        btMessages.setGraphic(messagesIV);

        //Reviews & Ratings button
        btReviews.setPrefSize(Region.USE_COMPUTED_SIZE, BUTTON_HEIGHT);
        File reviewsFile = new File("Images/rating-svgrepo-com.png");
        Image reviewsImage = new Image(reviewsFile.toURI().toString());
        ImageView reviewsIV = new ImageView(reviewsImage);
        reviewsIV.setFitHeight(BUTTON_HEIGHT);
        reviewsIV.setPreserveRatio(true);
        btReviews.setId("reviews-button");
        btReviews.setGraphic(reviewsIV);

        //Logout button
        btLogout.setPrefSize(Region.USE_COMPUTED_SIZE, BUTTON_HEIGHT);
        File logoutFile = new File("Images/logout-2-svgrepo-com.png");
        Image logoutImage = new Image(logoutFile.toURI().toString());
        ImageView logoutIV = new ImageView(logoutImage);
        logoutIV.setFitHeight(BUTTON_HEIGHT);
        logoutIV.setPreserveRatio(true);
        btLogout.setId("logout-button");
        btLogout.setGraphic(logoutIV);

        hostVBox.getChildren().addAll(btHome, btList, btReservations, btRequests, btMessages, btReviews, btLogout);
        hostVBox.setAlignment(Pos.CENTER);

        btHome.setOnAction(showHome -> {
            hostPropertiesTableView.getColumns().clear();
            hostReservationsTableView.getColumns().clear();
            hostRequestsTableView.getColumns().clear();
            hostBorder.setCenter(HostHomeBorderPane());
        });
        btList.setOnAction(showProperties -> {
            hostReservationsTableView.getColumns().clear();
            hostPropertiesTableView.getColumns().clear();
            hostRequestsTableView.getColumns().clear();
            hostBorder.setCenter(HostHousesBorderPane());
        });
        btReservations.setOnAction(showReservations -> {
            hostReservationsTableView.getColumns().clear();
            hostPropertiesTableView.getColumns().clear();
            hostRequestsTableView.getColumns().clear();
            hostBorder.setCenter(HostReservationsBorderPane());
        });
        btRequests.setOnAction(showRequests -> {
            hostRequestsTableView.getColumns().clear();
            hostReservationsTableView.getColumns().clear();
            hostPropertiesTableView.getColumns().clear();
            hostBorder.setCenter(HostRequestsBorderPane());
        });
        btMessages.setOnAction(showMessages -> {
            hostMessagesTableView.getColumns().clear();
            hostRequestsTableView.getColumns().clear();
            hostReservationsTableView.getColumns().clear();
            hostPropertiesTableView.getColumns().clear();
            hostBorder.setCenter(HostMessagesBorderPane());
        });
        btLogout.setOnAction(exit -> LogOut());

        hostBorder.setTop(hostGrid);
        hostBorder.setLeft(hostVBox);
        hostBorder.setCenter(HostHomeBorderPane());
        hostBorder.setBackground(new Background(new BackgroundImage(bgImg,BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,BackgroundSize.DEFAULT)));
        hostScene = new Scene(hostBorder);
        hostScene.getStylesheets().add(new File("Css/hostpage.css").toURI().toString());
        window.setScene(hostScene);
        window.setMaximized(true);
        window.show();
        return window;
    }

    public BorderPane HostHomeBorderPane(){
        Text welcomeText = new Text("Welcome back, "+getLoggedIn());
        welcomeText.setStyle("-fx-text-alignment: center; -fx-fill: limegreen; -fx-font-family: 'Arial Black'; -fx-font-size: 24px");

        BorderPane hostHomeBorderPane = new BorderPane();
        hostHomeBorderPane.setTop(welcomeText);
        return hostHomeBorderPane;
    }

    public BorderPane HostHousesBorderPane(){
        TableColumn<Property, Integer> propertyIDCol = new TableColumn<>("Property ID");
        propertyIDCol.setCellValueFactory(new PropertyValueFactory<>("property_ID"));

        TableColumn<Property, String> propertyNameCol = new TableColumn<>("Property Name");
        propertyNameCol.setCellValueFactory(new PropertyValueFactory<>("property_Name"));

        TableColumn<Property, String> propertyHostCol = new TableColumn<>("Host");
        propertyHostCol.setCellValueFactory(new PropertyValueFactory<>("property_Host"));

        TableColumn<Property, String> propertyAddressCol = new TableColumn<>("Address");
        propertyAddressCol.setCellValueFactory(new PropertyValueFactory<>("property_Address"));

        TableColumn<Property, Date> propertyBuildDateCol = new TableColumn<>("Build Date");
        propertyBuildDateCol.setCellValueFactory(new PropertyValueFactory<>("property_Build_Date"));

        TableColumn<Property,Integer> propertyNoBedrooms = new TableColumn<>("Number of bedrooms");
        propertyNoBedrooms.setCellValueFactory(new PropertyValueFactory<>("property_No_Bedrooms"));

        TableColumn<Property, Integer> propertyNoBathrooms = new TableColumn<>("Number of bathrooms");
        propertyNoBathrooms.setCellValueFactory(new PropertyValueFactory<>("property_No_Bathrooms"));

        TableColumn<Property, Double> propertyPricePerNight = new TableColumn<>("Price per night");
        propertyPricePerNight.setCellValueFactory(new PropertyValueFactory<>("property_Price_Per_Night"));

        TableColumn<Property, Boolean> isAvailableCol = new TableColumn<>("Is Available");
        isAvailableCol.setCellValueFactory(new PropertyValueFactory<>("is_Property_Available"));

        TableColumn<Property, String> propertyAmenitiesCol = new TableColumn<>("Amenities");
        propertyAmenitiesCol.setCellValueFactory(new PropertyValueFactory<>("property_Amenities"));

        hostPropertiesObservableList = DBConnection.Get_Property_By_Host();
        hostPropertiesTableView.setItems(hostPropertiesObservableList);
        hostPropertiesTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        hostPropertiesTableView.getColumns().addAll(propertyIDCol, propertyNameCol, propertyHostCol, propertyAddressCol, propertyBuildDateCol, propertyNoBedrooms, propertyNoBathrooms, propertyPricePerNight, isAvailableCol, propertyAmenitiesCol);

        HBox buttonsHBox = new HBox(5);
        VBox buttonsVBox = new VBox(5);
        buttonsHBox.setPadding(new Insets(5,5,20,5));
        buttonsVBox.setPadding(new Insets(5,5,20,5));

        Button btAdd = new Button("Add");
        btAdd.setId("add_button");
        Button btDelete = new Button("Delete");
        btDelete.setId("delete_button");

        propertyNameTF.setPromptText("Property Name");
        propertyAddressTF.setPromptText("Address");
        propertyBuildDateTF.setEditable(false);
        propertyNoOfBedroomsTF.setPromptText("Number of bedrooms");
        propertyNoOfBathroomsTF.setPromptText("Number of bathrooms");
        propertyPricePerNightTF.setPromptText("Price per night");
        propertyAmenitiesTF.setPromptText("Amenities");

        buttonsHBox.getChildren().addAll(propertyNameTF, propertyAddressTF, propertyBuildDateTF, propertyNoOfBedroomsTF, propertyNoOfBathroomsTF, propertyPricePerNightTF, propertyAmenitiesTF);
        buttonsVBox.getChildren().addAll(btAdd, btDelete);
        buttonsVBox.setAlignment(Pos.CENTER);

        VBox hostHousesVBox = new VBox(5);
        hostHousesVBox.setPadding(new Insets(100,5,5,5));
        hostHousesVBox.getChildren().add(hostPropertiesTableView);

        GridPane hostHousesGrid = new GridPane(10,10);
        hostHousesGrid.setAlignment(Pos.CENTER);
        hostHousesGrid.setPadding(new Insets(10,10,10,10));
        hostHousesGrid.add(buttonsHBox, 0, 0);
        hostHousesGrid.add(buttonsVBox, 0, 1);


        btAdd.setOnAction(add_house -> {
            Add_House();
            hostPropertiesObservableList = DBConnection.Get_Property_By_Host();
            hostPropertiesTableView.setItems(hostPropertiesObservableList);
            propertyNameTF.clear();
            propertyAddressTF.clear();
            propertyBuildDateTF.getEditor().clear();
            propertyNoOfBedroomsTF.clear();
            propertyNoOfBathroomsTF.clear();
            propertyPricePerNightTF.clear();
            propertyAmenitiesTF.clear();
        });
        btDelete.setOnAction(delete_house -> {
            final String[] selectedHouse = new String[1];
            hostPropertiesTableView.getItems().forEach(item -> selectedHouse[0] = item.getProperty_Name());
            if (!propertyNameTF.getText().isBlank()){
                Delete_House_Using_Name(propertyNameTF.getText());
            }else {
                Delete_House_Using_Name(selectedHouse[0]);
            }
            hostPropertiesObservableList = DBConnection.Get_Property_By_Host();
            hostPropertiesTableView.setItems(hostPropertiesObservableList);
        });

        BorderPane hostHousesBorderPane = new BorderPane();
        hostHousesBorderPane.setCenter(hostHousesVBox);
        hostHousesBorderPane.setBottom(hostHousesGrid);
        return hostHousesBorderPane;
    }

    public BorderPane HostReservationsBorderPane(){
        TableColumn<Reservation, Integer> reservationIDCol = new TableColumn<>("Reservation ID");
        reservationIDCol.setCellValueFactory(new PropertyValueFactory<>("reservation_ID"));

        TableColumn<Reservation, Integer> propertyIDCol = new TableColumn<>("Property ID");
        propertyIDCol.setCellValueFactory(new PropertyValueFactory<>("property_ID"));

        TableColumn<Reservation, String> propertyNameCol = new TableColumn<>("Property Name");
        propertyNameCol.setCellValueFactory(new PropertyValueFactory<>("property_Name"));

        TableColumn<Reservation, String> propertyHostCol = new TableColumn<>("Host");
        propertyHostCol.setCellValueFactory(new PropertyValueFactory<>("property_Host"));

        TableColumn<Reservation, Boolean> isPropertyAvailableCol = new TableColumn<>("Is Available ?");
        isPropertyAvailableCol.setCellValueFactory(new PropertyValueFactory<>("is_Property_Available"));

        TableColumn<Reservation, Date> startReservationCol = new TableColumn<>("Start Reservation Date");
        startReservationCol.setCellValueFactory(new PropertyValueFactory<>("property_Start_Reservation"));

        TableColumn<Reservation, Date> endReservationCol = new TableColumn<>("End Reservation Date");
        endReservationCol.setCellValueFactory(new PropertyValueFactory<>("property_End_Reservation"));

        hostReservationsObservableList = DBConnection.Get_Reservations_By_Host();
        hostReservationsTableView.setItems(hostReservationsObservableList);
        hostReservationsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        hostReservationsTableView.getColumns().addAll(reservationIDCol,propertyIDCol,propertyNameCol,propertyHostCol,isPropertyAvailableCol,startReservationCol,endReservationCol);

        VBox hostReservationsVBox = new VBox(10);
        hostReservationsVBox.setPadding(new Insets(100,5,5,5));
        hostReservationsVBox.getChildren().add(hostReservationsTableView);

        BorderPane hostReservationsBorderPane = new BorderPane();
        hostReservationsBorderPane.setCenter(hostReservationsVBox);
        return hostReservationsBorderPane;
    }

    public BorderPane HostRequestsBorderPane(){
        TableColumn<Request,String> propertyHostCol = new TableColumn<>("Host");
        propertyHostCol.setCellValueFactory(new PropertyValueFactory<>("propertyHost"));

        TableColumn<Request,String> propertyNameCol = new TableColumn<>("Property Name");
        propertyNameCol.setCellValueFactory(new PropertyValueFactory<>("propertyName"));

        TableColumn<Request,String> guestNameCol = new TableColumn<>("Guest Name");
        guestNameCol.setCellValueFactory(new PropertyValueFactory<>("guestName"));

        TableColumn<Request,Date> startReservationCol = new TableColumn<>("Start Reservation Date");
        startReservationCol.setCellValueFactory(new PropertyValueFactory<>("property_Start_Reservation"));

        TableColumn<Request,Date> endReservationCol = new TableColumn<>("End Reservation Date");
        endReservationCol.setCellValueFactory(new PropertyValueFactory<>("property_End_Reservation"));

        hostRequestsObservableList = DBConnection.Get_Requests_By_Host();
        hostRequestsTableView.setItems(hostRequestsObservableList);
        hostRequestsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        hostRequestsTableView.getColumns().addAll(propertyHostCol,propertyNameCol,guestNameCol,startReservationCol,endReservationCol);

        hostRequestsTableView.setRowFactory(getInfo -> {
            TableRow<Request> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                Request requestData = row.getItem();
                selectedProperty = requestData.getPropertyName();
                guestName = requestData.getGuestName();
                startDate = requestData.getProperty_Start_Reservation();
                endDate = requestData.getProperty_End_Reservation();
            });
            return row;
        });

        HBox buttonsHBox = new HBox(10);
        buttonsHBox.setPadding(new Insets(20,20,20,20));
        Button btApprove = new Button("Approve");
        btApprove.setId("approve-button");
        Button btReject = new Button("Reject");
        btReject.setId("reject-button");
        buttonsHBox.getChildren().addAll(btApprove, btReject);
        buttonsHBox.setAlignment(Pos.CENTER);

        btApprove.setOnAction(approveRequest -> Approve_Request());
        btReject.setOnAction(rejectRequest -> Reject_Request());

        VBox hostRequestsVBox = new VBox(10);
        hostRequestsVBox.setPadding(new Insets(100,5,5,5));
        hostRequestsVBox.getChildren().add(hostRequestsTableView);

        BorderPane hostRequestsBorderPane = new BorderPane();
        hostRequestsBorderPane.setCenter(hostRequestsVBox);
        hostRequestsBorderPane.setBottom(buttonsHBox);
        return hostRequestsBorderPane;
    }

    public BorderPane HostMessagesBorderPane(){
        TableColumn<Message, String> messageCol = new TableColumn<>("New Message From");
        messageCol.setCellValueFactory(new PropertyValueFactory<>("guestName"));

        hostMessagesObservableList = DBConnection.Get_Messages_By_Host();
        hostMessagesTableView.setItems(hostMessagesObservableList);

        hostMessagesTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        messageCol.minWidthProperty().bind(hostMessagesTableView.widthProperty().multiply(1));
        hostMessagesTableView.getColumns().add(messageCol);

        hostMessagesTableView.setRowFactory(showMessage -> {
            TableRow<Message> row = new TableRow<>();
            row.setOnMouseClicked(getMessage -> {
                if (getMessage.getButton() == MouseButton.PRIMARY && getMessage.getClickCount() == 2 && !row.isEmpty()) {
                    Message messageData = row.getItem();
                    selectedMessage = messageData.getGuestName();
                    window.setScene(ChatScene());
                }
            });
            return row;
        });

        VBox hostMessagesVBox = new VBox(10);
        hostMessagesVBox.setPadding(new Insets(100,10,5,10));
        hostMessagesVBox.getChildren().add(hostMessagesTableView);

        BorderPane hostMessagesBorderPane = new BorderPane();
        hostMessagesBorderPane.setCenter(hostMessagesVBox);
        return hostMessagesBorderPane;
    }

    public Scene ChatScene(){
        Get_Message_Data();
        Text chatText = new Text("New Chat : ");
        chatText.setStyle("-fx-text-alignment: center; -fx-fill: limegreen; -fx-font-family: 'Arial Black'; -fx-font-size: 24px");

        Label subjectLabel = new Label("Subject : ");
        Label messageLabel = new Label("Message : ");
        Label replyLabel = new Label("Reply : ");
        subjectTF.setPromptText("Subject");
        subjectTF.setEditable(false);
        messageArea.setPromptText("Message");
        messageArea.setPrefHeight(100);
        messageArea.setEditable(false);
        replyArea.setPromptText("Reply");
        replyArea.setPrefHeight(100);
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

        btSend.setOnAction(sendMessage -> Send_Reply());
        btBack.setOnAction(goBack -> {
            window.hide();
            HostStage();
        });

        chatGrid.setBackground(new Background(new BackgroundImage(bgImg,BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,BackgroundSize.DEFAULT)));
        Scene chatScene = new Scene(chatGrid, screenBounds.getMaxX(), screenBounds.getMaxY());
        chatScene.getStylesheets().add(new File("Css/chatScene.css").toURI().toString());
        return chatScene;
    }
}
