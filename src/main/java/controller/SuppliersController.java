package controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import dto.AccessTablesFromOtherClasses_ATFOC.ItemsATFOC;
import dto.Items;
import dto.Suppliers;
import dto.User;
import dto.tm.SuppliersTM;
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
import util.HibernateUtilItem;
import util.HibernateUtilSupplier;

import java.net.URL;
import java.util.*;

public class SuppliersController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TreeTableColumn<?, ?> OptionCol;

    @FXML
    private TreeTableColumn<?, ?> contactCol;

    @FXML
    private TreeTableColumn<?, ?> descriptionCol;

    @FXML
    private TreeTableColumn<?, ?> companyCol;

    @FXML
    private JFXTreeTableView<ItemsATFOC> supplierItemTable;

    @FXML
    private AnchorPane supplierPane;

    @FXML
    private TreeTableColumn<?, ?> itemCodeCol;

    @FXML
    private TreeTableColumn<?, ?> nameCol;

    @FXML
    private TreeTableColumn<?, ?> qtyCol;

    @FXML
    private JFXTextField searchTxt;

    @FXML
    private JFXTextField supplierContacttxt;

    @FXML
    private JFXTextField supplierCompanytxt;

    @FXML
    private TreeTableColumn<?, ?> supplierIDCol;

    @FXML
    private JFXTextField supplierIdtxt;

    @FXML
    private JFXTextField supplierNametxt;

    @FXML
    private JFXTreeTableView<SuppliersTM> supplierTable;

    @FXML
    private JFXComboBox<String> supplierTitleComboBox;

    @FXML
    private TreeTableColumn<?, ?> titleCol;

    public static User user;

    @FXML
    void Clear(ActionEvent event) {
        generateId();
        clearFields();
    }

    private void clearFields() {
        supplierNametxt.setText("");
        supplierContacttxt.setText("");
        supplierCompanytxt.setText("");
        supplierTitleComboBox.setValue(null);
        itemCodeCol.setGraphic(null);
    }

    @FXML
    void backToHome(ActionEvent event) {
        Stage stage = (Stage) supplierPane.getScene().getWindow();
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
    void printSupplierBill(ActionEvent event) {

    }

    @FXML
    void saveSupplier(ActionEvent event) {
        ArrayList<String> textValues = new ArrayList<>();
        textValues.add(supplierNametxt.getText());
        textValues.add(supplierContacttxt.getText());
        textValues.add(supplierCompanytxt.getText());
        boolean areEmpty = false;
        for(String value : textValues){
            if (Objects.equals(value, "")){
                new Alert(Alert.AlertType.WARNING,"Text fields cannot be empty!").show();
                areEmpty = true;
                break;
            }
        }
        if(supplierTitleComboBox.getValue() != null ){
            if (!areEmpty) {
                Suppliers suppliers = new Suppliers(supplierIdtxt.getText(), supplierTitleComboBox.getValue(), supplierNametxt.getText(), supplierContacttxt.getText(),  supplierCompanytxt.getText());
                Session session = HibernateUtilSupplier.getSession();
                Transaction transaction = session.beginTransaction();
                session.save(suppliers);
                transaction.commit();
                session.close();
                clearFields();
                loadTable();
                generateId();
                new Alert(Alert.AlertType.INFORMATION,"Supplier has been added successfully!").show();
            }else{
                new Alert(Alert.AlertType.WARNING,"Text fields mustn't be empty.").show();
            }
        }else{
            new Alert(Alert.AlertType.WARNING,"Please choose a title.").show();
        }
    }

    private void loadTable() {
        ObservableList<SuppliersTM> tmList = FXCollections.observableArrayList();
        try {
            Session session = HibernateUtilSupplier.getSession();
            Query query = session.createQuery("FROM Suppliers");
            List<Suppliers> list = query.list();
            session.close();
            for (Suppliers suppliers : list) {
                JFXButton btn = getJfxButton(suppliers);

                tmList.add(new SuppliersTM(
                        suppliers.getId(),
                        suppliers.getTitle(),
                        suppliers.getName(),
                        suppliers.getContact(),
                        suppliers.getCompany(),
                        btn
                ));
                TreeItem<SuppliersTM> treeItem = new RecursiveTreeItem<>(tmList, RecursiveTreeObject::getChildren); //Error comes here.
                supplierTable.setRoot(treeItem);
                supplierTable.setShowRoot(false);
             }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private JFXButton getJfxButton(Suppliers suppliers) {
        JFXButton btn = new JFXButton("Delete");
        btn.setStyle("-fx-background-color: #e12e2e; -fx-font-weight: BOLD");
        btn.setTextFill(Color.rgb(255,255,255));
        btn.setOnAction(actionEvent -> {
            try {
                Optional<ButtonType> buttonType = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete " + suppliers.getId() + " employer? ", ButtonType.YES, ButtonType.NO).showAndWait();
                if ((buttonType.isPresent()) &&  buttonType.get() == ButtonType.YES){
                    Session session = HibernateUtilSupplier.getSession();
                    Transaction transaction = session.beginTransaction();
                    session.delete(session.find(Suppliers.class, suppliers.getId()));
                    transaction.commit();
                    session.close();
                    new Alert(Alert.AlertType.INFORMATION,"Supplier has been deleted successfully!").show();
                    loadTable();
                    generateId();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
                throw new RuntimeException(e);
            }
        });
        return btn;
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
        supplierPane.setBackground(new Background(bgImage));
        supplierTitleComboBox.getItems().add(new Label("Mr.").getText());
        supplierTitleComboBox.getItems().add(new Label("Mrs.").getText());
        supplierTitleComboBox.setPromptText("Select");
        supplierIdtxt.setEditable(false);
        supplierIDCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Id"));
        titleCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Title"));
        nameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Name"));
        contactCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Contact"));
        companyCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Company"));
        OptionCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Option"));
        itemCodeCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("ItemCode"));
        descriptionCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Description"));
        qtyCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Qty"));
        generateId();
        loadTable();

        supplierTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) ->{
            if (newValue!=null){
                setData(newValue);
            }
        });

        searchTxt.textProperty().addListener((observableValue, oldValue, newValue) -> supplierTable.setPredicate(supplierTMTreeItem -> supplierTMTreeItem.getValue().getId().contains(newValue) ||
                supplierTMTreeItem.getValue().getName().contains(newValue)));
    }

    private void setData(TreeItem<SuppliersTM> value) {
        supplierIdtxt.setText(value.getValue().getId());
        supplierTitleComboBox.setValue(value.getValue().getTitle());
        supplierNametxt.setText(value.getValue().getName());
        supplierContacttxt.setText(value.getValue().getContact());
        supplierCompanytxt.setText(value.getValue().getCompany());

        ObservableList<ItemsATFOC> TMList = FXCollections.observableArrayList();

        Session sess = HibernateUtilItem.getSession();
        String hql = "FROM Items WHERE supplier.id = :supplierId";
        Query query = sess.createQuery(hql);
        query.setParameter("supplierId", supplierIdtxt.getText());
        List<Items> lst = query.list();
        sess.close();
        for (Items items : lst){
            TMList.add(new ItemsATFOC(
                    items.getCode(),
                    items.getDescription(),
                    items.getQty()
            ));
        }

        TreeItem<ItemsATFOC> trItm = new RecursiveTreeItem<>(TMList, RecursiveTreeObject::getChildren); //Error comes here.
        supplierItemTable.setRoot(trItm);
        supplierItemTable.setShowRoot(false);
    }

    private void generateId() {
        Session session = HibernateUtilSupplier.getSession();
        String qry = "FROM Suppliers ORDER BY id DESC";
        Query query = session.createQuery(qry);
        List<Suppliers> list = query.list();
        if ((long) list.size() > 0){
            int num = Integer.parseInt(list.get(0).getId().split("SUP-")[1]);
            num++;
            supplierIdtxt.setText(String.format("SUP-%05d",num));
        }else {
            supplierIdtxt.setText("SUP-00001");
        }
    }

}
