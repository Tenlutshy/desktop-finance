package com.example.desktopproj.classes;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LoggerUtil {

    static {
        try {
            // Déterminer le répertoire approprié en fonction de l'OS
            Path logDir = getAppropriateLogDirectory();
            if (!Files.exists(logDir)) {
                Files.createDirectories(logDir);
            }
            // Définir la propriété système pour log4j
            System.setProperty("log", logDir.toString());
        } catch (IOException e) {
            System.err.println("Erreur lors de la création du répertoire de logs: " + e.getMessage());
        }
    }

    public static Path getAppropriateLogDirectory() {
        String os = System.getProperty("os.name").toLowerCase();
        String userHome = System.getProperty("user.home");

        if (os.contains("win")) {
            // Windows: AppData/Local/Finance/logs
            return Paths.get(System.getenv("LOCALAPPDATA"), "Finance", "logs");
        } else if (os.contains("mac")) {
            // macOS: ~/Library/Logs/Finance
            return Paths.get(userHome, "Library", "Logs", "Finance");
        } else {
            // Linux et autres: ~/.cache/finance/logs ou ~/.finance/logs
            return Paths.get(userHome, ".cache", "finance", "logs");
        }
    }


    public static Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }
}