package controller;

import com.jfoenix.controls.*;
import dto.User;
import email.Email;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Random;
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

    public ImageView imageCheck;

    public JFXButton sendOTPbtn;

    public JFXButton signInBtn;

    public JFXTextField showAdminpwd = new JFXTextField();

    public JFXTextField showNewPwd = new JFXTextField();

    public JFXTextField showNewConfirmedPwd = new JFXTextField();
    public int randomNumber = 0;

    @FXML
    void BackToHome(ActionEvent event) {
        Stage stage = (Stage) SignInPane.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/LogInWindow.fxml")))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        stage.setTitle("Log In Window.");
        stage.show();
        stage.setResizable(false);
    }

    @FXML
    void CheckAdmin(ActionEvent event) {
        try{
            String name = adminEmailtxt.getText();
            String pwd = adminPwdtxt.getText();
            Session session = HibernateUtil.getSession();
            User user = session.find(User.class, name);
            session.close();
            if (user!=null){
                if (Objects.equals(name, user.getName()) && checkPwd(user.getPassword(), pwd)){
                    Email.user = user;
                    if ((Objects.equals(user.getType(), "Admin"))){
                        URL url = getClass().getResource("/img/tick_icon.png");
                        Image image = new Image(Objects.requireNonNull(url).toExternalForm());
                        imageCheck.setImage(image);
                        sendOTPbtn.setDisable(false);
                    }else{
                        new Alert(Alert.AlertType.WARNING,"User isn't Admin. Please enter another user!").show();
                        URL url = getClass().getResource("/img/cross_icon.png");
                        Image image = new Image(Objects.requireNonNull(url).toExternalForm());
                        imageCheck.setImage(image);
                        sendOTPbtn.setDisable(true);
                    }
                }else{
                    new Alert(Alert.AlertType.WARNING,"Username or password is wrong. Please try again!").show();
                }
            }else{
                new Alert(Alert.AlertType.WARNING,"Enter the right name of user!").show();
            }
        }catch(Exception ignored){
            new Alert(Alert.AlertType.ERROR,"Oops! Something went wrong!");
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
        return Objects.equals(encryptPwd, encryptedpassword);
    }

    @FXML
    void SendOTP(ActionEvent event) {
        Random random = new Random();
        randomNumber = random.nextInt(900) + 100;
        new Email().sendOTPByMailForAddUser(randomNumber);
        OTPtxt.setEditable(true);
    }

    @FXML
    void SignIn(ActionEvent event) {
        String pass = newPwdtxt.getText();
        String confpass = newConfirmPwdtxt.getText();
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}";
        if (!Objects.equals(confpass, pass)) {
            new Alert(Alert.AlertType.WARNING, "Confirmation Password and New Password must have the same content.").show();
        }else if (!pass.matches(pattern) || !confpass.matches(pattern)) {
            new Alert(Alert.AlertType.WARNING, "Password must contain a capital letter, numbers and a symbol. And it should be between 8 and 20 characters.").show();
        }else{
            String name = newUsernametxt.getText();
            String email = newEmailtxt.getText();
            String encryptedPwd = encryptPwd(pass);
            String type = userComboBox.getValue();
            User user = new User(name, email, encryptedPwd, type);
            Session session = HibernateUtil.getSession();
            Transaction transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            session.close();
            new Alert(Alert.AlertType.INFORMATION,"User has been added successfully!").show();
        }
    }

    private String encryptPwd(String newPassword) {
        String encryptedpassword = null;
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(newPassword.getBytes());
            byte[] bytes = m.digest();
            StringBuilder s = new StringBuilder();
            for (byte aByte : bytes) {
                s.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            encryptedpassword = s.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encryptedpassword;
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
        /*----------------------------------------------------------------------------*/
        sendOTPbtn.setDisable(true);
        signInBtn.setDisable(true);
        showPwdCheckBox2.setDisable(true);
        userComboBox.setDisable(true);
        OTPtxt.setEditable(false);
        newUsernametxt.setEditable(false);
        newEmailtxt.setEditable(false);
        newPwdtxt.setEditable(false);
        newConfirmPwdtxt.setEditable(false);
        /*----------------------------------------------------------------------------*/
        OTPtxt.textProperty().addListener((observableValue, oldValue, newValue) -> {
            int otp = Integer.parseInt(OTPtxt.getText());
            if (otp == randomNumber && !(otp + "").isEmpty()){
                signInBtn.setDisable(false);
                showPwdCheckBox2.setDisable(false);
                userComboBox.setDisable(false);
                newUsernametxt.setEditable(true);
                newEmailtxt.setEditable(true);
                newPwdtxt.setEditable(true);
                newConfirmPwdtxt.setEditable(true);
            }else{
                signInBtn.setDisable(true);
                showPwdCheckBox2.setDisable(true);
                userComboBox.setDisable(true);
                newUsernametxt.setEditable(false);
                newEmailtxt.setEditable(false);
                newPwdtxt.setEditable(false);
                newConfirmPwdtxt.setEditable(false);
            }
        });
    }
}