package controller;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
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

    public String classTitle = "";

    @FXML
    void AddOrderToCart(ActionEvent event) {

    }

    @FXML
    void Clear(ActionEvent event) {

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
        if (Objects.equals(classTitle, "HomePageAdmin")){
            try {
                stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/HomePageAdmin.fxml")))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(Objects.equals(classTitle,"HomePageDefault")){
            try {
                stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/HomePageDefault.fxml")))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            new Alert(Alert.AlertType.WARNING,"Class not found!").show();
        }
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void openCalculator(ActionEvent event) {

    }

    @FXML
    void initialize() {

    }

}
