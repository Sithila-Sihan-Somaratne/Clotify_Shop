
package controller;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import dto.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.hibernate.Session;
import util.HibernateUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class LogInController {

    public AnchorPane PaneContainer;

    public JFXCheckBox showPwdCheckBox;

    public JFXTextField txtUserName;

    public JFXPasswordField txtPassword;

    public AnchorPane dashboardPane;

    public JFXTextField txtVisiblePassword = new JFXTextField();

    @FXML
    public void LogIn(ActionEvent ignored) {
        String pass = txtPassword.getText();
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}";
        if (!pass.matches(pattern)){
            new Alert(Alert.AlertType.WARNING,"Password must contain a capital letter, numbers and a symbol. And it should be between 8 and 20 characters.").show();
        }else{
            try{
                if (!Objects.equals(txtUserName.getText(), "") && (!Objects.equals(txtVisiblePassword.getText(), ""))){
                    String name = txtUserName.getText();
                    Session session = HibernateUtil.getSession();
                    User user = session.find(User.class, name);
                    String encryptPwd = user.getPassword();
                    session.close();
                    String pwd = txtVisiblePassword.getText();
                    String user_type = user.getType();
                    boolean samePwd = checkPwd(encryptPwd,pwd);
                    if(Objects.equals(user.getName(), name) && samePwd){
                        Stage stg = (Stage) dashboardPane.getScene().getWindow();
                        stg.close();
                        Stage stage = new Stage();
                        if (Objects.equals(user_type, "Admin")){
                            stage.setTitle("Home Window (Admin)");
                            try {
                                stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/HomePageAdmin.fxml")))));
                            } catch (Exception ignoreExceptiton) {}

                        }else{
                            stage.setTitle("Home Window (Default)");
                            try {
                                stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/HomePageDefault.fxml")))));
                            } catch (Exception ignoreExceptiton) {}
                        }
                        stage.show();
                        stage.setResizable(false);
                    }else{
                        new Alert(Alert.AlertType.WARNING,"Password is unknown. Please try again!").show();
                    }
                }else{
                    new Alert(Alert.AlertType.WARNING,"Don't leave blank text fields please!").show();
                }
            }catch(Exception ex){
                new Alert(Alert.AlertType.WARNING,"Username is unknown. Please try again!").show();
            }
        }
    }

    private boolean checkPwd(String encryptPwd, String pwd) {
        String encryptedpassword = null;
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(pwd.getBytes());
            byte[] bytes = m.digest();
            StringBuilder s = new StringBuilder();
            for (byte aByte : bytes) {
                s.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            encryptedpassword = s.toString();
        } catch (NoSuchAlgorithmException e) {
            new Alert(Alert.AlertType.WARNING,"Oops! Something went wrong!").show();
        }
        System.out.println(Objects.equals(encryptPwd, encryptedpassword));
        return Objects.equals(encryptPwd, encryptedpassword);
    }

    @FXML
    public void SignIn(ActionEvent ignored) {
        Stage stage = (Stage) PaneContainer.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/SignInWindow.fxml")))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        stage.setTitle("Sign In Window.");
        stage.show();
        stage.setResizable(false);
    }

    @FXML
    public void forgotPwd(ActionEvent ignored) {
     if(!Objects.equals(txtUserName.getText(), "")){
        Stage stage = new Stage();
        stage.setTitle("Forgot Password Window");
        try {
            stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/forgotPwdWindow.fxml")))));
            stage.setResizable(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        stage.show();
        ForgotPwdWindow forgotPwdWindow = new ForgotPwdWindow();
        forgotPwdWindow.storeUser(txtUserName.getText());
     }else{
         new Alert(Alert.AlertType.WARNING,"You must enter the username before you click the link!").show();
     }
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
        PaneContainer.getChildren().add(txtVisiblePassword);
        txtVisiblePassword.managedProperty().bind(txtVisiblePassword.visibleProperty());
        txtPassword.managedProperty().bind(txtPassword.visibleProperty());
        txtVisiblePassword.textProperty().bindBidirectional(txtPassword.textProperty());
        txtVisiblePassword.setLayoutX(txtPassword.getLayoutX());
        txtVisiblePassword.setLayoutY(txtPassword.getLayoutY());
        txtVisiblePassword.setPrefWidth(txtPassword.getPrefWidth());
        txtVisiblePassword.setPrefHeight(txtPassword.getPrefHeight());
        txtVisiblePassword.setVisible(false);
    }
}
