package controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class HomePageAdmin {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    public Label dateLabel;

    public PieChart pieClothes;

    public Label timeLabel;

    @FXML
    void employerBtn(ActionEvent ignored) {

    }

    @FXML
    void itemBtn(ActionEvent ignored) {

    }

    @FXML
    void logOut(ActionEvent ignored) {

    }

    @FXML
    void openCalculator(ActionEvent ignored) {

    }

    @FXML
    void orderBtn(ActionEvent ignored) {

    }

    @FXML
    void orderDetailsBtn(ActionEvent ignored) {

    }

    @FXML
    void salesReportBtn(ActionEvent ignored) {

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