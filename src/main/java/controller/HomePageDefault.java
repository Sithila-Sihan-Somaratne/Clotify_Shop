package controller;

import com.jfoenix.controls.JFXButton;
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

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class HomePageDefault {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane HomePane;

    @FXML
    private Label dateLabel;

    @FXML
    private PieChart pieClothes;

    @FXML
    private Label timeLabel;

    @FXML
    private JFXButton btnSalesReport;

    @FXML
    void employerBtn(ActionEvent ignored) {

    }

    @FXML
    void itemBtn(ActionEvent ignored) {

    }

    @FXML
    void logOut(ActionEvent ignored) {
        Optional<ButtonType> option = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to log out?", ButtonType.YES, ButtonType.NO).showAndWait();
        if (option.isPresent() && (option.get() == ButtonType.YES)){
            Stage stage = (Stage) pieClothes.getScene().getWindow();
            stage.close();
            Stage stg = new Stage();
            try {
                stg.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/LogInWindow.fxml")))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            stg.setResizable(false);
            stg.show();
        }
    }

    @FXML
    void openCalculator(ActionEvent ignored) {
        Runtime run = Runtime.getRuntime();
        try {
            run.exec("calc");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void orderBtn(ActionEvent ignored) {
        Stage stage = (Stage) HomePane.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/OrderWindow.fxml")))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        stage.setResizable(false);
        stage.show();
        new OrderController().classTitle = "HomePageDefault";
    }

    @FXML
    void orderDetailsBtn(ActionEvent ignored) {

    }
    
    @FXML
    void salesReturnBtn(ActionEvent ignored) {

    }

    @FXML
    void supplierBtn(ActionEvent ignored) {

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
        btnSalesReport.setDisable(true);
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