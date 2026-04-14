package umu.pds.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class BoardViewController {

    @FXML private VBox todoContainer;

    @FXML
    public void initialize() {
        System.out.println("Inicializando Vista de Tablero Simple...");
    }
}