package controller;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class LogInController {
    @FXML
    private ImageView bgView;

    @FXML
    private AnchorPane LogInPane;

    @FXML
    void initialize() {
        LogInPane.widthProperty().addListener(observable -> {
            bgView.setFitWidth(LogInPane.getWidth());
            bgView.setFitHeight(LogInPane.getHeight());
        });
    }
}
