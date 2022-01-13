package hu.petrik.filmdb.controllers;

import hu.petrik.filmdb.Controller;
import hu.petrik.filmdb.Film;
import hu.petrik.filmdb.FilmApp;
import hu.petrik.filmdb.FilmDb;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainController extends Controller {

    @FXML
    private TableView<Film> filmTable;
    @FXML
    private TableColumn<Film, String> colCim;
    @FXML
    private TableColumn<Film, String> colKategoria;
    @FXML
    private TableColumn<Film, Integer> colHossz;
    @FXML
    private TableColumn<Film, Integer> colErtekeles;

    private FilmDb db;

    public void initialize() {
        colCim.setCellValueFactory(new PropertyValueFactory<>("cim"));
        //a tárolt objektumban egy getCim függvényt fog keresni.
        colKategoria.setCellValueFactory(new PropertyValueFactory<>("kategoria"));
        colHossz.setCellValueFactory(new PropertyValueFactory<>("hossz"));
        colErtekeles.setCellValueFactory(new PropertyValueFactory<>("ertekeles"));
        try {
            db = new FilmDb();
            List<Film> filmList = db.getFilmek();
            for (Film film : filmList) {
                filmTable.getItems().add(film);
            }
        } catch (SQLException e) {
            hibaKiir(e);
        }
    }


    @FXML
    public void onModositasButtonClick(ActionEvent actionEvent) {
        int selectedIndex = filmTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            alert("A módosításhoz előbb válasszon ki egy elemet a táblázatból");
            return;
        }
        Film modositando = filmTable.getSelectionModel().getSelectedItem();
        try {
            ModositController modositas = (ModositController) ujAblak("modosit-view.fxml", "Film módosítása", 320, 400);
            modositas.setModositando(modositando);
            modositas.getStage().setOnHiding(event -> filmTable.refresh());
            modositas.getStage().show();
        } catch (IOException e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onTorlesButtonClick(ActionEvent actionEvent) {
        int selectedIndex = filmTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            alert("A törléshez előbb válasszon ki egy elemet a táblázatból");
            return;
        }
        Film torlendoFilm = filmTable.getSelectionModel().getSelectedItem();
        if (!confirm("Biztos hogy törölni szeretné az alábbi filmet: " + torlendoFilm.getCim())) {
            return;
        }
        try {
            db.filmTorles(torlendoFilm.getId());
            alert("Sikeres törlés");
            filmListaFeltolt();
        } catch (SQLException e) {
            hibaKiir(e);
        }
    }

    @FXML
    public void onHozzadasButtonClick(ActionEvent actionEvent) {
        try {
            Controller hozzadas = ujAblak("hozzaad-view.fxml", "Film hozzáadása", 320, 400);
            hozzadas.getStage().setOnCloseRequest(event -> filmListaFeltolt());
            hozzadas.getStage().show();
        } catch (Exception e) {
            hibaKiir(e);
        }
    }

    private void filmListaFeltolt() {
        try {
            List<Film> filmList = db.getFilmek();
            filmTable.getItems().clear();
            for (Film film : filmList) {
                filmTable.getItems().add(film);
            }
        } catch (SQLException e) {
            hibaKiir(e);
        }
    }
}