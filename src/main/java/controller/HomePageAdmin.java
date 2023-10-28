package controller;

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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
            stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/OrderDetailForm.fxml")))));
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

    }

    @FXML
    void salesReturnBtn(ActionEvent event) {

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
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Gents", 0),
                        new PieChart.Data("Ladies", 0),
                        new PieChart.Data("Kids", 0),
                        new PieChart.Data("Others", 0));
        pieClothes.setData(pieChartData);
        manageDateAndTime();
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