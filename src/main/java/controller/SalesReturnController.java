package controller;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import dto.OrderDetails;
import dto.Orders;
import dto.User;
import dto.tm.withoutBtn.OrderDetailsTM;
import javafx.beans.value.ChangeListener;
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

public class SalesReturnController {

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
    private TreeTableColumn<?, ?> OptionCol;

    @FXML
    private TreeTableColumn<?, ?> QtyCol;

    @FXML
    private TreeTableColumn<?, ?> SizeCol;

    @FXML
    private TreeTableColumn<?, ?> TypeCol;

    @FXML
    private TreeTableColumn<?, ?> UnitPriceCol;

    @FXML
    private AnchorPane salesReturnPane;

    @FXML
    private JFXTreeTableView<OrderDetailsTM> orderDetailsTable;

    @FXML
    private JFXTextField orderIDtxt;

    @FXML
    private JFXTextField qtyText;

    public static User user;
    private ChangeListener<String> changeListener;

    private boolean isListenerActive = true;

    @FXML
    void backToHome(ActionEvent event) {
        Stage stage = (Stage) salesReturnPane.getScene().getWindow();
        if (Objects.equals(user.getType(), "Admin")){
            stage.setTitle("Home Page (Admin)");
            try {
                stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/HomePageAdmin.fxml")))));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else if(Objects.equals(user.getType(),"Default")){
            stage.setTitle("Home Page (Default)");
            try {
                stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/HomePageDefault.fxml")))));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void clear(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        orderIDtxt.textProperty().removeListener(changeListener);
        orderIDtxt.setText("");
        qtyText.setText("0");
        orderIDtxt.textProperty().addListener(changeListener);
    }

    @FXML
    void placeReturn(ActionEvent event) {

    }

    @FXML
    void updateReturn(ActionEvent event) {

    }

    @FXML
    void initialize() {
        ItemCodeCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("ItemCode"));
        DescriptionCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Description"));
        QtyCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Qty"));
        UnitPriceCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("UnitPrice"));
        DateCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Date"));
        DiscountCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Discount"));
        TypeCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Type"));
        SizeCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Size"));
        AmountCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Amount"));
        OptionCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Option"));
        LoadTables();

        orderDetailsTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) ->{
            if (newValue!=null){
                setData(newValue);
            }
        });

        changeListener = (observableValue, oldValue, newValue) -> {
            if (!isListenerActive) {
                return;
            }

            Session session = HibernateUtilOrder.getSession();
            String string = "FROM Orders";
            Query query = session.createQuery(string);
            ArrayList<Orders> list = (ArrayList<Orders>) query.list();
            String date;
            for (Orders orders : list){
                if (Objects.equals(orders.getOrderId(), newValue)){
                    date = orders.getDate();
                    String finalDate = date;
                    orderDetailsTable.setPredicate(ordersTMTreeItem -> ordersTMTreeItem.getValue().getDate().contains(finalDate));
                    break;
                }else{
                    orderDetailsTable.setPredicate(null);
                }
            }
            session.close();
        };

        orderIDtxt.textProperty().addListener(changeListener);
    }

    private void setData(TreeItem<OrderDetailsTM> newValue) {
        qtyText.setText(String.valueOf(newValue.getValue().getQty()));
        String date = newValue.getValue().getDate();
        try{
            Session session = HibernateUtilOrder.getSession();
            String string = "FROM Orders";
            Query query = session.createQuery(string);
            List<Orders> list = query.list();
            for(Orders orders : list){
                if (Objects.equals(date, orders.getDate())){
                    isListenerActive = false;
                    orderIDtxt.setText(orders.getOrderId());
                    isListenerActive = true;
                    break;
                }
            }
            session.close();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    private void LoadTables() {
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
