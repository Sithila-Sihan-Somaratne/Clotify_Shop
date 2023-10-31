package controller;

import dto.OrderDetails;
import dto.User;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.hibernate.Session;
import org.hibernate.query.Query;
import util.HibernateUtilOrderDetails;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class HomePageAdmin {

    @FXML
    private AnchorPane HomePane;

    @FXML
    private Label dateLabel;

    @FXML
    private PieChart pieClothes;

    @FXML
    private Label timeLabel;

    public static User user = new User();

    @FXML
    void employerBtn(ActionEvent event) {
        Stage stage = (Stage) HomePane.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/EmployersWindow.fxml")))));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        stage.setTitle("Employer Window");
        stage.setResizable(false);
        stage.show();
        EmployersController.user = user;
    }

    @FXML
    void itemBtn(ActionEvent event) {
        Stage stage = (Stage) HomePane.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/ItemWindow.fxml")))));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        stage.setTitle("Item Window");
        stage.setResizable(false);
        stage.show();
        ItemController.user = user;
    }

    @FXML
    void logOut(ActionEvent event) {
        Optional<ButtonType> option = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to log out?", ButtonType.YES, ButtonType.NO).showAndWait();
        if (option.isPresent() && (option.get() == ButtonType.YES)){
            Stage stage = (Stage) HomePane.getScene().getWindow();
            stage.close();
            Stage stg = new Stage();
            stg.setTitle("Log In Page");
            try {
                stg.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/LogInWindow.fxml")))));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            stg.setResizable(false);
            stg.show();
        }
    }

    @FXML
    void openCalculator(ActionEvent event) {
        Runtime run = Runtime.getRuntime();
        try {
            run.exec("calc");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void orderBtn(ActionEvent event) {
        Stage stage = (Stage) HomePane.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/OrderWindow.fxml")))));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        stage.setTitle("Order Window");
        stage.setResizable(false);
        stage.show();
        OrderController.user = user;
    }

    @FXML
    void orderDetailsBtn(ActionEvent event) {
        Stage stage = (Stage) HomePane.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/OrderDetailsWindow.fxml")))));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        stage.setTitle("Order Details Window");
        stage.setResizable(false);
        stage.show();
        OrderDetailController.user = user;
    }

    @FXML
    void salesReportBtn(ActionEvent event) {
        Stage stage = (Stage) HomePane.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/SalesReportWindow.fxml")))));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        stage.setTitle("Sales Report Window");
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void salesReturnBtn(ActionEvent event) {
        Stage stage = (Stage) HomePane.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/SalesReturnWindow.fxml")))));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        stage.setTitle("Sales Return Window");
        stage.setResizable(false);
        stage.show();
        SalesReturnController.user = user;
    }

    @FXML
    void supplierBtn(ActionEvent event) {
        Stage stage = (Stage) HomePane.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/SuppliersWindow.fxml")))));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        stage.setTitle("Supplier Window");
        stage.setResizable(false);
        stage.show();
        SuppliersController.user = user;
    }

    @FXML
    void initialize() {
        var image = new Image("file:/C:/desktop%20copy/NEW%20SHARED%20FOLDER/JavaFX-Final-Project/Code/Clotify_Shop/src/main/resources/img/bg-img.jpg");
        var bgImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1.0, 1.0, true, true,false,false)
        );
        HomePane.setBackground(new Background(bgImage));
        manageDateAndTime();
        pieClothes.setData(setPieChartData());
    }

    private ObservableList<PieChart.Data> setPieChartData() {
        ObservableList<PieChart.Data> pieChartData;
        Session session = HibernateUtilOrderDetails.getSession();
        String string = "SELECT COUNT(*) FROM OrderDetails";
        Query query = session.createQuery(string);
        int count = query.list().size();
        int gents = 0;
        int ladies = 0;
        int kids = 0;
        int others = 0;
        int totQty = 0;
        session.close();
        if (count != 0){
            Session sess = HibernateUtilOrderDetails.getSession();
            String str = "FROM OrderDetails";
            Query qry = sess.createQuery(str);
            List<OrderDetails> list = qry.list();
            for (OrderDetails orderDetails : list){
                int numCount = 0;
                while(numCount != count){
                    numCount++;
                    if (Objects.equals(orderDetails.getType(), "Gents")){
                        gents = orderDetails.getQty();
                    } else if (Objects.equals(orderDetails.getType(), "Ladies")) {
                        ladies = orderDetails.getQty();
                    } else if (Objects.equals(orderDetails.getType(), "Kids")) {
                        kids = orderDetails.getQty();
                    } else if (Objects.equals(orderDetails.getType(), "Others")) {
                        others = orderDetails.getQty();
                    }
                }
                totQty = gents+ladies+kids+others;
            }
        }
        float gentsData = ((float) gents / totQty) * 100;
        float ladiesData = ((float) ladies / totQty) * 100;
        float kidsData = ((float) kids / totQty) * 100;
        float othersData = ((float) others / totQty) * 100;
        pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Gents", gentsData),
                new PieChart.Data("Ladies", ladiesData),
                new PieChart.Data("Kids", kidsData),
                new PieChart.Data("Others", othersData));
        return pieChartData;
    }

    private void manageDateAndTime() {
        Timeline date = new Timeline(new KeyFrame(
                Duration.ZERO,
                actionEvent -> dateLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        ), new KeyFrame(Duration.seconds(1)));

        date.setCycleCount(Animation.INDEFINITE);
        date.play();

        Timeline time = new Timeline(new KeyFrame(
                Duration.ZERO,
                actionEvent -> timeLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
        ), new KeyFrame(Duration.seconds(1)));

        time.setCycleCount(Animation.INDEFINITE);
        time.play();
    }
}