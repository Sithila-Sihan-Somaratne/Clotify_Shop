package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import dto.Items;
import dto.OrderDetails;
import dto.Orders;
import dto.User;
import dto.tm.OrderDetailTM;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtilItem;
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
    private JFXTreeTableView<OrderDetailTM> orderDetailsTable;

    @FXML
    private JFXTextField orderIDtxt;

    @FXML
    private JFXTextField qtyText;

    @FXML
    private Label refundTxt;

    public static User user;

    private ChangeListener<String> changeListener;

    private boolean isListenerActive = true;

    private double total = 0;

    private int qty = 0;

    private String itemCode = "";


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
        if (!Objects.equals(qtyText.getText(), "") && !Objects.equals(orderIDtxt.getText(), "")){
            Session session = HibernateUtilItem.getSession();
            Items items = session.find(Items.class, itemCode);
            Transaction transaction = session.beginTransaction();
            items.setQty(items.getQty() + Integer.parseInt(qtyText.getText()));
            session.save(items);
            transaction.commit();
            session.close();
            new Alert(Alert.AlertType.INFORMATION,"The sales return has been done successfully!").show();
        }else{
            new Alert(Alert.AlertType.WARNING, "Text Fields mustn't be empty!").show();
        }
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
        LoadTable();

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
                    total = orders.getTotal();
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

        qtyText.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            String regex = "\\d+";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(qtyText.getText());
            if (matcher.matches()){
                int returnQty = Integer.parseInt(qtyText.getText());
                refundTxt.setText(String.valueOf(total / qty * returnQty));
            }else{
                new Alert(Alert.AlertType.WARNING,"PLease enter digits between 0 and 9.").show();
            }
        }));
    }

    private void setData(TreeItem<OrderDetailTM> newValue) {
        itemCode = newValue.getValue().getItemCode();
        qty = newValue.getValue().getQty();
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

    private void LoadTable() {
        ObservableList<OrderDetailTM> tmList = FXCollections.observableArrayList();
        try {
            Session session = HibernateUtilOrderDetails.getSession();
            Query query = session.createQuery("FROM OrderDetails");
            List<OrderDetails> list = query.list();
            session.close();

            for (OrderDetails orderDetails : list) {
                JFXButton btn = getJfxButton(orderDetails);
                tmList.add(new OrderDetailTM(
                        orderDetails.getItemCode(),
                        orderDetails.getDescription(),
                        orderDetails.getQty(),
                        orderDetails.getUnitPrice(),
                        orderDetails.getDate(),
                        orderDetails.getDiscount(),
                        orderDetails.getType(),
                        orderDetails.getSize(),
                        orderDetails.getAmount(),
                        btn
                ));
            }
            TreeItem<OrderDetailTM> treeItem = new RecursiveTreeItem<>(tmList, RecursiveTreeObject::getChildren); //Error comes here.
            orderDetailsTable.setRoot(treeItem);
            orderDetailsTable.setShowRoot(false);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private JFXButton getJfxButton(OrderDetails orderDetails) {
        JFXButton btn = new JFXButton("Delete");
        btn.setStyle("-fx-background-color: #e12e2e; -fx-font-weight: BOLD");
        btn.setTextFill(Color.rgb(255,255,255));
        btn.setOnAction(actionEvent -> {
            try {
                Optional<ButtonType> buttonType = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete " + orderDetails.getItemCode() + " item from cart? ", ButtonType.YES, ButtonType.NO).showAndWait();
                if ((buttonType.isPresent()) &&  buttonType.get() == ButtonType.YES){
                    Session session = HibernateUtilOrderDetails.getSession();
                    Transaction transaction = session.beginTransaction();
                    session.delete(session.find(OrderDetails.class, orderDetails.getItemCode()));
                    transaction.commit();
                    session.close();
                    new Alert(Alert.AlertType.INFORMATION,"Employer has been deleted successfully from cart!").show();
                    LoadTable();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
                throw new RuntimeException(e);
            }
        });
        return btn;
    }

}
