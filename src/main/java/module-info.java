module com.example.desktopproj {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires log4j;

    opens com.example.desktopproj to javafx.fxml;
    exports com.example.desktopproj;
    exports com.example.desktopproj.classes;
    opens com.example.desktopproj.classes to javafx.fxml;
}