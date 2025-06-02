package com.example.desktopproj;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.util.Objects;

public class MainLayoutController {
    @FXML
    private AnchorPane contentPane;

    private void setContent(String fxmlPath) {
        try {
            Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            contentPane.getChildren().setAll(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDashboard() {
        setContent("dashboard.fxml");
    }

    @FXML
    private void handleTable() {
        setContent("expense-table.fxml");
    }

    @FXML
    private void initialize() {
        handleDashboard();
    }
}
