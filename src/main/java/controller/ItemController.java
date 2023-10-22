package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import dto.Items;
import dto.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.query.Query;
import util.HibernateUtilItem;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ItemController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField AddQtytxt;

    @FXML
    private TreeTableColumn<?, ?> BuyingPriceCol;

    @FXML
    private TreeTableColumn<?, ?> DescriptionCol;

    @FXML
    private JFXTextField Descriptiontxt;

    @FXML
    private TreeTableColumn<?, ?> ItemCodeCol;

    @FXML
    private TreeTableColumn<?, ?> OptionCol;

    @FXML
    private TreeTableColumn<?, ?> ProfitCol;

    @FXML
    private TreeTableColumn<?, ?> QtyCol;

    @FXML
    private JFXTextField Qtytxt;

    @FXML
    private TreeTableColumn<?, ?> SellingPriceCol;

    @FXML
    private TreeTableColumn<?, ?> SizeCol;

    @FXML
    private TreeTableColumn<?, ?> TypeCol;

    @FXML
    private JFXTextField buyingPricetxt;

    @FXML
    private JFXTextField itemCodetxt;

    @FXML
    private AnchorPane itemPane;

    @FXML
    private JFXTreeTableView<?> itemTable;

    @FXML
    private Label profitLabel;

    @FXML
    private JFXTextField sellingPricetxt;

    @FXML
    private JFXComboBox<?> sizeComboBox;

    @FXML
    private JFXComboBox<?> supplierComboBox;

    @FXML
    private TreeTableColumn<?, ?> supplierIDCol;

    @FXML
    private JFXTextField supplierNametxt;

    @FXML
    private JFXComboBox<?> typeComboBox;

    public static User user;

    @FXML
    void AddItemToCart(ActionEvent event) {

    }

    @FXML
    void Clear(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {

    }

    @FXML
    void UpdateItem(ActionEvent event) {

    }

    @FXML
    void backToHome(ActionEvent event) {
        Stage stage = (Stage) itemPane.getScene().getWindow();
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
    void printItemBill(ActionEvent event) {

    }

    @FXML
    void initialize() {
        itemCodetxt.setEditable(false);
        generateID();
    }

    private void generateID() {
        Session session = HibernateUtilItem.getSession();
        String qry = "FROM Items ORDER BY code DESC";
        Query query = session.createQuery(qry);
        List<Items> list = query.list();
        if ((long) list.size() > 0){
            int num = Integer.parseInt(list.get(0).getCode().split("P-")[1]);
            num++;
            itemCodetxt.setText(String.format("P-%05d",num));
        }else {
            itemCodetxt.setText("P-00001");
        }
    }

}
