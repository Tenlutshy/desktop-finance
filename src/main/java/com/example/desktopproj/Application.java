package com.example.desktopproj;

import com.example.desktopproj.classes.Database;
import com.example.desktopproj.classes.LoggerUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Objects;

public class Application extends javafx.application.Application {
    private static Logger logger;

    public static void main(String[] args) {
        logger = LoggerUtil.getLogger(Application.class);
        logger.info("Démarrage de l'application");
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        logger.info("Initialisation de l'interface utilisateur");
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("main-layout.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1080, 720);
        stage.setTitle("Finance Management");
        stage.setScene(scene);

        try {
            stage.getIcons().add(new Image(Objects.requireNonNull(Application.class.getResourceAsStream("/com/example/desktopproj/icon/img.png"))));
        } catch (Exception e) {
            logger.warn("Impossible de charger l'icône de l'application: " + e.getMessage());
        }

        if (!Database.isOK()) {
            logger.error("Erreur d'initialisation de la base de données. L'application ne peut pas démarrer.");
            return;
        }

        logger.info("Application démarrée avec succès");
        stage.show();
    }
}