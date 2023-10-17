package email;

import dto.User;
import javafx.scene.control.Alert;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

public class Email {
    public static User user = new User();
    public void sendOTPByMail(int randomNumber) {
        String from = "sithi.ss23@gmail.com";
        String to = user.getEmail();
        String name = user.getName();
        String password = "quoa htwi pflp ahjh";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "25");
        props.put("mail.smtp.user", "sithi");
        props.put("mail.smtp.password", "sithila2010");
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, password);
                    }
                });
        session.setDebug(true);
        try{
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            message.setSubject("Clotify_Shop: Change Password For Login");
            message.setText("Hello, "+name+". Here's your new pin : "+randomNumber+".\nWith this pin, you can change your password. \nThank you.\nSithila Sihan Somaratne, one of the admins of Clotify_Shop.","UTF-8");
            message.setSentDate(new Date());
            Transport.send(message);
            new Alert(Alert.AlertType.INFORMATION,"OTP has been sent successfully!").show();
        }catch (Exception mex) {
            new Alert(Alert.AlertType.ERROR,"Oops! Something went wrong!").show();
            mex.printStackTrace();
        }
    }
}
