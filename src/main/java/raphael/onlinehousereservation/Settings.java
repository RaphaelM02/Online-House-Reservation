package raphael.onlinehousereservation;

import com.google.protobuf.InvalidProtocolBufferException;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Settings{
    TextField firstNameTF = new TextField();
    TextField lastNameTF = new TextField();
    TextField emailTF = new TextField();
    TextField phoneTF = new TextField();
    TextField oldPasswordTF = new TextField();
    TextField newPasswordTF = new TextField();
    TextField confirmPasswordTF = new TextField();
    Image userImage = new Image(new File("Images/user-alt-1-svgrepo-com-test.png").toURI().toString());
    ImageView userImageIV = new ImageView(userImage);
    public String userImagePath;

    //Methods :
    public void GetUserInfo(){
        Connection connection = DBConnection.ConnectDBUser();
        try {
            PreparedStatement getUserInfoStmt = connection.prepareStatement("SELECT firstName, lastName, email, phoneNumber FROM testuser, temp_user WHERE _id = user_token");
            ResultSet getUserInfoRS = getUserInfoStmt.executeQuery();
            while (getUserInfoRS.next()){
                firstNameTF.setText(getUserInfoRS.getString("firstName"));
                lastNameTF.setText(getUserInfoRS.getString("lastName"));
                emailTF.setText(getUserInfoRS.getString("email"));
                phoneTF.setText(String.valueOf(getUserInfoRS.getInt("phoneNumber")));
            }
        }catch (Exception GetUserInfoException){
            GetUserInfoException.getMessage();
            SaveExceptions.saveExceptionToXml(GetUserInfoException);
        }
    }

    public BorderPane SettingsBorderPane(){
        GetUserInfo();
        Label firstNameLabel = new Label("First Name");
        firstNameTF.setPromptText("First Name");
        firstNameTF.setEditable(false);
        Label lastNameLabel = new Label("Last Name");
        lastNameTF.setPromptText("Last Name");
        lastNameTF.setEditable(false);
        Label emailLabel = new Label("Email");
        emailTF.setPromptText("Email");
        emailTF.setEditable(false);
        Label phoneLabel = new Label("Phone");
        phoneTF.setPromptText("Phone");
        phoneTF.setEditable(false);
        Label oldPasswordLabel = new Label("Old Password");
        oldPasswordTF.setPromptText("Old Password");
        Label newPasswordLabel = new Label("New Password");
        newPasswordTF.setPromptText("New Password");
        Label confirmPasswordLabel = new Label("Confirm Password");
        confirmPasswordTF.setPromptText("Confirm Password");
        Button btnChangePassword = new Button("Change Password");

        BorderPane settingsBorderPane =  new BorderPane();
        GridPane settingsGrid = new GridPane(10,10);
        settingsGrid.setAlignment(Pos.CENTER);
        settingsGrid.setPadding(new Insets(10,10,10,10));

        settingsGrid.add(userImageIV,0,0);
        settingsGrid.add(firstNameLabel,0,1);
        settingsGrid.add(firstNameTF,1,1);
        settingsGrid.add(lastNameLabel,0,2);
        settingsGrid.add(lastNameTF,1,2);
        settingsGrid.add(emailLabel,0,3);
        settingsGrid.add(emailTF,1,3);
        settingsGrid.add(phoneLabel,0,4);
        settingsGrid.add(phoneTF,1,4);
        settingsGrid.add(btnChangePassword,1,5);
        GridPane.setHalignment(btnChangePassword,HPos.CENTER);

        settingsBorderPane.setCenter(settingsGrid);
        return settingsBorderPane;
    }

//    public void SelectImage(){
//        JFileChooser imageSelect = new JFileChooser();
//        imageSelect.setDialogTitle("Select Image");
//
//        FileNameExtensionFilter imageFilter = new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif", "bmp", "tif", "tiff");
//        imageSelect.setFileFilter(imageFilter);
//
//        int result = imageSelect.showOpenDialog(null);
//        if (result == JFileChooser.APPROVE_OPTION) {
//            File chosenImage = imageSelect.getSelectedFile();
//            userImage = chosenImage.getAbsolutePath();
//            ProcessImage(userImage);
//        }else {
//            JOptionPane.showMessageDialog(null, "No image selected");
//        }
//    }
//
//    public static void ProcessImage(String imagePath){
//        try {
//            File file = new File(imagePath);
//            BufferedImage image = ImageIO.read(file);
//        }
//    }
}
