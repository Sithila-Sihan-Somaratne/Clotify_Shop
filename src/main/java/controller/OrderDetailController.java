package controller;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import dto.OrderDetails;
import dto.Orders;
import dto.User;
import dto.tm.OrderDetailsTM;
import dto.tm.OrdersTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.query.Query;
import util.HibernateUtilOrder;
import util.HibernateUtilOrderDetails;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class OrderDetailController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TreeTableColumn<?, ?> AmountCol;

    @FXML
    private TreeTableColumn<?, ?> DateCol;

    @FXML
    private TreeTableColumn<?, ?> DescriptionCol;

    @FXML
    private TreeTableColumn<?, ?> DiscountCol;

    @FXML
    private TreeTableColumn<?, ?> ItemCodeCol;

    @FXML
    private TreeTableColumn<?, ?> QtyCol;

    @FXML
    private TreeTableColumn<?, ?> SizeCol;

    @FXML
    private TreeTableColumn<?, ?> TypeCol;

    @FXML
    private TreeTableColumn<?, ?> UnitPriceCol;

    @FXML
    private TreeTableColumn<?, ?> arrearsCol;

    @FXML
    private TreeTableColumn<?, ?> custContactCol;

    @FXML
    private TreeTableColumn<?, ?> custEmailCol;

    @FXML
    private TreeTableColumn<?, ?> custNameCol;

    @FXML
    private TreeTableColumn<?, ?> dateCol;

    @FXML
    private TreeTableColumn<?, ?> employersIDCol;

    @FXML
    private JFXTreeTableView<OrderDetailsTM> orderDetailsTable;

    @FXML
    private TreeTableColumn<?, ?> orderIDCol;

    @FXML
    private JFXTextField orderIDtxt;

    @FXML
    private AnchorPane orderDetailsPane;

    @FXML
    private JFXTreeTableView<OrdersTM> orderTable;

    @FXML
    private TreeTableColumn<?, ?> totalCol;

    public static User user;

    @FXML
    void backToHome(ActionEvent event) {
        Stage stage = (Stage) orderDetailsPane.getScene().getWindow();
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
    void initialize(){
        orderIDCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("OrderId"));
        dateCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Date"));
        totalCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Total"));
        custNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("CustName"));
        custContactCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("CustContact"));
        custEmailCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("CustEmail"));
        employersIDCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("EmployerId"));
        arrearsCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Arrears"));
        /*--------------------------------------------------------------------------------------*/
        ItemCodeCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("ItemCode"));
        DescriptionCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Description"));
        QtyCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Qty"));
        UnitPriceCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("UnitPrice"));
        DateCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Date"));
        DiscountCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Discount"));
        TypeCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Type"));
        SizeCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Size"));
        AmountCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Amount"));

        LoadTables();
    }

    private void LoadTables() {
        ObservableList<OrdersTM> tmList1 = FXCollections.observableArrayList();
        try {
            Session session = HibernateUtilOrder.getSession();
            Query query = session.createQuery("FROM Orders");
            List<Orders> list = query.list();
            session.close();

            for (Orders orders : list) {
                tmList1.add(new OrdersTM(
                        orders.getOrderId(),
                        orders.getDate(),
                        orders.getTotal(),
                        orders.getCustName(),
                        orders.getCustContact(),
                        orders.getCustEmail(),
                        orders.getEmployerId(),
                        orders.getArrears()
                ));
            }
            TreeItem<OrdersTM> treeItem = new RecursiveTreeItem<>(tmList1, RecursiveTreeObject::getChildren); //Error comes here.
            orderTable.setRoot(treeItem);
            orderTable.setShowRoot(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        ObservableList<OrderDetailsTM> tmList2 = FXCollections.observableArrayList();
        try {
            Session session = HibernateUtilOrderDetails.getSession();
            Query query = session.createQuery("FROM OrderDetails");
            List<OrderDetails> list = query.list();
            session.close();

            for (OrderDetails orderDetails : list) {
                tmList2.add(new OrderDetailsTM(
                        orderDetails.getItemCode(),
                        orderDetails.getDescription(),
                        orderDetails.getQty(),
                        orderDetails.getUnitPrice(),
                        orderDetails.getDate(),
                        orderDetails.getDiscount(),
                        orderDetails.getType(),
                        orderDetails.getSize(),
                        orderDetails.getAmount()
                ));
            }
            TreeItem<OrderDetailsTM> treeItem = new RecursiveTreeItem<>(tmList2, RecursiveTreeObject::getChildren); //Error comes here.
            orderDetailsTable.setRoot(treeItem);
            orderDetailsTable.setShowRoot(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
