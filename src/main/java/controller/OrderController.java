package controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import dto.*;
import dto.tm.OrderDetailTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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
    private TreeTableColumn<?,?> optionCol;

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
    private JFXTreeTableView<OrderDetailTM> orderTable;

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
                    Session itmSession = HibernateUtilItem.getSession();
                    Items item = itmSession.find(Items.class, itemCodetxt.getText());
                    itmSession.close();
                    Session session = HibernateUtilOrderDetails.getSession();
                    Transaction transaction = session.beginTransaction();
                    OrderDetails orderDetails = new OrderDetails(itemCodetxt.getText(), Descriptiontxt.getText(), Integer.parseInt(QtyOnhandtxt.getText()), Double.parseDouble(sellingPricetxt.getText()), datePicker.getValue().toString(), Double.parseDouble(Discounttxt.getText()), Typetxt.getText(), Sizetxt.getText(), Double.parseDouble(Totaltxt.getText()), null, item);
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
                    loadTable(datePicker.getValue().toString());
                    double total = getTotal(datePicker.getValue().toString(), Totaltxt.getText());
                    double discount = getDiscount(datePicker.getValue().toString(), Discounttxt.getText());
                    Totaltxt.setText(String.valueOf(total));
                    discountLabeltxt.setText(String.valueOf(discount));
                }
            }else{
                new Alert(Alert.AlertType.WARNING,"Please select one of the two options.").show();
            }
        }

    }

    private static double getDiscount(String date, String text) {
        double discount = 0;
        Session sess = HibernateUtilOrderDetails.getSession();
        String string = "FROM OrderDetails WHERE date = '"+date+"'";
        Query query = sess.createQuery(string);
        List<OrderDetails> list = query.list();
        sess.close();
        if (list != null){
            for (OrderDetails details : list){
                discount = discount + details.getDiscount();
            }
            return discount;
        }
        return Double.parseDouble(text);
    }

    private static double getTotal(String date, String text) {
        double total = 0;
        Session sess = HibernateUtilOrderDetails.getSession();
        String string = "FROM OrderDetails WHERE date = '"+date+"'";
        Query query = sess.createQuery(string);
        List<OrderDetails> list = query.list();
        sess.close();
        if (list != null){
            for (OrderDetails details : list){
                total = total + details.getAmount();
            }
            return total;
        }
        return Double.parseDouble(text);
    }

    private void loadTable(String date) {
        ObservableList<OrderDetailTM> tmList = FXCollections.observableArrayList();
        try {
            Session session = HibernateUtilOrderDetails.getSession();
            Query query = session.createQuery("FROM OrderDetails WHERE date = '"+date+"'");
            List<OrderDetails> list = query.list();
            session.close();

            if (list != null){
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
                orderTable.setRoot(treeItem);
                orderTable.setShowRoot(false);
            }

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
                    String hql = "FROM OrderDetails WHERE itemCode = :itemCode";
                    Query query = session.createQuery(hql);
                    query.setParameter("itemCode",orderDetails.getItemCode());
                    List<OrderDetails> list = query.list();
                    for (OrderDetails details : list){
                        session.delete(details);
                        transaction.commit();
                    }
                    session.close();
                    new Alert(Alert.AlertType.INFORMATION,"Item has been deleted successfully from cart!").show();
                    loadTable(datePicker.getValue().toString());
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
                throw new RuntimeException(e);
            }
        });
        return btn;
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
        Session empSession = HibernateUtilEmployer.getSession();
        Employers employers = empSession.find(Employers.class, employerComboBox.getValue());
        empSession.close();
        Orders orders = new Orders(orderID.getText(), datePicker.getValue().toString(), Double.parseDouble(Totaltxt.getText()), customerNametxt.getText(), customerContacttxt.getText(), customerEmailtxt.getText(), employers);
        Session sess = HibernateUtilOrder.getSession();
        Transaction trans = sess.beginTransaction();
        sess.save(orders);
        trans.commit();
        sess.close();
        Session ordDetailsSession = HibernateUtilOrderDetails.getSession();
        String s = "FROM OrderDetails WHERE date = '"+datePicker.getValue().toString()+"'";
        Query query = ordDetailsSession.createQuery(s);
        List<OrderDetails> list = query.list();
        OrderDetails details = null;
        for (OrderDetails orderDetails : list){
            details = orderDetails;
        }
        if (details != null) {
            Objects.requireNonNull(details).setOrder(orders);
        }
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
        String date = LocalDate.now().toString();
        Session sess = HibernateUtilOrderDetails.getSession();
        String string = "FROM OrderDetails WHERE date = '"+date+"'";
        Query query = sess.createQuery(string);
        List<OrderDetails> list = query.list();
        for (OrderDetails orderDetails : list){
            if (orderDetails!=null){
                Transaction trans = sess.beginTransaction();
                sess.delete(orderDetails);
                trans.commit();
            }
        }
        sess.close();
        Session session = HibernateUtilOrder.getSession();
        Transaction transaction = session.beginTransaction();
        Orders orders = session.find(Orders.class, orderID.getText());
        if (orders!=null){
            session.delete(orders);
            transaction.commit();
            session.close();
            generateId();
            clearFields();
            loadTable(orders.getDate());
        }
        new Alert(Alert.AlertType.INFORMATION,"Order has been deleted successfully!").show();
    }

    @FXML
    void UpdateOrder(ActionEvent event) {
        Session session1 = HibernateUtilOrder.getSession();
        Orders orders = session1.find(Orders.class, orderID.getText());
        orders.setDate(datePicker.getValue().toString());
        orders.setTotal(Double.parseDouble(Totaltxt.getText()));
        orders.setCustName(customerNametxt.getText());
        orders.setCustContact(customerContacttxt.getText());
        orders.setCustEmail(customerEmailtxt.getText());
        Transaction transaction = session1.beginTransaction();
        session1.save(orders);
        transaction.commit();
        session1.close();
        Session session2 = HibernateUtilOrderDetails.getSession();
        OrderDetails orderDetails = session2.find(OrderDetails.class,itemCodetxt.getText());
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
        loadTable(orders.getDate());
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
        var image = new Image("file:/C:/desktop%20copy/NEW%20SHARED%20FOLDER/JavaFX-Final-Project/Code/Clotify_Shop/src/main/resources/img/bg-img.jpg");
        var bgImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1.0, 1.0, true, true,false,false)
        );
        orderPane.setBackground(new Background(bgImage));
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
        optionCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Option"));
        loadTable(LocalDate.now().toString());

        orderTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) ->{
            if (newValue!=null){
                setData(newValue);
            }
        });

        txtSearch.textProperty().addListener((observableValue, oldValue, newValue) -> orderTable.setPredicate(orderDetailsTMTreeItem -> orderDetailsTMTreeItem.getValue().getItemCode().contains(newValue)));
        cashTxt.textProperty().addListener((observableValue, oldValue, newValue) -> {
            String regex = "^\\d*\\.?\\d*$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(cashTxt.getText());
            if (matcher.matches() && !Objects.equals(cashTxt.getText(), "")) {
                Balancetxt.setText(String.valueOf(Double.parseDouble(cashTxt.getText()) - Double.parseDouble(Totaltxt.getText())));
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
                    Totaltxt.setText(String.valueOf(Double.parseDouble(sellingPricetxt.getText()) * Integer.parseInt(QtyOnhandtxt.getText())));
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

    private void setData(TreeItem<OrderDetailTM> newValue) {
        try{
            Session session = HibernateUtilOrder.getSession();
            String string = "FROM Orders WHERE date = '"+newValue.getValue().getDate()+"'";
            Query query = session.createQuery(string);
            List<Orders> list = query.list();
            for(Orders orders : list){
                orderID.setText(orders.getOrderId());
                employerComboBox.setValue(orders.getEmployer().getId());
                setEmployerName(employerComboBox.getValue());
                customerNametxt.setText(orders.getCustName());
                customerContacttxt.setText(orders.getCustContact());
                customerEmailtxt.setText(orders.getCustEmail());
                datePicker.setValue(LocalDate.parse(orders.getDate()));
                itemCodetxt.setText(newValue.getValue().getItemCode());
                Descriptiontxt.setText(newValue.getValue().getDescription());
                QtyOnhandtxt.setText(String.valueOf(newValue.getValue().getQty()));
                Profittxt.setText(String.valueOf(newValue.getValue().getUnitPrice()));
                Typetxt.setText(newValue.getValue().getType());
                Sizetxt.setText(newValue.getValue().getSize());
                Discounttxt.setText(String.valueOf(newValue.getValue().getDiscount()));
                Totaltxt.setText(String.valueOf(newValue.getValue().getAmount()));
                sellingPricetxt.setText(String.valueOf(orders.getTotal() - Double.parseDouble(QtyOnhandtxt.getText())));
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
        String hql = "FROM Items WHERE supplier.id = :supplierId";
        Query query = session.createQuery(hql);
        query.setParameter("supplierId", newValue);
        List<Items> list = query.list();
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
            orderID.setText(String.format("ORD-%08d",num));
        }else {
            orderID.setText("ORD-00000001");
        }
    }

}
