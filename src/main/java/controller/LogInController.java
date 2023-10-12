package controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;


public class LogInController {
    @FXML
    private StackPane PaneContainer;

    @FXML
    void initialize() {
        PaneContainer.setAlignment(Pos.CENTER);
    }
}
