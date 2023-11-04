package controller;

import com.jfoenix.controls.JFXComboBox;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;

import db.DBConnection;
import dto.Items;
import dto.OrderDetails;
import dto.Orders;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.Session;
import util.HibernateUtilItem;
import util.HibernateUtilOrder;
import util.HibernateUtilOrderDetails;

public class SalesReportController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label profitTxt;

    @FXML
    private Label salesCountTxt;

    @FXML
    private AnchorPane salesReportPane;

    @FXML
    private Label salesTxt;

    @FXML
    private JFXComboBox<String> selectDayComboBox;

    @FXML
    private StackPane chartPane = new StackPane();

    private LineChart lineChart;

    @FXML
    void backToHome(ActionEvent event) {
        Stage stage = (Stage) salesReportPane.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/HomePageAdmin.fxml")))));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        stage.setTitle("Home Page (Admin).");
        stage.show();
        stage.setResizable(false);
    }

    @FXML
    void getAnnualReport(ActionEvent event) {
        try {
            JasperDesign design = JRXmlLoader.load("src/main/resources/jasper/reports/SalesReportThisYear.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(design);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint, false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void getDailyReport(ActionEvent event) {
        try {
            JasperDesign design = JRXmlLoader.load("src/main/resources/jasper/reports/SalesReportToday.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(design);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint, false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void getMonthlyReport(ActionEvent event) {
        try {
            JasperDesign design = JRXmlLoader.load("src/main/resources/jasper/reports/SalesReportThisMonth.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(design);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint, false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
        salesReportPane.setBackground(new Background(bgImage));
        selectDayComboBox.getItems().add(new Label("Today").getText());
        selectDayComboBox.getItems().add(new Label("This month").getText());
        selectDayComboBox.getItems().add(new Label("This year").getText());

        selectDayComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, stringSingleSelectionModel, t1) -> {
            Session checkSession = HibernateUtilOrder.getSession();
            String queryString = "FROM Orders WHERE date_format(date, '%Y-%m-%d') = date_format(now(), '%Y-%m-%D')";
            Query executeQuery = checkSession.createQuery(queryString);
            List<Orders> todayList = executeQuery.list();
            checkSession.close();
            if (todayList != null){
                if (Objects.equals(selectDayComboBox.getValue(), "Today")){
                    Session session = HibernateUtilOrder.getSession();
                    String string = "FROM Orders WHERE date_format(date, '%Y-%m-%d') = date_format(now(), '%Y-%m-%d')";
                    Query query = session.createQuery(string);
                    List<Orders> list = query.list();
                    session.close();
                    String currentDay = LocalDate.now().toString();
                    Orders ord = null;
                    double profit = 0;
                    int salesCount = 0;
                    double sales = 0;
                    LocalDate date = LocalDate.parse(currentDay);
                    int Day = date.getDayOfMonth();
                    int sizeOfMonth = date.lengthOfMonth();
                    for (Orders orders : list){
                        if (orders!=null){
                            ord = orders;
                        }
                    }
                    if (Objects.equals(Objects.requireNonNull(ord).getDate(), currentDay)){
                        sales = ord.getTotal();
                        Session sess = HibernateUtilOrderDetails.getSession();
                        String str = "FROM OrderDetails WHERE date = '"+currentDay+"'";
                        Query qry = sess.createQuery(str);
                        List<OrderDetails> lst = qry.list();
                        sess.close();
                        for (OrderDetails orderDetails : lst){
                            if (orderDetails!=null){
                                salesCount = salesCount + orderDetails.getQty();
                                Session sss = HibernateUtilItem.getSession();
                                String s = "FROM Items WHERE code = '"+orderDetails.getItemCode()+"'";
                                Query q = sss.createQuery(s);
                                List<Items> l = q.list();
                                sss.close();
                                for (Items items : l){
                                    if (items != null){
                                        profit = profit + items.getProfit();
                                    }
                                }
                            }
                        }
                    }
                    salesTxt.setText(String.valueOf(sales));
                    profitTxt.setText(String.valueOf(profit));
                    salesCountTxt.setText(String.valueOf(salesCount));
                    double finalSales = sales;
                    final CategoryAxis xAxis = new CategoryAxis();
                    xAxis.setAutoRanging(false);
                    ObservableList<String> categories = null;
                    if (sizeOfMonth == 31){
                        categories = FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31");
                    }else if(sizeOfMonth == 30){
                        categories = FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30");
                    }else if(sizeOfMonth == 29){
                        categories = FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29");
                    }else if(sizeOfMonth == 28){
                        categories = FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28");
                    }
                    xAxis.setCategories(categories);
                    NumberAxis yAxis = new NumberAxis();
                    yAxis.setAutoRanging(true);
                    yAxis.setLowerBound(0.0);
                    yAxis.setUpperBound(1000000000.0);
                    yAxis.setTickUnit(1);
                    if (lineChart != null){
                        chartPane.getChildren().remove(lineChart);
                    }
                    lineChart = new LineChart<>(xAxis, yAxis);
                    lineChart.setPrefWidth(chartPane.getPrefWidth());
                    lineChart.setPrefHeight(chartPane.getPrefHeight());
                    chartPane.getChildren().add(lineChart);
                    XYChart.Series series = new XYChart.Series();
                    series.setName("Sales");
                    series.getData().add(new XYChart.Data(Day+"", finalSales));
                    if (categories != null) {
                        for(int i = 1; i < categories.size()+1; i++) {
                            if (i != Day) {
                                series.getData().add(new XYChart.Data(i+"", 0));
                            }
                        }
                    }
                    lineChart.getData().clear();
                    lineChart.getData().add(series);
                }else if (Objects.equals(selectDayComboBox.getValue(), "This month")){
                    Session session = HibernateUtilOrder.getSession();
                    String string = "FROM Orders WHERE date_format(date, '%Y-%m') = date_format(now(), '%Y-%m')";
                    Query query = session.createQuery(string);
                    List<Orders> list = query.list();
                    session.close();
                    Orders ord = null;
                    double profit = 0;
                    int salesCount = 0;
                    double sales = 0;
                    LocalDate date = LocalDate.now();
                    int month = date.getMonthValue();
                    for (Orders orders : list){
                        if (orders!=null){
                            ord = orders;
                            date = LocalDate.parse(orders.getDate());
                            sales = sales + ord.getTotal();
                        }
                    }
                    int sizeOfMonth = date.lengthOfMonth();
                    if (Objects.equals(Objects.requireNonNull(ord).getDate(), LocalDate.now().toString())){
                        Session sess = HibernateUtilOrderDetails.getSession();
                        String str = "FROM OrderDetails WHERE date_format(date, '%Y-%m') = date_format(now(), '%Y-%m')";
                        Query qry = sess.createQuery(str);
                        List<OrderDetails> lst = qry.list();
                        sess.close();
                        for (OrderDetails orderDetails : lst){
                            if (orderDetails!=null){
                                LocalDate local = LocalDate.parse(orderDetails.getDate());
                                YearMonth yearMonth = YearMonth.from(local);
                                if (month == yearMonth.getMonthValue()){
                                    salesCount = salesCount + orderDetails.getQty();
                                    Session sss = HibernateUtilItem.getSession();
                                    String s = "FROM Items WHERE code = '"+orderDetails.getItemCode()+"'";
                                    Query q = sss.createQuery(s);
                                    List<Items> l = q.list();
                                    sss.close();
                                    for (Items items : l){
                                        if (items != null){
                                            profit = profit + items.getProfit();
                                        }
                                    }
                                }
                            }
                        }
                    }
                    salesTxt.setText(String.valueOf(sales));
                    profitTxt.setText(String.valueOf(profit));
                    salesCountTxt.setText(String.valueOf(salesCount));
                    final CategoryAxis xAxis = new CategoryAxis();
                    xAxis.setAutoRanging(false);

                    ObservableList<String> categories = null;
                    if (sizeOfMonth == 31){
                        categories = FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31");
                    }else if(sizeOfMonth == 30){
                        categories = FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30");
                    }else if(sizeOfMonth == 29){
                        categories = FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29");
                    }else if(sizeOfMonth == 28){
                        categories = FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28");
                    }
                    xAxis.setCategories(categories);
                    final NumberAxis yAxis = new NumberAxis();
                    yAxis.setAutoRanging(true);
                    yAxis.setLowerBound(0.0);
                    yAxis.setUpperBound(1000000000.0);
                    yAxis.setTickUnit(1);
                    if (lineChart != null){
                        chartPane.getChildren().remove(lineChart);
                    }
                    lineChart = new LineChart<>(xAxis, yAxis);
                    lineChart.setPrefWidth(chartPane.getPrefWidth());
                    lineChart.setPrefHeight(chartPane.getPrefHeight());
                    chartPane.getChildren().add(lineChart);

                    Map<String, Double> salesTotals = new HashMap<>();
                    for (Orders ORDList : list) {
                        LocalDate localDate = LocalDate.parse(ORDList.getDate());
                        int number = localDate.getDayOfMonth();
                        salesTotals.put(number + "", ORDList.getTotal());
                    }

                    XYChart.Series series = new XYChart.Series();
                    series.setName("Sales");

                    if (categories != null) {
                        for (int i = 1; i <= categories.size(); i++) {
                            Double total = salesTotals.getOrDefault(i + "", 0.0);
                            series.getData().add(new XYChart.Data(i + "", total));
                        }
                    }

                    lineChart.getData().clear();
                    lineChart.getData().add(series);

                }else if (Objects.equals(selectDayComboBox.getValue(), "This year")){
                    Session session = HibernateUtilOrder.getSession();
                    Transaction transaction = session.beginTransaction();
                    String string = "FROM Orders WHERE date_format(date, '%Y') = date_format(now(), '%Y')";
                    Query query = session.createQuery(string);
                    List<Orders> list = query.list();
                    session.flush();
                    transaction.commit();
                    session.close();
                    Orders ord = null;
                    double profit = 0;
                    int salesCount = 0;
                    double sales = 0;
                    LocalDate date = LocalDate.now();
                    int year = date.getYear();
                    for (Orders orders : list){
                        if (orders!=null){
                            ord = orders;
                            sales = sales + ord.getTotal();
                        }
                    }
                    String monthName;
                    if (Objects.equals(Objects.requireNonNull(ord).getDate(), LocalDate.now().toString())){
                        Session sess = HibernateUtilOrderDetails.getSession();
                        sess.clear();
                        Transaction trans = sess.beginTransaction();
                        String str = "FROM OrderDetails WHERE date_format(date, '%Y') = date_format(now(), '%Y')";
                        Query qry = sess.createQuery(str);
                        List<OrderDetails> lst = qry.list();
                        sess.flush();
                        trans.commit();
                        sess.close();
                        for (OrderDetails orderDetails : lst){
                            if (orderDetails!=null){
                                LocalDate local = LocalDate.parse(orderDetails.getDate());
                                YearMonth yearMonth = YearMonth.from(local);
                                if (year == yearMonth.getYear()){
                                    salesCount = salesCount + orderDetails.getQty();
                                    Session sss = HibernateUtilItem.getSession();
                                    String s = "FROM Items WHERE code = '"+orderDetails.getItemCode()+"'";
                                    Query q = sss.createQuery(s);
                                    List<Items> l = q.list();
                                    sss.close();
                                    for (Items items : l){
                                        if (items != null){
                                            profit = profit + items.getProfit();
                                        }
                                    }
                                }
                            }
                        }
                    }
                    salesTxt.setText(String.valueOf(sales));
                    profitTxt.setText(String.valueOf(profit));
                    salesCountTxt.setText(String.valueOf(salesCount));
                    final CategoryAxis xAxis = new CategoryAxis();
                    xAxis.setAutoRanging(false);
                    ObservableList<String> categories = FXCollections.observableArrayList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
                    xAxis.setCategories(categories);
                    final NumberAxis yAxis = new NumberAxis();
                    yAxis.setAutoRanging(true);
                    yAxis.setLowerBound(0.0);
                    yAxis.setUpperBound(1000000000.0);
                    yAxis.setTickUnit(1);
                    if (lineChart != null){
                        chartPane.getChildren().remove(lineChart);
                    }
                    lineChart = new LineChart<>(xAxis, yAxis);
                    lineChart.setPrefWidth(chartPane.getPrefWidth());
                    lineChart.setPrefHeight(chartPane.getPrefHeight());
                    chartPane.getChildren().add(lineChart);

                    double totSalesInThisYear = 0;

                    Map<String, Double> salesTotals = new HashMap<>();
                    for (Orders ORDList : list) {
                        LocalDate localDate = LocalDate.parse(ORDList.getDate());
                        Month month = localDate.getMonth();
                        monthName = month.getDisplayName(TextStyle.SHORT, Locale.US);
                        totSalesInThisYear = totSalesInThisYear + ORDList.getTotal();
                        salesTotals.put(monthName, totSalesInThisYear);
                    }

                    XYChart.Series series = new XYChart.Series();
                    series.setName("Sales");

                    for (int i = 0; i <= categories.size()-1; i++) {
                        Double total = salesTotals.getOrDefault(categories.get(i), 0.0);
                        series.getData().add(new XYChart.Data(categories.get(i), total));
                    }

                    lineChart.getData().clear();
                    lineChart.getData().add(series);
                }
            }else{
                new Alert(Alert.AlertType.WARNING,"Sorry! You need to make an order to see the line chart.").show();
            }

        });
    }

}
