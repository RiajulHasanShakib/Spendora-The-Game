package com.example.gamef;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML
    private Button CloseButton;

    @FXML
    private Label CnfirmPassProblem;

    @FXML
    private TextField ConfirmPasswordField;

    @FXML
    private TextField FirstnameField;

    @FXML
    private TextField LastnameField;

    @FXML
    private Button RegisterButton;

    @FXML
    private TextField SetPasswordFIeld;

    @FXML
    private TextField UsernameField;

    @FXML
    private Label firstnameProblem;

    @FXML
    private Label lastnameproblem;

    @FXML
    private Button loginButtonREG;

    @FXML
    private Label passwordMissing;

    @FXML
    private ImageView registerLogo;

    @FXML
    private Label usernameProblem;

    @FXML
    void CloseButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) CloseButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set logo
        File spendoralogoFile = new File("image/register.png");
        Image spendralogoImage = new Image(spendoralogoFile.toURI().toString());
        registerLogo.setImage(spendralogoImage);

        // Add listeners to clear error messages when typing
        setupFieldListeners();
    }

    private void setupFieldListeners() {
        FirstnameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.trim().isEmpty()) {
                firstnameProblem.setText("");
            }
        });

        LastnameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.trim().isEmpty()) {
                lastnameproblem.setText("");
            }
        });

        UsernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.trim().isEmpty()) {
                usernameProblem.setText("");
            }
        });

        SetPasswordFIeld.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                passwordMissing.setText("");
            }
        });

        ConfirmPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                CnfirmPassProblem.setText("");
            }
        });
    }

    @FXML
    void goToLoginPage(ActionEvent event) {
        try {
            // Load Login.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();

            // Get current stage
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

            // Set new scene
            stage.setScene(new Scene(root));
            stage.setTitle("Login Page");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void registerUser(ActionEvent event) {
        // Clear previous error messages
        clearErrorMessages();

        String firstname = FirstnameField.getText().trim();
        String lastname = LastnameField.getText().trim();
        String username = UsernameField.getText().trim();
        String password = SetPasswordFIeld.getText();
        String confirmPassword = ConfirmPasswordField.getText();

        boolean hasErrors = false;

        // Validate empty fields
        if (firstname.isEmpty()) {
            firstnameProblem.setText("First name is required!");
            hasErrors = true;
        }

        if (lastname.isEmpty()) {
            lastnameproblem.setText("Last name is required!");
            hasErrors = true;
        }

        if (username.isEmpty()) {
            usernameProblem.setText("Username is required!");
            hasErrors = true;
        }

        if (password.isEmpty()) {
            passwordMissing.setText("Password is required!");
            hasErrors = true;
        }

        if (confirmPassword.isEmpty()) {
            CnfirmPassProblem.setText("Please confirm your password!");
            hasErrors = true;
        }

        if (hasErrors) {
            return;
        }

        // Validate password match
        if (!password.equals(confirmPassword)) {
            CnfirmPassProblem.setText("Passwords do not match!");
            return;
        }

        // Check if username already exists
        DatabaseConnection connectNow = new DatabaseConnection();

        try (Connection connectDB = connectNow.getConnection()) {
            // Check if username exists
            String checkQuery = "SELECT * FROM user_account WHERE username = ?";
            try (PreparedStatement checkStmt = connectDB.prepareStatement(checkQuery)) {
                checkStmt.setString(1, username);
                try (ResultSet resultSet = checkStmt.executeQuery()) {
                    if (resultSet.next()) {
                        usernameProblem.setText("Username already exists!");
                        return;
                    }
                }
            }

            // Insert new user
            String insertQuery = "INSERT INTO user_account (firstname, lastname, username, password) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connectDB.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, firstname);
                preparedStatement.setString(2, lastname);
                preparedStatement.setString(3, username);
                preparedStatement.setString(4, password);

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    clearFields();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Registration Successful");
                    alert.setHeaderText(null);
                    alert.setContentText("User registered successfully!");


                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            goToLoginPage(event);
                        }
                    });
                }

                connectDB.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            usernameProblem.setText("Database error occurred!");
        }
    }

    private void clearErrorMessages() {
        firstnameProblem.setText("");
        lastnameproblem.setText("");
        usernameProblem.setText("");
        passwordMissing.setText("");
        CnfirmPassProblem.setText("");
    }

    private void clearFields() {
        FirstnameField.clear();
        LastnameField.clear();
        UsernameField.clear();
        SetPasswordFIeld.clear();
        ConfirmPasswordField.clear();
    }
}
