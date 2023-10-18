package controller;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class SignInController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    public JFXTextField OTPtxt;

    public AnchorPane SignInPane;

    public JFXTextField adminEmailtxt;

    public JFXPasswordField adminPwdtxt;

    public JFXPasswordField newConfirmPwdtxt;

    public JFXTextField newEmailtxt;

    public JFXPasswordField newPwdtxt;

    public JFXTextField newUsernametxt;

    public JFXCheckBox showPwdCheckBox1;

    public JFXCheckBox showPwdCheckBox2;

    public JFXComboBox<String> userComboBox;

    public JFXTextField showAdminpwd = new JFXTextField();

    public JFXTextField showNewPwd = new JFXTextField();

    public JFXTextField showNewConfirmedPwd = new JFXTextField();

    @FXML
    void BackToHome(ActionEvent event) {

    }

    @FXML
    void CheckAdmin(ActionEvent event) {

    }

    @FXML
    void SendOTP(ActionEvent event) {

    }

    @FXML
    void SignIn(ActionEvent event) {

    }

    @FXML
    void showAdminPwd(ActionEvent event) {
        if (showPwdCheckBox1.isSelected()) {
            showAdminpwd.setVisible(true);
            adminPwdtxt.setVisible(false);
        } else {
            showAdminpwd.setVisible(false);
            adminPwdtxt.setVisible(true);
        }
    }

    @FXML
    void showNewPwds(ActionEvent event) {
        if (showPwdCheckBox2.isSelected()) {
            showNewPwd.setVisible(true);
            showNewConfirmedPwd.setVisible(true);
            newPwdtxt.setVisible(false);
            newConfirmPwdtxt.setVisible(false);
        } else {
            newPwdtxt.setVisible(true);
            newConfirmPwdtxt.setVisible(true);
            showNewPwd.setVisible(false);
            showNewConfirmedPwd.setVisible(false);
        }
    }

    @FXML
    void initialize() {
        SignInPane.getChildren().add(showAdminpwd);
        showAdminpwd.managedProperty().bind(showAdminpwd.visibleProperty());
        adminPwdtxt.managedProperty().bind(adminPwdtxt.visibleProperty());
        showAdminpwd.textProperty().bindBidirectional(adminPwdtxt.textProperty());
        showAdminpwd.setLayoutX(adminPwdtxt.getLayoutX());
        showAdminpwd.setLayoutY(adminPwdtxt.getLayoutY());
        showAdminpwd.setPrefWidth(adminPwdtxt.getPrefWidth());
        showAdminpwd.setPrefHeight(adminPwdtxt.getPrefHeight());
        showAdminpwd.setVisible(false);
        /*----------------------------------------------------------------------------*/
        SignInPane.getChildren().add(showNewPwd);
        showNewPwd.managedProperty().bind(showNewPwd.visibleProperty());
        newPwdtxt.managedProperty().bind(newPwdtxt.visibleProperty());
        showNewPwd.textProperty().bindBidirectional(newPwdtxt.textProperty());
        showNewPwd.setLayoutX(newPwdtxt.getLayoutX());
        showNewPwd.setLayoutY(newPwdtxt.getLayoutY());
        showNewPwd.setPrefWidth(newPwdtxt.getPrefWidth());
        showNewPwd.setPrefHeight(newPwdtxt.getPrefHeight());
        showNewPwd.setVisible(false);
        /*----------------------------------------------------------------------------*/
        SignInPane.getChildren().add(showNewConfirmedPwd);
        showNewConfirmedPwd.managedProperty().bind(showNewConfirmedPwd.visibleProperty());
        newConfirmPwdtxt.managedProperty().bind(newConfirmPwdtxt.visibleProperty());
        showNewConfirmedPwd.textProperty().bindBidirectional(newConfirmPwdtxt.textProperty());
        showNewConfirmedPwd.setLayoutX(newConfirmPwdtxt.getLayoutX());
        showNewConfirmedPwd.setLayoutY(newConfirmPwdtxt.getLayoutY());
        showNewConfirmedPwd.setPrefWidth(newConfirmPwdtxt.getPrefWidth());
        showNewConfirmedPwd.setPrefHeight(newConfirmPwdtxt.getPrefHeight());
        showNewConfirmedPwd.setVisible(false);
        /*----------------------------------------------------------------------------*/
        userComboBox.getItems().add(new Label("Admin").getText());
        userComboBox.getItems().add(new Label("Default").getText());
        userComboBox.setPromptText("Select");

    }
}