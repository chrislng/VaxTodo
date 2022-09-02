package VaxTodo.Controllers;

import VaxTodo.Configs.Config;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerLogin_Test {
    // Import the application's controls
    @FXML
    private Label lblInvalidEmploye, lblInvalidBenevole;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField textFieldEmploye, passwdFieldEmploye, textFieldBenevole, passwdFieldBenevole;
    // , signUpEmailTextField, signUpRepeatPasswordPasswordField;

    // Creation of methods which are activated on events in the forms
    @FXML
    protected void onCancelButtonClick() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void btnLoginEmployeClick() {
        if (textFieldEmploye.getText().isBlank() || passwdFieldEmploye.getText().isBlank()) {
            lblInvalidEmploye.setText("Tous les champs doivent êtres remplis!");
            lblInvalidEmploye.setStyle(Config.strStyleErrorMessage);
            // lblInvalidBenevole.setText("");

            textFieldEmploye.setStyle(Config.strStyleSuccessBorder);
            passwdFieldEmploye.setStyle(Config.strStyleSuccessBorder);

            if (textFieldEmploye.getText().isBlank()) {
                textFieldEmploye.setStyle(Config.strStyleErrorBorder);
            } 
            if (passwdFieldEmploye.getText().isBlank()) {
                passwdFieldEmploye.setStyle(Config.strStyleErrorBorder);
            }
        } 
        else {
            lblInvalidEmploye.setText("Connexion Employé Réussi!");
            lblInvalidEmploye.setStyle(Config.strStyleSuccessMessage);
            textFieldEmploye.setStyle(Config.strStyleSuccessBorder);
            passwdFieldEmploye.setStyle(Config.strStyleSuccessBorder);
            // lblInvalidBenevole.setText("");
        }
    }

    @FXML
    protected void btnLoginBenevoleClick() {
        // || signUpEmailTextField.getText().isBlank() || signUpRepeatPasswordPasswordField.getText().isBlank()
        if (textFieldBenevole.getText().isBlank() || passwdFieldBenevole.getText().isBlank() ) {
            lblInvalidBenevole.setText("Tous les champs doivent êtres remplis!");
            lblInvalidBenevole.setStyle(Config.strStyleErrorMessage);
            // lblInvalidEmploye.setText("");

            textFieldBenevole.setStyle(Config.strStyleSuccessBorder);
            passwdFieldBenevole.setStyle(Config.strStyleSuccessBorder);

            if (textFieldBenevole.getText().isBlank()) {
                textFieldBenevole.setStyle(Config.strStyleErrorBorder);
            } 
            // else if (signUpEmailTextField.getText().isBlank()) {
            //     signUpEmailTextField.setStyle(errorStyle);
            // } 
            if (passwdFieldBenevole.getText().isBlank()) {
                passwdFieldBenevole.setStyle(Config.strStyleErrorBorder);
            } 
            // else if (signUpRepeatPasswordPasswordField.getText().isBlank()) {
            //     signUpRepeatPasswordPasswordField.setStyle(errorStyle);
            // }
        } 
        // if (signUpRepeatPasswordPasswordField.getText().equals(passwdFieldBenevole.getText()))
        else {
            lblInvalidBenevole.setText("Connexion Bénévole Réussi!");
            lblInvalidBenevole.setStyle(Config.strStyleSuccessMessage);
            textFieldBenevole.setStyle(Config.strStyleSuccessBorder);
            // signUpEmailTextField.setStyle(successStyle);
            passwdFieldBenevole.setStyle(Config.strStyleSuccessBorder);
            // signUpRepeatPasswordPasswordField.setStyle(successStyle);
            // lblInvalidEmploye.setText("");
        } 
        // else {
        //     lblInvalidBenevole.setText("The Passwords don't match!");
        //     lblInvalidBenevole.setStyle(errorMessage);
        //     passwdFieldBenevole.setStyle(errorStyle);
        //     // signUpRepeatPasswordPasswordField.setStyle(errorStyle);
        //     lblInvalidEmploye.setText("");
        // }
    }

    @FXML
    protected void btnForgotPassWDClick() {
        System.out.println("\n\n");
        System.out.println("Goto Menu Forgot Password");
    }
}