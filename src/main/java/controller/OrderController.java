package controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import dto.*;
import dto.tm.OrderDetailsTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private JFXTextField customerEmailtxt;

    @FXML
    private JFXTextField customerNametxt;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label discountLabeltxt;

    @FXML
    private JFXComboBox<String> employerComboBox;

    @FXML
    private JFXTextField employerNameTxt;

    @FXML
    private JFXTextField itemCodetxt;

    @FXML
    private Label orderID;

    @FXML
    private JFXTreeTableView<OrderDetailsTM> orderTable;

    @FXML
    private JFXTextField sellingPricetxt;

    @FXML
    private JFXComboBox<String> supplierComboBox;

    @FXML
    private JFXTextField txtSearch;

    public static User user = new User();

    @FXML
    void AddOrderToCart(ActionEvent event) {
        ArrayList<String> textList = new ArrayList<>();
        textList.add(employerNameTxt.getText());
        textList.add(customerNametxt.getText());
        textList.add(customerEmailtxt.getText());
        textList.add(datePicker.getValue().toString());
        textList.add(itemCodetxt.getText());
        textList.add(Discounttxt.getText());
        textList.add(cashTxt.getText());
        boolean areEmpty = false;
        for(String textValues : textList){
            if (Objects.equals(textValues, "")){
                areEmpty = true;
            }else {
                break;
            }
        }
        if(supplierComboBox.getValue() != null && employerComboBox.getValue() != null){
            if (cashComboBox.isSelected() && cardComboBox.isSelected()){
                new Alert(Alert.AlertType.WARNING,"PLease select only one of the two options.").show();
            }else if(cashComboBox.isSelected() && !cardComboBox.isSelected() || !cashComboBox.isSelected() && cardComboBox.isSelected()){
                if (!areEmpty){
                    OrderDetails orderDetails = new OrderDetails(itemCodetxt.getText(), Descriptiontxt.getText(), Integer.parseInt(QtyOnhandtxt.getText()), Double.parseDouble(Profittxt.getText()), datePicker.getValue().toString(), Double.parseDouble(Discounttxt.getText()), Typetxt.getText(), Sizetxt.getText(), Double.parseDouble(Totaltxt.getText()));
                    Session session = HibernateUtilOrderDetails.getSession();
                    Transaction transaction = session.beginTransaction();
                    session.save(orderDetails);
                    transaction.commit();
                    session.close();
                    Session itemSession = HibernateUtilItem.getSession();
                    Items items = itemSession.find(Items.class, itemCodetxt.getText());
                    items.setQty(items.getQty() - Integer.parseInt(QtyOnhandtxt.getText()));
                    Transaction itemTransaction = itemSession.beginTransaction();
                    itemSession.save(items);
                    itemTransaction.commit();
                    itemSession.close();
                    loadTable();
                    new Alert(Alert.AlertType.INFORMATION,"Item has been added to cart successfully successfully!").show();
                }
            }else{
                new Alert(Alert.AlertType.WARNING,"PLease select one of the two options.").show();
            }
        }
    }

    private void loadTable() {
        ObservableList<OrderDetailsTM> tmList = FXCollections.observableArrayList();
        try {
            Session session = HibernateUtilOrderDetails.getSession();
            Query query = session.createQuery("FROM OrderDetails");
            List<OrderDetails> list = query.list();
            session.close();

            for (OrderDetails orderDetails : list) {
                tmList.add(new OrderDetailsTM(
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
            TreeItem<OrderDetailsTM> treeItem = new RecursiveTreeItem<>(tmList, RecursiveTreeObject::getChildren); //Error comes here.
            orderTable.setRoot(treeItem);
            orderTable.setShowRoot(false);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void Clear(ActionEvent event) {
        generateId();
        clearFields();
    }

    private void clearFields() {
        employerNameTxt.setText("");
        customerNametxt.setText("");
        customerEmailtxt.setText("");
        customerContacttxt.setText("");
        itemCodetxt.setText("");
        Descriptiontxt.setText("");
        Qtytxt.setText("0");
        QtyOnhandtxt.setText("0");
        sellingPricetxt.setText("0");
        Profittxt.setText("");
        Discounttxt.setText("0");
        cashTxt.setText("0");
        Totaltxt.setText("0");
        Balancetxt.setText("0");
        employerComboBox.setValue(null);
        supplierComboBox.setValue(null);
        cardComboBox.setSelected(false);
        cashComboBox.setSelected(false);
        datePicker.setValue(null);
    }

    @FXML
    void PlaceOrder(ActionEvent event) {
        String arrears = "Yes";
        if (Double.parseDouble(cashTxt.getText()) >= Double.parseDouble(Totaltxt.getText())){
            arrears = "No";
        }
        Orders orders = new Orders(orderID.getText(), datePicker.getValue().toString(), Double.parseDouble(Totaltxt.getText()), customerNametxt.getText(), customerContacttxt.getText(), customerEmailtxt.getText(), employerComboBox.getValue(), arrears);
        Session sess = HibernateUtilOrder.getSession();
        Transaction trans = sess.beginTransaction();
        sess.save(orders);
        trans.commit();
        sess.close();
        clearFields();
        generateId();
        Optional<ButtonType> option =  new Alert(Alert.AlertType.CONFIRMATION,"Order has been placed successfully! Do you want to print bill?",ButtonType.YES,ButtonType.NO).showAndWait();
        if (option.isPresent()){
            if (option.get() == ButtonType.YES){
                System.out.println("Printing bill...");
            }
        }
    }

    @FXML
    void RemoveOrder(ActionEvent event) {
        Session session = HibernateUtilOrder.getSession();
        Transaction transaction = session.beginTransaction();
        System.out.println();
        Orders orders = session.find(Orders.class, orderID.getText());
        String date = orders.getDate();
        session.delete(orders);
        transaction.commit();
        session.close();
        Session sess = HibernateUtilOrderDetails.getSession();
        String string = "FROM OrderDetails WHERE date = '"+date+"'";
        Query query = sess.createQuery(string);
        List<OrderDetails> list = query.list();
        String code = "";
        for (OrderDetails orderDetails : list){
            code = orderDetails.getItemCode();
        }
        sess.close();
        Session newSession = HibernateUtilOrderDetails.getSession();
        OrderDetails orderDetails = newSession.find(OrderDetails.class, code);
        Transaction trans = newSession.beginTransaction();
        newSession.delete(orderDetails);
        trans.commit();
        newSession.close();
        generateId();
        clearFields();
        loadTable();
        new Alert(Alert.AlertType.INFORMATION,"Order has been deleted successfully!").show();
    }

    @FXML
    void UpdateOrder(ActionEvent event) {
        Session session1 = HibernateUtilOrder.getSession();
        Orders orders = session1.find(Orders.class, orderID.getText());
        orders.setDate(datePicker.getValue().toString());
        orders.setTotal(Double.parseDouble(Totaltxt.getText()));
        Transaction transaction = session1.beginTransaction();
        session1.save(orders);
        transaction.commit();
        session1.close();
        Session session2 = HibernateUtilOrderDetails.getSession();
        String string = "FROM OrderDetails";
        Query query = session2.createQuery(string);
        OrderDetails orderDetails = (OrderDetails) query.list();
        orderDetails.setItemCode(itemCodetxt.getText());
        orderDetails.setDescription(Descriptiontxt.getText());
        orderDetails.setQty(Integer.parseInt(QtyOnhandtxt.getText()));
        orderDetails.setUnitPrice(Double.parseDouble(Profittxt.getText()));
        orderDetails.setDate(datePicker.getValue().toString());
        orderDetails.setDiscount(Double.parseDouble(Discounttxt.getText()));
        orderDetails.setType(Typetxt.getText());
        orderDetails.setSize(Sizetxt.getText());
        orderDetails.setAmount(Double.parseDouble(Totaltxt.getText()));
        Transaction trans = session2.beginTransaction();
        session2.save(orderDetails);
        trans.commit();
        session2.close();
        Session session3 = HibernateUtilItem.getSession();
        Items item = session3.find(Items.class, itemCodetxt.getText());
        item.setQty(item.getQty()-Integer.parseInt(QtyOnhandtxt.getText()));
        Transaction itemTrans = session3.beginTransaction();
        session3.save(item);
        itemTrans.commit();
        session3.close();
        loadTable();
        clearFields();
        generateId();
        new Alert(Alert.AlertType.INFORMATION,"Order has been updated successfully!").show();
    }

    @FXML
    void backToHome(ActionEvent event) {
        Stage stage = (Stage) orderPane.getScene().getWindow();
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
    void openCalculator(ActionEvent event) {
        Runtime run = Runtime.getRuntime();
        try {
            run.exec("calc");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void initialize() {
        generateId();
        Session session = HibernateUtilSupplier.getSession();
        String strQry = "SELECT id FROM Suppliers";
        Query query = session.createQuery(strQry);
        List<String> list = query.list();
        for (String supID : list){
            supplierComboBox.getItems().add(new Label(supID).getText());
        }
        Session sess = HibernateUtilEmployer.getSession();
        String StrQry = "SELECT id FROM Employers";
        Query Qry = sess.createQuery(StrQry);
        List <String> lst = Qry.list();
        for (String empID : lst){
            employerComboBox.getItems().add(new Label(empID).getText());
        }
        sess.close();
        ItemCodeCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("ItemCode"));
        DescriptionCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Description"));
        QtyCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Qty"));
        UnitPriceCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("UnitPrice"));
        DateCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Date"));
        DiscountCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Discount"));
        TypeCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Type"));
        SizeCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Size"));
        AmountCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Amount"));
        loadTable();

        orderTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) ->{
            if (newValue!=null){
                setData(newValue);
            }
        });

        txtSearch.textProperty().addListener((observableValue, oldValue, newValue) -> orderTable.setPredicate(orderDetailsTMTreeItem -> orderDetailsTMTreeItem.getValue().getItemCode().contains(newValue)));
        cashTxt.textProperty().addListener((observableValue, oldValue, newValue) -> {
            String regex = "^[0-9]*\\.?[0-9]*$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(cashTxt.getText());
            if (matcher.matches() && !Objects.equals(cashTxt.getText(), "")) {
                Balancetxt.setText(String.valueOf(Double.parseDouble(Totaltxt.getText()) - Double.parseDouble(cashTxt.getText())));
            }else{
                new Alert(Alert.AlertType.WARNING,"Please enter digits between 0 and 9 and a dot (.) .").show();
            }
        });

        QtyOnhandtxt.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            String regex = "\\d+";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(QtyOnhandtxt.getText());
            if (matcher.matches()) {
                if (!Objects.equals(Profittxt.getText(), "") && !Objects.equals(QtyOnhandtxt.getText(), "")) {
                    Totaltxt.setText(String.valueOf(Double.parseDouble(Profittxt.getText()) * Integer.parseInt(QtyOnhandtxt.getText())));
                }
            }else{
                new Alert(Alert.AlertType.WARNING,"Please enter digits between 0 and 9.").show();
            }
        }));
        Discounttxt.textProperty().addListener((observableValue, oldValue, newValue) -> {
                String regex = "\\d+";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(Discounttxt.getText());
                if (matcher.matches()) {
                    if (!Objects.equals(Discounttxt.getText(),"") && !Objects.equals(QtyOnhandtxt.getText(), "")){
                        int discount = Integer.parseInt(Discounttxt.getText());
                        double total = Double.parseDouble(Totaltxt.getText());
                        double DiscountNum = (double) discount / 100 * total;
                        Totaltxt.setText(String.valueOf(total - DiscountNum));
                    }else{
                        new Alert(Alert.AlertType.WARNING,"Make sure that Qty On Hand Text Field isn't empty.").show();
                    }
                }else{
                    new Alert(Alert.AlertType.WARNING,"Please enter digits between 0 and 9.").show();
                }
        });

    }

    @FXML
    void selectEmployerID(ActionEvent event) {
        if (employerComboBox.getValue() != null){
            setEmployerName(employerComboBox.getValue());
        }
    }

    @FXML
    void selectSupplierID(ActionEvent event) {
        if (supplierComboBox.getValue() != null){
            setItemsData(supplierComboBox.getValue());
        }
    }

    private void setData(TreeItem<OrderDetailsTM> newValue) {
        try{
            Session session = HibernateUtilOrder.getSession();
            String string = "FROM Orders WHERE date = '"+newValue.getValue().getDate()+"'";
            Query query = session.createQuery(string);
            List<Orders> list = query.list();
            for(Orders orders : list){
                orderID.setText(orders.getOrderId());
                employerComboBox.setValue(orders.getEmployerId());
                setEmployerName(employerComboBox.getValue());
                customerNametxt.setText(orders.getCustName());
                customerContacttxt.setText(orders.getCustContact());
                customerEmailtxt.setText(orders.getCustEmail());
                datePicker.setValue(LocalDate.parse(orders.getDate()));
                itemCodetxt.setText(newValue.getValue().getItemCode());
                Descriptiontxt.setText(newValue.getValue().getDescription());
                Qtytxt.setText(String.valueOf(newValue.getValue().getQty()));
                Profittxt.setText(String.valueOf(newValue.getValue().getUnitPrice()));
                Typetxt.setText(newValue.getValue().getType());
                Sizetxt.setText(newValue.getValue().getSize());
                Discounttxt.setText(String.valueOf(newValue.getValue().getDiscount()));
                Totaltxt.setText(String.valueOf(newValue.getValue().getAmount()));
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private void setEmployerName(String value) {
        Session session = HibernateUtilEmployer.getSession();
        Employers employers = session.find(Employers.class, value);
        session.close();
        employerNameTxt.setText(employers.getName());
    }

    private void setItemsData(String newValue) {
        Session session = HibernateUtilItem.getSession();
        String strQry = "FROM Items WHERE supplierID = '"+newValue+"'";
        Query query = session.createQuery(strQry);
        List <Items> list = query.list();
        for (Items items : list){
            itemCodetxt.setText(items.getCode());
            Descriptiontxt.setText(items.getDescription());
            Qtytxt.setText(String.valueOf(items.getQty()));
            sellingPricetxt.setText(String.valueOf(items.getSellingPrice()));
            Profittxt.setText(String.valueOf(items.getProfit()));
            Typetxt.setText(String.valueOf(items.getType()));
            Sizetxt.setText(String.valueOf(items.getSize()));
        }
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
