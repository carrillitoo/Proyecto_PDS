package umu.pds.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

public class CreateListController {

    @FXML private TextField listNameField;
    @FXML private Slider capacitySlider;

    @FXML
    public void initialize() {
        System.out.println("Modal de Crear Lista cargado.");
    }
    
    @FXML
    private void handleCreateList() {
        System.out.println("Creando lista: " + listNameField.getText() + " con límite " + capacitySlider.getValue());
    }
}