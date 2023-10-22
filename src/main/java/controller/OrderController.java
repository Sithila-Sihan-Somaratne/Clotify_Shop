package controller;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import dto.Orders;
import dto.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.query.Query;
import util.HibernateUtilOrder;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class OrderController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane orderPane;

    @FXML
    private TreeTableColumn<?, ?> AmountCol;

    @FXML
    private Label Balancetxt;

    @FXML
    private TreeTableColumn<?, ?> DateCol;

    @FXML
    private TreeTableColumn<?, ?> DescriptionCol;

    @FXML
    private JFXTextField Descriptiontxt;

    @FXML
    private TreeTableColumn<?, ?> DiscountCol;

    @FXML
    private JFXTextField Discounttxt;

    @FXML
    private TreeTableColumn<?, ?> ItemCodeCol;

    @FXML
    private TreeTableColumn<?, ?> OptionCol;

    @FXML
    private JFXTextField Profittxt;

    @FXML
    private TreeTableColumn<?, ?> QtyCol;

    @FXML
    private JFXTextField QtyOnhandtxt;

    @FXML
    private JFXTextField Qtytxt;

    @FXML
    private TreeTableColumn<?, ?> SizeCol;

    @FXML
    private JFXTextField Sizetxt;

    @FXML
    private Label Totaltxt;

    @FXML
    private TreeTableColumn<?, ?> TypeCol;

    @FXML
    private JFXTextField Typetxt;

    @FXML
    private TreeTableColumn<?, ?> UnitPriceCol;

    @FXML
    private JFXCheckBox cardComboBox;

    @FXML
    private JFXCheckBox cashComboBox;

    @FXML
    private JFXTextField cashTxt;

    @FXML
    private JFXTextField customerContacttxt;

    @FXML
    private JFXTextField customerEmail;

    @FXML
    private JFXTextField customerNametxt;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label discountLabeltxt;

    @FXML
    private JFXComboBox<?> employerComboBox;

    @FXML
    private JFXTextField employerNameTxt;

    @FXML
    private JFXTextField itemCodetxt;

    @FXML
    private Label orderID;

    @FXML
    private JFXTreeTableView<?> orderTable;

    @FXML
    private JFXTextField sellingPricetxt;

    @FXML
    private JFXComboBox<?> supplierComboBox;

    public static User user = new User();

    @FXML
    void AddOrderToCart(ActionEvent event) {

    }

    @FXML
    void Clear(ActionEvent event) {
        employerNameTxt.setText("");
        customerNametxt.setText("");
        customerEmail.setText("");
        customerContacttxt.setText("");
        itemCodetxt.setText("");
        sellingPricetxt.setText("");
        cashTxt.setText("");
    }

    @FXML
    void PlaceOrder(ActionEvent event) {

    }

    @FXML
    void RemoveOrder(ActionEvent event) {

    }

    @FXML
    void UpdateOrder(ActionEvent event) {

    }

    @FXML
    void backToHome(ActionEvent event) {
        Stage stage = (Stage) orderPane.getScene().getWindow();
        if (Objects.equals(user.getType(), "Admin")){
            stage.setTitle("Home Page (Admin)");
            try {
                stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/HomePageAdmin.fxml")))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(Objects.equals(user.getType(),"Default")){
            stage.setTitle("Home Page (Default)");
            try {
                stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/HomePageDefault.fxml")))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void openCalculator(ActionEvent event) {
        Runtime run = Runtime.getRuntime();
        try {
            run.exec("calc");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        generateId();
    }

    private void generateId() {
        Session session = HibernateUtilOrder.getSession();
        String qry = "FROM Orders ORDER BY orderId DESC";
        Query query = session.createQuery(qry);
        List<Orders> list = query.list();
        if ((long) list.size() > 0){
            int num = Integer.parseInt(list.get(0).getOrderId().split("ORD-")[1]);
            num++;
            orderID.setText(String.format("ORD-%07d",num));
        }else {
            orderID.setText("ORD-00000001");
        }
    }

}
