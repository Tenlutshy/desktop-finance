package com.example.desktopproj.classes;

import org.sqlite.JDBC;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {

    /**
     * Location of database
     */
    private static final String location = Database.getDatabaseLocation() + "database.db";

    /**
     * Currently only table needed
     */
    private static final String requiredTable = "Expense";

    public static boolean isOK() {
        if (!checkDrivers()) return false; //driver errors

        if (!checkConnection()) return false; //can't connect to db

        return createTableIfNotExists(); //tables didn't exist
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
            dbDir.mkdirs();
        }
        
        return location + File.separator;
    }

    private static boolean checkDrivers() {
        try {
            Class.forName("org.sqlite.JDBC");
            DriverManager.registerDriver(new JDBC());
            return true;
        } catch (ClassNotFoundException | SQLException classNotFoundException) {
            Logger.getAnonymousLogger().log(Level.SEVERE, LocalDateTime.now() + ": Could not start SQLite Drivers");
            return false;
        }
    }

    private static boolean checkConnection() {
        try (Connection connection = connect()) {
            return connection != null;
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, LocalDateTime.now() + ": Could not connect to database");
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
            PreparedStatement statement = connection.prepareStatement(createTables);
            statement.executeUpdate();
            return true;
        } catch (SQLException exception) {
            Logger.getAnonymousLogger().log(Level.SEVERE, LocalDateTime.now() + ": Could not find tables in database");
            return false;
        }
    }

    protected static Connection connect() {
        String dbPrefix = "jdbc:sqlite:";
        Connection connection;
        try {
            connection = DriverManager.getConnection(dbPrefix + location);
        } catch (SQLException exception) {
            Logger.getAnonymousLogger().log(Level.SEVERE,
                    LocalDateTime.now() + ": Could not connect to SQLite DB at " +
                            location);
            return null;
        }
        return connection;
    }

}
