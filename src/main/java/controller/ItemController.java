package controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import dto.Items;
import dto.Suppliers;
import dto.User;
import dto.tm.ItemsTM;
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
import util.HibernateUtilSupplier;

import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

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
    private JFXTreeTableView<ItemsTM> itemTable;

    @FXML
    private Label profitLabel;

    @FXML
    private JFXTextField sellingPricetxt;

    @FXML
    private JFXComboBox<String> sizeComboBox;

    @FXML
    private JFXComboBox<String> supplierComboBox;

    @FXML
    private TreeTableColumn<?, ?> supplierIDCol;

    @FXML
    private JFXTextField supplierNametxt;

    @FXML
    private JFXComboBox<String> typeComboBox;

    public JFXTextField txtSearch;

    public static User user;

    @FXML
    void AddItemToStock(ActionEvent event) {
        ArrayList<String> textValues = new ArrayList<>();
        textValues.add(supplierNametxt.getText());
        textValues.add(Descriptiontxt.getText());
        textValues.add(Qtytxt.getText());
        textValues.add(buyingPricetxt.getText());
        textValues.add(sellingPricetxt.getText());
        boolean areEmpty = false;
        for(String value : textValues){
            if (Objects.equals(value, "")){
                new Alert(Alert.AlertType.WARNING,"Text fields cannot be empty!").show();
                areEmpty = true;
                break;
            }
        }
        if (typeComboBox.getValue() != null && sizeComboBox.getValue() != null && supplierComboBox.getValue() != null){
            if (!areEmpty) {
                Items item = new Items(itemCodetxt.getText(), Descriptiontxt.getText(), Integer.parseInt(Qtytxt.getText()), Double.parseDouble(buyingPricetxt.getText()), Double.parseDouble(sellingPricetxt.getText()), typeComboBox.getValue(), sizeComboBox.getValue(), Double.parseDouble(profitLabel.getText()), supplierComboBox.getValue());
                Session session = HibernateUtilItem.getSession();
                Transaction transaction = session.beginTransaction();
                session.save(item);
                transaction.commit();
                session.close();
                clearFields();
                loadTable();
                generateID();
                new Alert(Alert.AlertType.INFORMATION,"Item has been added successfully!").show();
            }
        }else{
            new Alert(Alert.AlertType.WARNING,"Please choose an item for the combo boxes.").show();
        }
    }

    private void loadTable() {
        ObservableList<ItemsTM> tmList = FXCollections.observableArrayList();
        try {
            Session session = HibernateUtilItem.getSession();
            Query query = session.createQuery("FROM Items");
            List<Items> list = query.list();
            session.close();

            for (Items items : list) {
                JFXButton btn = getJfxButton(items);

                tmList.add(new ItemsTM(
                        items.getCode(),
                        items.getDescription(),
                        items.getQty(),
                        items.getSellingPrice(),
                        items.getBuyingPrice(),
                        items.getType(),
                        items.getSize(),
                        items.getProfit(),
                        items.getSupplierID(),
                        btn
                ));
            }
            TreeItem<ItemsTM> treeItem = new RecursiveTreeItem<>(tmList, RecursiveTreeObject::getChildren); //Error comes here.
            itemTable.setRoot(treeItem);
            itemTable.setShowRoot(false);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private JFXButton getJfxButton(Items items) {
        JFXButton btn = new JFXButton("Delete");
        btn.setStyle("-fx-background-color: #e12e2e; -fx-font-weight: BOLD");
        btn.setTextFill(Color.rgb(255,255,255));
        btn.setOnAction(actionEvent -> {
            try {
                Optional<ButtonType> buttonType = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete " + items.getCode() + " item? ", ButtonType.YES, ButtonType.NO).showAndWait();
                if ((buttonType.isPresent()) &&  buttonType.get() == ButtonType.YES){
                    Session session = HibernateUtilItem.getSession();
                    Transaction transaction = session.beginTransaction();
                    session.delete(session.find(Items.class, items.getCode()));
                    transaction.commit();
                    session.close();
                    new Alert(Alert.AlertType.INFORMATION,"Item has been deleted successfully!").show();
                    loadTable();
                    generateID();
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
        generateID();
        clearFields();
    }

    private void clearFields() {
        supplierNametxt.setText("");
        Descriptiontxt.setText("");
        Qtytxt.setText("0");
        buyingPricetxt.setText("0");
        sellingPricetxt.setText("0");
        profitLabel.setText("0");
        supplierComboBox.setValue(null);
        supplierComboBox.setPromptText("Select");
        typeComboBox.setValue(null);
        typeComboBox.setPromptText("Select");
        sizeComboBox.setValue(null);
        sizeComboBox.setPromptText("Select");
    }

    @FXML
    void UpdateItem(ActionEvent event) {
        Session session = HibernateUtilItem.getSession();
        Items item = session.find(Items.class, itemCodetxt.getText());
        item.setDescription(Descriptiontxt.getText());
        item.setQty(Integer.parseInt(Qtytxt.getText()));
        item.setBuyingPrice(Double.parseDouble(buyingPricetxt.getText()));
        item.setSellingPrice(Double.parseDouble(sellingPricetxt.getText()));
        item.setProfit(Double.parseDouble(profitLabel.getText()));
        item.setType(typeComboBox.getValue());
        item.setSize(sizeComboBox.getValue());
        item.setSupplierID(supplierComboBox.getValue());
        Transaction transaction = session.beginTransaction();
        session.save(item);
        transaction.commit();
        session.close();
        loadTable();
        clearFields();
        generateID();
        new Alert(Alert.AlertType.INFORMATION,"Item has been updated successfully!").show();
    }

    @FXML
    void backToHome(ActionEvent event) {
        Stage stage = (Stage) itemPane.getScene().getWindow();
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
    void printItemBill(ActionEvent event) {

    }

    @FXML
    void initialize() {
        itemCodetxt.setEditable(false);
        supplierComboBox.setPromptText("Select");
        typeComboBox.getItems().add(new Label("Gents").getText());
        typeComboBox.getItems().add(new Label("Ladies").getText());
        typeComboBox.getItems().add(new Label("Kids").getText());
        typeComboBox.getItems().add(new Label("Others").getText());
        typeComboBox.setPromptText("Select");
        sizeComboBox.getItems().add(new Label("S").getText());
        sizeComboBox.getItems().add(new Label("M").getText());
        sizeComboBox.getItems().add(new Label("L").getText());
        sizeComboBox.getItems().add(new Label("XL").getText());
        sizeComboBox.getItems().add(new Label("XXL").getText());
        sizeComboBox.setPromptText("Select");
        Session session = HibernateUtilSupplier.getSession();
        String qry = "FROM Suppliers ORDER BY id DESC";
        Query query = session.createQuery(qry);
        List<Suppliers> list = query.list();
        for (Suppliers suppliers : list){
            supplierComboBox.getItems().add(suppliers.getId());
        }
        session.close();
        generateID();
        buyingPricetxt.textProperty().addListener((observableValue, oldValue, newValue) -> {
            String regex = "^\\d*\\.?\\d*$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(buyingPricetxt.getText());
            if (!matcher.matches()){
                new Alert(Alert.AlertType.WARNING,"Please enter digits between 0 and 9.").show();
            }
        });
        sellingPricetxt.textProperty().addListener((observableValue, oldValue, newValue) -> {
            String regex = "^\\d*\\.?\\d*$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(sellingPricetxt.getText());
            double sellingPrice = 0.0;
            double buyingPrice = 0.0;
            if (matcher.matches()) {
                sellingPrice = Double.parseDouble(sellingPricetxt.getText());
                if (!Objects.equals(buyingPricetxt.getText(), "")) {
                    buyingPrice = Double.parseDouble(buyingPricetxt.getText());
                } else {
                    new Alert(Alert.AlertType.WARNING, "Buying Price Text Filed mustn't be empty.").show();
                }
            }else{
                new Alert(Alert.AlertType.WARNING,"Please enter digits between 0 and 9 and a dot (.) .").show();
            }
            profitLabel.setText((sellingPrice-buyingPrice)+"");
        });
        ItemCodeCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Code"));
        DescriptionCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Description"));
        QtyCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Qty"));
        SellingPriceCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("SellingPrice"));
        BuyingPriceCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("BuyingPrice"));
        TypeCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Type"));
        SizeCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Size"));
        ProfitCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Profit"));
        supplierIDCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("SupplierID"));
        OptionCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Option"));
        generateID();
        loadTable();

        itemTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) ->{
            if (newValue!=null){
                setData(newValue);
            }
        });

        txtSearch.textProperty().addListener((observableValue, oldValue, newValue) -> itemTable.setPredicate(itemsTMTreeItem -> itemsTMTreeItem.getValue().getCode().contains(newValue) ||
                itemsTMTreeItem.getValue().getDescription().contains(newValue)));
    }

    private void setData(TreeItem<ItemsTM> value) {
        itemCodetxt.setText(value.getValue().getCode());
        supplierComboBox.setValue(value.getValue().getSupplierID());
        Session session = HibernateUtilSupplier.getSession();
        String qry = "FROM Suppliers";
        Query query = session.createQuery(qry);
        List<Suppliers> list = query.list();
        for (Suppliers suppliers : list){
            supplierNametxt.setText(suppliers.getName());
        }
        session.close();
        Descriptiontxt.setText(String.valueOf(value.getValue().getDescription()));
        Qtytxt.setText(String.valueOf(value.getValue().getQty()));
        buyingPricetxt.setText(String.valueOf(value.getValue().getBuyingPrice()));
        sellingPricetxt.setText(String.valueOf(value.getValue().getSellingPrice()));
        typeComboBox.setValue(value.getValue().getType());
        sizeComboBox.setValue(value.getValue().getSize());
        profitLabel.setText(String.valueOf(value.getValue().getProfit()));
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
        session.close();
    }

    @FXML
    void selectSupplierID(ActionEvent event) {
        if (supplierComboBox.getValue() != null){
            String suppID = supplierComboBox.getValue();
            Session session = HibernateUtilSupplier.getSession();
            Suppliers supplier = session.find(Suppliers.class,suppID);
            supplierNametxt.setText(supplier.getName());
            session.close();
        }
    }
}
