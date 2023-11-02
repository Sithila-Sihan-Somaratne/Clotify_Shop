package controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import db.DBConnection;
import dto.Employers;
import dto.User;
import dto.tm.EmployersTM;
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
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtilEmployer;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class EmployersController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane employerPane;

    @FXML
    private TreeTableColumn<?, ?> BBANCol;

    @FXML
    private TreeTableColumn<?, ?> DOBCol;

    @FXML
    private TreeTableColumn<?, ?> NICCol;

    @FXML
    private TreeTableColumn<?, ?> OptionCol;

    @FXML
    private TreeTableColumn<?, ?> addressCol;

    @FXML
    private TreeTableColumn<?, ?> bankbranchCol;

    @FXML
    private TreeTableColumn<?, ?> contactCol;

    @FXML
    private DatePicker datePicker;

    @FXML
    private JFXTextField employerAddresstxt;

    @FXML
    private JFXTextField employerBBANtxt;

    @FXML
    private JFXTextField employerBankBranchtxt;

    @FXML
    private JFXTextField employerContacttxt;

    @FXML
    private TreeTableColumn<?, ?> employerIDCol;

    @FXML
    private JFXTextField employerIdtxt;

    @FXML
    private JFXTextField employerNICtxt;

    @FXML
    private JFXTextField employerNameTxt;

    @FXML
    private JFXTreeTableView<EmployersTM> employerTable;

    @FXML
    private JFXComboBox<String> employerTitleComboBox;

    @FXML
    private TreeTableColumn<?, ?> nameCol;

    @FXML
    private JFXTextField searchTxt;

    @FXML
    private TreeTableColumn<?, ?> titleCol;

    public static User user = new User();

    @FXML
    void Clear(ActionEvent event) {
        generateId();
        clearFields();
    }

    private void clearFields() {
        employerNameTxt.setText("");
        employerAddresstxt.setText("");
        employerContacttxt.setText("");
        employerNICtxt.setText("");
        employerBBANtxt.setText("");
        employerBankBranchtxt.setText("");
        searchTxt.setText("");
        datePicker.setValue(null);
        employerTitleComboBox.setValue(null);
        employerTitleComboBox.setPromptText("Select");
    }

    @FXML
    void backToHome(ActionEvent event) {
        Stage stage = (Stage) employerPane.getScene().getWindow();
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
    void printEmployerReport(ActionEvent event) {
        try {
            JasperDesign design = JRXmlLoader.load("src/main/resources/jasper/reports/EmployerReport.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(design);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint, false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void saveEmployer(ActionEvent event) {
        ArrayList<String> textValues = new ArrayList<>();
        textValues.add(employerNameTxt.getText());
        textValues.add(employerAddresstxt.getText());
        textValues.add(employerContacttxt.getText());
        textValues.add(employerNICtxt.getText());
        textValues.add(employerBBANtxt.getText());
        textValues.add(employerBankBranchtxt.getText());
        boolean areEmpty = false;
        for(String value : textValues){
            if (Objects.equals(value, "")){
                new Alert(Alert.AlertType.WARNING,"Text fields cannot be empty!").show();
                areEmpty = true;
                break;
            }
        }
        if (employerTitleComboBox.getValue() != null){
            if (!areEmpty) {
                Employers employer = new Employers(employerIdtxt.getText(), employerTitleComboBox.getValue(), employerNameTxt.getText(), employerNICtxt.getText(), datePicker.getValue().toString(), employerAddresstxt.getText(), employerContacttxt.getText(), employerBBANtxt.getText(), employerBankBranchtxt.getText());
                Session session = HibernateUtilEmployer.getSession();
                Transaction transaction = session.beginTransaction();
                session.save(employer);
                transaction.commit();
                session.close();
                clearFields();
                loadTable();
                generateId();
                new Alert(Alert.AlertType.INFORMATION,"Employer has been added successfully!").show();
            }else{
                new Alert(Alert.AlertType.WARNING,"Text fields mustn't be empty.").show();
            }
        }else{
            new Alert(Alert.AlertType.WARNING,"Please choose a title.").show();
        }
    }

    private void loadTable() {
        ObservableList<EmployersTM> tmList = FXCollections.observableArrayList();
        try {
            Session session = HibernateUtilEmployer.getSession();
            Query query = session.createQuery("FROM Employers");
            List<Employers> list = query.list();
            session.close();

            for (Employers employers : list) {
                JFXButton btn = getJfxButton(employers);

                tmList.add(new EmployersTM(
                        employers.getId(),
                        employers.getTitle(),
                        employers.getName(),
                        employers.getNIC(),
                        employers.getDOB(),
                        employers.getAddress(),
                        employers.getContact(),
                        employers.getBBAN(),
                        employers.getBank_branch(),
                        btn
                ));
            }
            TreeItem<EmployersTM> treeItem = new RecursiveTreeItem<>(tmList, RecursiveTreeObject::getChildren); //Error comes here.
            employerTable.setRoot(treeItem);
            employerTable.setShowRoot(false);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private JFXButton getJfxButton(Employers employers) {
        JFXButton btn = new JFXButton("Delete");
        btn.setStyle("-fx-background-color: #e12e2e; -fx-font-weight: BOLD");
        btn.setTextFill(Color.rgb(255,255,255));
        btn.setOnAction(actionEvent -> {
            try {
                Optional<ButtonType> buttonType = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete " + employers.getId() + " employer? ", ButtonType.YES, ButtonType.NO).showAndWait();
                if ((buttonType.isPresent()) &&  buttonType.get() == ButtonType.YES){
                    Session session = HibernateUtilEmployer.getSession();
                    Transaction transaction = session.beginTransaction();
                    session.delete(session.find(Employers.class, employers.getId()));
                    transaction.commit();
                    session.close();
                    new Alert(Alert.AlertType.INFORMATION,"Employer has been deleted successfully!").show();
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
        employerPane.setBackground(new Background(bgImage));
        employerTitleComboBox.getItems().add(new Label("Mr.").getText());
        employerTitleComboBox.getItems().add(new Label("Mrs.").getText());
        employerTitleComboBox.setPromptText("Select");
        employerIdtxt.setEditable(false);
        generateId();
        employerIDCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Id"));
        titleCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Title"));
        nameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Name"));
        NICCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("NIC"));
        DOBCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("DOB"));
        addressCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Address"));
        contactCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Contact"));
        BBANCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("BBAN"));
        bankbranchCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("bank_branch"));
        OptionCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("Option"));
        generateId();
        loadTable();

        employerTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) ->{
            if (newValue!=null){
                setData(newValue);
            }
        });

        searchTxt.textProperty().addListener((observableValue, oldValue, newValue) -> employerTable.setPredicate(employerTMTreeItem -> employerTMTreeItem.getValue().getId().contains(newValue) ||
                employerTMTreeItem.getValue().getName().contains(newValue)));
    }

    private void setData(TreeItem<EmployersTM> value) {
        employerIdtxt.setText(value.getValue().getId());
        employerTitleComboBox.setValue(value.getValue().getTitle());
        employerNameTxt.setText(value.getValue().getName());
        employerNICtxt.setText(value.getValue().getNIC());
        datePicker.setValue(LocalDate.parse(value.getValue().getDOB()));
        employerAddresstxt.setText(value.getValue().getAddress());
        employerContacttxt.setText(value.getValue().getContact());
        employerBBANtxt.setText(value.getValue().getBBAN());
        employerBankBranchtxt.setText(value.getValue().getBank_branch());
    }


    private void generateId() {
        Session session = HibernateUtilEmployer.getSession();
        String qry = "FROM Employers ORDER BY id DESC";
        Query query = session.createQuery(qry);
        List<Employers> list = query.list();
        if ((long) list.size() > 0){
            int num = Integer.parseInt(list.get(0).getId().split("EMP-")[1]);
            num++;
            employerIdtxt.setText(String.format("EMP-%06d",num));
        }else {
            employerIdtxt.setText("EMP-000001");
        }
    }

}
