package controller;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;

public class LogInController {

    public StackPane PaneContainer;

    public JFXCheckBox showPwdCheckBox;

    public JFXTextField txtUserName;

    public JFXPasswordField txtPassword;

    public JFXTextField txtVisiblePassword = new JFXTextField();

    @FXML
    public void LogIn(ActionEvent ignored) {
        new Alert(Alert.AlertType.INFORMATION, "Successfully Logged In").show();
    }

    @FXML
    public void SignIn(ActionEvent ignored) {
        new Alert(Alert.AlertType.INFORMATION, "Successfully Signed In").show();
    }

    @FXML
    public void forgotPwd(ActionEvent ignored) {
        new Alert(Alert.AlertType.INFORMATION, "Link is working").show();
    }

    @FXML
    public void showPwd(ActionEvent ignored) {
        if (showPwdCheckBox.isSelected()) {
            txtVisiblePassword.setVisible(true);
            txtPassword.setVisible(false);
        } else {
            txtPassword.setVisible(true);
            txtVisiblePassword.setVisible(false);
        }
    }

    @FXML
    public void initialize() {
        PaneContainer.setAlignment(Pos.CENTER);
        PaneContainer.getChildren().add(txtVisiblePassword);
        txtVisiblePassword.managedProperty().bind(txtVisiblePassword.visibleProperty());
        txtPassword.managedProperty().bind(txtPassword.visibleProperty());
        txtVisiblePassword.textProperty().bindBidirectional(txtPassword.textProperty());
        txtVisiblePassword.translateXProperty().bind(txtPassword.translateXProperty());
        txtVisiblePassword.translateYProperty().bind(txtPassword.translateYProperty());
        txtVisiblePassword.setVisible(false);
        txtVisiblePassword.setMaxWidth(400);
        txtVisiblePassword.setMaxHeight(20);
    }
}
