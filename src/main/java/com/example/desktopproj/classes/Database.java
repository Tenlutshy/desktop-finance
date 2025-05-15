package com.example.desktopproj.classes;

import org.apache.log4j.Logger;
import org.sqlite.JDBC;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {
    private static final Logger logger = LoggerUtil.getLogger(Database.class);
    /**
     * Location of database
     */
    private static final String location = Database.getDatabaseLocation() + "database.db";

    /**
     * Currently only table needed
     */
    private static final String requiredTable = "Expense";

    public static boolean isOK() {
        if (!checkDrivers()) {
            logger.error("Erreur avec les drivers SQLite");
            return false;
        }

        if (!checkConnection()) {
            logger.error("Impossible de se connecter à la base de données");
            return false;
        }

        if (!createTableIfNotExists()) {
            logger.error("Impossible de créer les tables nécessaires");
            return false;
        }

        logger.info("Base de données initialisée avec succès à " + location);
        return true;
    }

    private static String getDatabaseLocation() {
        String userHome = System.getProperty("user.home");
        String appName = "Finance";
        String osName = System.getProperty("os.name").toLowerCase();
        String location;
        if (osName.contains("win")) {
            location = System.getenv("APPDATA") + File.separator + appName + File.separator + "Database";
        } else if (osName.contains("mac")) {
            location = userHome + File.separator + "Library" + File.separator +
                    "Application Support" + File.separator + appName + File.separator + "Database";
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            location = userHome + File.separator + ".config" + File.separator +
                    appName + File.separator + "Database";
        } else {
            location = userHome + File.separator + "." + appName.toLowerCase() + File.separator + "Database";
        }

        File dbDir = new File(location);
        if (!dbDir.exists()) {
            logger.info("Création du répertoire de base de données: " + location);
            if (dbDir.mkdirs()) {
                logger.info("Répertoire de base de données créé avec succès");
            } else {
                logger.warn("Impossible de créer le répertoire de base de données");
            }
        }

        return location + File.separator;
    }

    private static boolean checkDrivers() {
        try {
            Class.forName("org.sqlite.JDBC");
            DriverManager.registerDriver(new JDBC());
            logger.debug("Drivers SQLite chargés avec succès");
            return true;
        } catch (ClassNotFoundException | SQLException e) {
            logger.error("Impossible de charger les drivers SQLite:" + e.getMessage());
            return false;
        }
    }

    private static boolean checkConnection() {
        try (Connection connection = connect()) {
            boolean isConnected = connection != null;
            if (isConnected) {
                logger.debug("Test de connexion à la base de données réussi");
            } else {
                logger.warn("Test de connexion à la base de données échoué");
            }
            return isConnected;
        } catch (SQLException e) {
            logger.error("Erreur lors du test de connexion à la base de données: " + e.getMessage());
            return false;
        }
    }

    private static boolean createTableIfNotExists() {
        String createTables =
                """
                         CREATE TABLE IF NOT EXISTS expense(
                              date TEXT NOT NULL,
                              logement REAL NOT NULL,
                              nourriture REAL NOT NULL,
                              sortie REAL NOT NULL,
                              transport REAL NOT NULL,
                              impot REAL NOT NULL,
                              autre REAL NOT NULL
                          );
                        """;

        try (Connection connection = Database.connect()) {
            assert connection != null;
            PreparedStatement statement = connection.prepareStatement(createTables);
            statement.executeUpdate();
            logger.info("Tables vérifiées/créées avec succès");
            return true;
        } catch (SQLException exception) {
            logger.error("Erreur lors de la création des tables: " + exception.getMessage());
            return false;
        }
    }

    protected static Connection connect() {
        String dbPrefix = "jdbc:sqlite:";
        Connection connection;
        try {
            logger.debug("Tentative de connexion à " + location);
            connection = DriverManager.getConnection(dbPrefix + location);
            return connection;
        } catch (SQLException exception) {
            logger.error("Erreur de connexion à la base de données SQLite: " + exception.getMessage());
            return null;
        }
    }
}