package controller;

import com.jfoenix.controls.JFXComboBox;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import dto.Items;
import dto.OrderDetails;
import dto.Orders;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
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
    private LineChart<?, ?> lineChart;

    @FXML
    private NumberAxis numberAxis = new NumberAxis();

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

    }

    @FXML
    void getDailyReport(ActionEvent event) {

    }

    @FXML
    void getMonthlyReport(ActionEvent event) {

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
        selectDayComboBox.getSelectionModel().selectedItemProperty().addListener(((observableValue, stringSingleSelectionModel, t1) -> {
            if (Objects.equals(selectDayComboBox.getValue(), "Today")){
                Session session = HibernateUtilOrder.getSession();
                String string = "FROM Orders";
                Query query = session.createQuery(string);
                List<Orders> list = query.list();
                session.close();
                String currentDay = LocalDate.now().toString();
                Orders ord = null;
                for (Orders orders : list){
                    if (orders!=null){
                        ord = orders;
                    }
                }
                double profit = 0;
                int salesCount = 0;
                double sales;
                if (Objects.equals(Objects.requireNonNull(ord).getDate(), currentDay)){
                    Session sess = HibernateUtilOrderDetails.getSession();
                    String str = "FROM OrderDetails";
                    Query qry = sess.createQuery(str);
                    List<OrderDetails> lst = qry.list();
                    sess.close();
                    for (OrderDetails orderDetails : lst){
                        if (orderDetails!=null){
                            salesCount = salesCount + orderDetails.getQty();
                            Session sss = HibernateUtilItem.getSession();
                            String s = "FROM Items WHERE id = '"+orderDetails.getItemCode()+"'";
                            Query q = sss.createQuery(s);
                            List<Items> l = q.list();
                            for (Items items : l){
                                if (items != null){
                                    profit = profit + items.getProfit();
                                }
                            }
                        }
                    }
                }
                sales = ord.getTotal();
                salesTxt.setText(String.valueOf(sales));
                profitTxt.setText(String.valueOf(profit));
                salesCountTxt.setText(String.valueOf(salesCount));
            }else if (Objects.equals(selectDayComboBox.getValue(), "This month")){
                System.out.println("Month");
            }else if (Objects.equals(selectDayComboBox.getValue(), "This year")){
                System.out.println("Year");
            }
        }));
    }

}
