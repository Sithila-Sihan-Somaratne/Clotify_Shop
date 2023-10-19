package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import dto.User;
import email.Email;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Random;

public class ForgotPwdWindow {

    @FXML
    private AnchorPane ForgotPane;

    @FXML
    private JFXPasswordField ConfirmNewPwdTxt;

    @FXML
    private JFXPasswordField newPwdTxt;

    @FXML
    private JFXTextField otpText;

    @FXML
    private JFXCheckBox showPwdCheckBox;

    @FXML
    private JFXButton savePwdBtn;
    private final JFXTextField showNewPwdText = new JFXTextField();

    private final JFXTextField showConfirmedNewPwdText = new JFXTextField();
    private int randomNumber = 0;

    public void storeUser(String userName){
        try{
            Session session = HibernateUtil.getSession();
            User user = session.find(User.class, userName);
            if (user!=null){
                Email.user = user;
                session.close();
            }else{
                new Alert(Alert.AlertType.ERROR,"Enter the right name of the username!").show();
                Stage stage = (Stage) ForgotPane.getScene().getWindow();
                stage.close();
            }
        }catch (Exception ignored){}
    }

    @FXML
    void btnOkOnClick(ActionEvent event) {
        if (Objects.equals(otpText.getText(), randomNumber + "")){
            newPwdTxt.setEditable(true);
            ConfirmNewPwdTxt.setEditable(true);
            savePwdBtn.setDisable(false);
            showPwdCheckBox.setDisable(false);
        }else{
            new Alert(Alert.AlertType.WARNING,"You entered the wrong PIN. Please try again").show();
        }
    }

    @FXML
    void savePwd(ActionEvent event) {
        if(Objects.equals(newPwdTxt.getText(),"") || Objects.equals(ConfirmNewPwdTxt.getText(),"")){
            new Alert(Alert.AlertType.WARNING,"Text fields cannot be empty!").show();
        }else if (!Objects.equals(newPwdTxt.getText(), ConfirmNewPwdTxt.getText())){
            new Alert(Alert.AlertType.WARNING,"Both text fields must have the same content.").show();
        }else{
            String newPassword = newPwdTxt.getText();
            Session session = HibernateUtil.getSession();
            User user = session.find(User.class, Email.user.getName());
            user.setPassword(updatePwd(newPassword));
            Transaction transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            session.close();
            new Alert(Alert.AlertType.INFORMATION,"Password has been updated successfully!").show();
        }
    }

    private String updatePwd(String newPassword) {
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
    void sendOTP(ActionEvent event) {
        Random random = new Random();
        randomNumber = random.nextInt(900) + 100;
        new Email().sendOTPByMail(randomNumber);
    }

    @FXML
    void showBothPwd(ActionEvent event) {
        if (showPwdCheckBox.isSelected()) {
            showNewPwdText.setVisible(true);
            showConfirmedNewPwdText.setVisible(true);
            newPwdTxt.setVisible(false);
            ConfirmNewPwdTxt.setVisible(false);
        } else {
            newPwdTxt.setVisible(true);
            ConfirmNewPwdTxt.setVisible(true);
            showNewPwdText.setVisible(false);
            showConfirmedNewPwdText.setVisible(false);
        }
    }

    @FXML
    void initialize() {
        ForgotPane.getChildren().add(showNewPwdText);
        showNewPwdText.managedProperty().bind(showNewPwdText.visibleProperty());
        newPwdTxt.managedProperty().bind(newPwdTxt.visibleProperty());
        showNewPwdText.textProperty().bindBidirectional(newPwdTxt.textProperty());
        showNewPwdText.setLayoutX(newPwdTxt.getLayoutX());
        showNewPwdText.setLayoutY(newPwdTxt.getLayoutY());
        showNewPwdText.setPrefWidth(newPwdTxt.getPrefWidth());
        showNewPwdText.setPrefHeight(newPwdTxt.getPrefHeight());
        showNewPwdText.setVisible(false);
        /*-----------------------------------------------------------------------------*/
        ForgotPane.getChildren().add(showConfirmedNewPwdText);
        showConfirmedNewPwdText.managedProperty().bind(showConfirmedNewPwdText.visibleProperty());
        ConfirmNewPwdTxt.managedProperty().bind(ConfirmNewPwdTxt.visibleProperty());
        showConfirmedNewPwdText.textProperty().bindBidirectional(ConfirmNewPwdTxt.textProperty());
        showConfirmedNewPwdText.setLayoutX(ConfirmNewPwdTxt.getLayoutX());
        showConfirmedNewPwdText.setLayoutY(ConfirmNewPwdTxt.getLayoutY());
        showConfirmedNewPwdText.setPrefWidth(ConfirmNewPwdTxt.getPrefWidth());
        showConfirmedNewPwdText.setPrefHeight(ConfirmNewPwdTxt.getPrefHeight());
        showConfirmedNewPwdText.setVisible(false);
        /*-----------------------------------------------------------------------------*/
        newPwdTxt.setEditable(false);
        ConfirmNewPwdTxt.setEditable(false);
        savePwdBtn.setDisable(true);
        showPwdCheckBox.setDisable(true);
    }
}
