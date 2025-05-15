package com.example.desktopproj;

import com.example.desktopproj.classes.Database;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("expense-table.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Finance Management");
        stage.setScene(scene);
        stage.getIcons().add(new Image(Application.class.getResourceAsStream("/com/example/desktopproj/icon/img.png")));

        if (!Database.isOK()) {
            return;
        }
        stage.show();
    }
}