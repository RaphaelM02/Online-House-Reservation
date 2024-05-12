package raphael.onlinehousereservation;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends Application {
    Stage window;
    HostPage hostPage = new HostPage();
    GuestPage guestPage = new GuestPage();
    Text errorText = new Text();
    Text logInErrorText = new Text();
    Scene signUpScene1, logInScene1;
    Connection connection = DBConnection.ConnectDBUser();
    TextField signUpEmailTF = new TextField();
    TextField firstNameTF = new TextField();
    TextField lastNameTF = new TextField();
    TextField phoneNumberTF = new TextField();
    PasswordField signUpPassPF = new PasswordField();
    PasswordField retypePassPF = new PasswordField();
    TextField emailTF = new TextField();
    PasswordField passwordTF = new PasswordField();
    String userType;

    void SignUpUser(String email, String firstName, String lastName, String phoneNumber, String password, String retypePassword){
        String addUser = "INSERT INTO testUser(email, firstName, lastName, phoneNumber, userPass, userPass2, userType) values(?,?,?,?,?,?,'user');";
        try {
            assert connection != null;
            if(!email.isEmpty() &&  !firstName.isEmpty() && !lastName.isEmpty() && !phoneNumber.isEmpty() && !password.isEmpty() && !retypePassword.isEmpty()){
                if (retypePassword.equals(password)){
                    PreparedStatement preparedStatement = connection.prepareStatement(addUser);
                    preparedStatement.setString(1,signUpEmailTF.getText());
                    preparedStatement.setString(2,firstNameTF.getText());
                    preparedStatement.setString(3,lastNameTF.getText());
                    preparedStatement.setInt(4, Integer.parseInt(phoneNumberTF.getText()));
                    preparedStatement.setString(5,signUpPassPF.getText());
                    preparedStatement.setString(6,retypePassPF.getText());
                    preparedStatement.execute();
                    JOptionPane.showMessageDialog(null,"Welcome "+firstNameTF.getText()+".");
                    PreparedStatement user_temp = connection.prepareStatement("INSERT INTO temp_user(user_email, login_date) values(?,NOW())");
                    user_temp.setString(1,signUpEmailTF.getText());
                    user_temp.execute();
                    PreparedStatement user_temp_update = connection.prepareStatement("UPDATE temp_user, testuser SET user_name = testuser.firstName, user_token = testuser._id where user_email = testuser.email ");
                    user_temp_update.execute();
                    window.hide();
                    guestPage.GuestStage();
                }else {
                    errorText.setText("Passwords do not match");
                }
            }else {
                errorText.setText("All fields are required");
            }
        }
        catch (Exception MissingInfo){
            MissingInfo.getMessage();
            SaveExceptions.saveExceptionToXml(MissingInfo);
        }
    }
    void SignUpHost(String email, String firstName, String lastName, String phoneNumber, String password, String retypePassword){
        String addHost = "INSERT INTO testUser(email, firstName, lastName, phoneNumber, userPass, userPass2, userType) values(?,?,?,?,?,?,'owner');";
        try {
            assert connection != null;
            if(!email.isEmpty() &&  !firstName.isEmpty() && !lastName.isEmpty() && !phoneNumber.isEmpty() && !password.isEmpty() && !retypePassword.isEmpty()){
                if (retypePassword.equals(password)){
                    PreparedStatement preparedStatement = connection.prepareStatement(addHost);
                    preparedStatement.setString(1,signUpEmailTF.getText());
                    preparedStatement.setString(2,firstNameTF.getText());
                    preparedStatement.setString(3,lastNameTF.getText());
                    preparedStatement.setInt(4, Integer.parseInt(phoneNumberTF.getText()));
                    preparedStatement.setString(5,signUpPassPF.getText());
                    preparedStatement.setString(6,retypePassPF.getText());
                    preparedStatement.execute();
                    JOptionPane.showMessageDialog(null,"Welcome "+firstNameTF.getText()+".");
                    PreparedStatement user_temp = connection.prepareStatement("INSERT INTO temp_user(user_email, login_date) values(?,NOW())");
                    user_temp.setString(1,email);
                    user_temp.execute();
                    PreparedStatement user_temp_update = connection.prepareStatement("UPDATE temp_user, testuser SET user_name = testuser.firstName, user_token = testuser._id where user_email = testuser.email ");
                    user_temp_update.execute();
                    window.hide();
                    hostPage.HostStage();
                }else {
                    errorText.setText("Passwords do not match");
                }
            }else {
                errorText.setText("All fields are required");
            }
        }
        catch (Exception MissingInfo){
            MissingInfo.getMessage();
            SaveExceptions.saveExceptionToXml(MissingInfo);
        }
    }
    boolean Log_In(String email, String password){
        try {
            int count = 0;
            PreparedStatement logInStatement = connection.prepareStatement("SELECT count(*) as count from testUser where email = '" + email + "' and userPass = '" + password + "'");
            ResultSet logInRS = logInStatement.executeQuery();
            while (logInRS.next()){
                count = logInRS.getInt("count");
            }
            return count != 0;
        }catch (Exception logInFailed){
            JOptionPane.showMessageDialog(null,logInFailed.getMessage());
            SaveExceptions.saveExceptionToXml(logInFailed);
            return false;
        }
    }
    void Get_User_Type(String email, String password){
        try {
            PreparedStatement getUserTypeStatement = connection.prepareStatement("SELECT userType FROM testuser where email = ? AND userPass = ?");
            getUserTypeStatement.setString(1, email);
            getUserTypeStatement.setString(2, password);
            ResultSet getUserTypeRS = getUserTypeStatement.executeQuery();
            while (getUserTypeRS.next()){
                userType = getUserTypeRS.getString("userType");
            }
            if (userType.equals("owner")){
                window.hide();
                hostPage.HostStage();
            } else if (userType.equals("user")) {
                window.hide();
                guestPage.GuestStage();
            }
        }catch (Exception getUserTypeException){
            getUserTypeException.getMessage();
            SaveExceptions.saveExceptionToXml(getUserTypeException);
        }
    }
    void Add_To_Temp(){
        if (Log_In(emailTF.getText(), passwordTF.getText())){
            try {
                PreparedStatement loggedInUserStatement = connection.prepareStatement("INSERT INTO temp_user(user_email, login_date) values(?,NOW())");
                loggedInUserStatement.setString(1,emailTF.getText());
                loggedInUserStatement.execute();
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE temp_user, testuser SET user_name = testuser.firstName, user_token = testuser._id where user_email = testuser.email ");
                preparedStatement.execute();
            }catch (Exception Add_To_Temp_Exception){
                Add_To_Temp_Exception.getMessage();
                SaveExceptions.saveExceptionToXml(Add_To_Temp_Exception);
            }
        }
    }

    @Override
    public void start(Stage signUpStage){
        window = signUpStage;
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        System.out.println(screenBounds);
        final Pattern ValidEmail = Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$",Pattern.CASE_INSENSITIVE);
        final Pattern ValidName = Pattern.compile("^[A-Z|a-z][a-zA-z]{2,29}$");
        final Pattern ValidPhoneNumber = Pattern.compile("(03|70|71|76|78|79|81)\\d{6}");
        final Pattern ValidPassword = Pattern.compile("[0-9a-zA-Z]{8,}", Pattern.CASE_INSENSITIVE);

        GridPane signUpGridPane = new GridPane(30,30);
        signUpGridPane.setPadding(new Insets(10,10,10,10));
        signUpGridPane.setAlignment(Pos.CENTER);
        HBox userTypeHbox = new HBox(220);

        errorText.setId("error-text");
        Label signUpEmail = new Label("Email :");
        Label firstName = new Label("First Name :");
        Label lastName = new Label("Last Name :");
        Label phoneNumber = new Label("Phone Number :");
        Label signUpPass = new Label("Password :");
        Label retypePass = new Label("Repeat Password :");
        RadioButton hostRB = new RadioButton("Host");
        RadioButton userRB = new RadioButton("User");
        ToggleGroup typeGroup = new ToggleGroup();

        hostRB.setToggleGroup(typeGroup);
        userRB.setToggleGroup(typeGroup);
        userTypeHbox.getChildren().addAll(hostRB, userRB);
        Button btSignUp = new Button("Sign Up");
        btSignUp.setId("btSignUp");
        Button btLogin = new Button("Already have an account? Log In here");
        btLogin.setId("btLogIn");

        signUpEmailTF.setPromptText("Enter your email");
        firstNameTF.setPromptText("Enter your first name");
        lastNameTF.setPromptText("Enter your last name");
        phoneNumberTF.setPromptText("Enter your phone number");
        signUpPassPF.setPromptText("Enter your password");
        retypePassPF.setPromptText("Repeat your password");

        signUpGridPane.add(signUpEmail,0,1);
        signUpGridPane.add(signUpEmailTF,1,1);
        signUpGridPane.add(firstName,0,2);
        signUpGridPane.add(firstNameTF,1,2);
        signUpGridPane.add(lastName,0,3);
        signUpGridPane.add(lastNameTF,1,3);
        signUpGridPane.add(phoneNumber,0,4);
        signUpGridPane.add(phoneNumberTF,1,4);
        signUpGridPane.add(signUpPass,0,5);
        signUpGridPane.add(signUpPassPF,1,5);
        signUpGridPane.add(retypePass,0,6);
        signUpGridPane.add(retypePassPF,1,6);
        signUpGridPane.add(userTypeHbox,1,7);
        signUpGridPane.add(btSignUp,1,8);
        signUpGridPane.add(btLogin,1,9);
        signUpGridPane.add(errorText,1,10);
        GridPane.setHalignment(userTypeHbox,HPos.CENTER);
        GridPane.setHalignment(btSignUp,HPos.CENTER);
        GridPane.setHalignment(btLogin,HPos.CENTER);
        GridPane.setHalignment(errorText, HPos.CENTER);

        signUpEmailTF.textProperty().addListener((observableValue, oldValue, newValue) -> {
            Matcher emailMatcher = ValidEmail.matcher(newValue);
            if (!emailMatcher.matches()){
                signUpEmailTF.setStyle("-fx-text-fill : white");
            }
            else {
                signUpEmailTF.setStyle("-fx-text-fill : green");
            }
        });
        firstNameTF.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            Matcher nameMatcher = ValidName.matcher(newValue);
            if (!nameMatcher.matches()){
                firstNameTF.setStyle("-fx-text-fill : white");
            }
            else {
                firstNameTF.setStyle("-fx-text-fill : green");
            }
        }));
        lastNameTF.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            Matcher nameMatcher = ValidName.matcher(newValue);
            if (!nameMatcher.matches()){
                lastNameTF.setStyle("-fx-text-fill : white");
            }
            else {
                lastNameTF.setStyle("-fx-text-fill : green");
            }
        }));
        phoneNumberTF.textProperty().addListener(((observable, oldValue, newValue) -> {
            Matcher phoneMatcher = ValidPhoneNumber.matcher(newValue);
            if (!phoneMatcher.matches()){
                phoneNumberTF.setStyle("-fx-text-fill: white");
            }
            else {
                phoneNumberTF.setStyle("-fx-text-fill: green");
            }
        }));
        signUpPassPF.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            Matcher passwordMatcher = ValidPassword.matcher(newValue);
            if(!passwordMatcher.matches()){
                signUpPassPF.setStyle("-fx-text-fill: white");
            }
            else {
                signUpPassPF.setStyle("-fx-text-fill: green");
            }
        }));
        retypePassPF.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if(!retypePassPF.getText().equals(signUpPassPF.getText())){
                retypePassPF.setStyle("-fx-text-fill: white");
            }else {
                retypePassPF.setStyle("-fx-text-fill: green");
            }
        });


        btSignUp.setOnAction(signUpAction->{
            if(!userRB.isSelected() && !hostRB.isSelected()){
                errorText.setText("You must select a user type.");
            }
            else if (userRB.isSelected()){
                SignUpUser(signUpEmailTF.getText(),firstNameTF.getText(),lastNameTF.getText(),phoneNumberTF.getText(),signUpPassPF.getText(),retypePassPF.getText());
            } else if (hostRB.isSelected()) {
                SignUpHost(signUpEmailTF.getText(),firstNameTF.getText(),lastNameTF.getText(),phoneNumberTF.getText(),signUpPassPF.getText(),retypePassPF.getText());
            }
        });
        btLogin.setOnAction(logInAction -> window.setScene(logInScene1));


        Image bgImg = new Image(new File("Images/background.png").toURI().toString());
        signUpGridPane.setBackground(new Background(new BackgroundImage(bgImg,BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,BackgroundSize.DEFAULT)));
        Scene signUpScene = new Scene(signUpGridPane,screenBounds.getMaxX(), screenBounds.getMaxY());
        signUpScene1 = signUpScene;
        File signUpCss = new File("Css/signUpCss.css");
        signUpScene.getStylesheets().add(signUpCss.toURI().toString());
        window.setTitle("Sign Up");
        window.setScene(signUpScene);
        window.setMaximized(true);
        window.show();

        //Start of Log In
        GridPane logInGrid = new GridPane(10,10);
        logInGrid.setPadding(new Insets(10,10,10,10));
        logInGrid.setAlignment(Pos.CENTER);

        Label logInEmail = new Label("Email : ");
        Label logInPassword = new Label("Password : ");
        Button logInBt = new Button("Log In");
        Button signUpBt = new Button("New user? Sign Up here");

        logInBt.setId("logInBt");
        signUpBt.setId("signUpBt");
        emailTF.setPromptText("Enter your email");
        passwordTF.setPromptText("Enter your password");
        logInErrorText.setId("error-text");

        logInGrid.add(logInEmail,0,1);
        logInGrid.add(emailTF,1,1);
        logInGrid.add(logInPassword,0,2);
        logInGrid.add(passwordTF,1,2);
        logInGrid.add(logInBt,1,3);
        logInGrid.add(signUpBt,1,4);
        logInGrid.add(logInErrorText,1,5);
        GridPane.setHalignment(logInBt, HPos.CENTER);
        GridPane.setHalignment(signUpBt,HPos.CENTER);
        GridPane.setHalignment(logInErrorText, HPos.CENTER);

        logInGrid.setBackground(new Background(new BackgroundImage(bgImg,BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,BackgroundSize.DEFAULT)));
        Scene logInScene = new Scene(logInGrid,screenBounds.getMaxX(),screenBounds.getMaxY());
        logInScene1 = logInScene;
        File logInCss = new File("Css/logInCss.css");
        logInScene.getStylesheets().add(logInCss.toURI().toString());

        emailTF.textProperty().addListener((observableValue, oldValue, newValue) -> {
            Matcher emailMatcher = ValidEmail.matcher(newValue);
            if (!emailMatcher.matches()) {
                emailTF.setStyle("-fx-text-fill: white;");
            }
            else emailTF.setStyle("-fx-text-fill:  green");
        });
        passwordTF.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            Matcher passwordMatcher = ValidPassword.matcher(newValue);
            if(!passwordMatcher.matches()){
                passwordTF.setStyle("-fx-text-fill: white");
            }
            else {
                passwordTF.setStyle("-fx-text-fill: green");
            }
        }));

        logInBt.setOnAction(logInAction -> {
            if(Log_In(emailTF.getText(), passwordTF.getText())){
                Add_To_Temp();
                Get_User_Type(emailTF.getText(), passwordTF.getText());
            }else {
                logInErrorText.setText("Email and password do not match");
            }
        });
        signUpBt.setOnAction(signUpAction -> window.setScene(signUpScene));

    }
}
