module hu.petrik.filmdb {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.google.gson;

    opens hu.petrik.filmdb to javafx.fxml, com.google.gson;
    exports hu.petrik.filmdb;
    exports hu.petrik.filmdb.controllers;
    opens hu.petrik.filmdb.controllers to javafx.fxml;
}