package com.example.gamef;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javafx.scene.image.ImageView;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.scene.image.Image;


public class LoginController implements Initializable {

    @FXML
    private Button CancelButton;

    @FXML
    private Label InvalidFieldLOGIN;

    @FXML
    private Button LoginButton;

    @FXML
    private PasswordField PasswordFieldLOGIN;

    @FXML
    private TextField UsernameFieldLOGIN;

    @FXML
    private ImageView lockLOGin;

    @FXML
    private Button registerButton;

    @FXML
    private ImageView spendralogo;


    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) CancelButton.getScene().getWindow();
        stage.close();
    }
    @FXML
    void goToRegisterPage(ActionEvent event) {
        try {
            // Load Register.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Register.fxml"));
            Parent root = loader.load();

            // Get current stage
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

            // Set new scene
            stage.setScene(new Scene(root));
            stage.setTitle("Register Page");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML

    void loginButtonOnAction(ActionEvent event) {
        if (!UsernameFieldLOGIN.getText().isBlank() || !PasswordFieldLOGIN.getText().isBlank()) {
            validateLogin();
        } else {
            InvalidFieldLOGIN.setText("⚠️ Please enter username & password");
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // For spendralogo
        File spendoralogoFile = new File("image/Spendora_Logo.png");
        Image spendralogoImage = new Image(spendoralogoFile.toURI().toString());
        spendralogo.setImage(spendralogoImage);

        // For lockLOGin
        File locklogoFile = new File("image/lock.png");
        Image locklogoImage = new Image(locklogoFile.toURI().toString());
        lockLOGin.setImage(locklogoImage);
    }


    public void validateLogin() {
        DatabaseConnection connectNow = new DatabaseConnection();

        Connection connectDB = connectNow.getConnection();

        String sql = "SELECT * FROM user_account WHERE username = ? AND password = ?";

        try {
            PreparedStatement preparedStatement = connectDB.prepareStatement(sql);
            preparedStatement.setString(1, UsernameFieldLOGIN.getText());
            preparedStatement.setString(2, PasswordFieldLOGIN.getText());

            ResultSet queryResult = preparedStatement.executeQuery();

            if (queryResult.next()) {
                // If at least one row is returned, login is successful
                InvalidFieldLOGIN.setText("🎉 Congratulations! Login successful.");
                System.out.println("Username: " + queryResult.getString("username"));
            } else {
                // No matching row → invalid login
                InvalidFieldLOGIN.setText("❌ Invalid login. Please try again.");
            }

            queryResult.close();
            preparedStatement.close();
            connectDB.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
