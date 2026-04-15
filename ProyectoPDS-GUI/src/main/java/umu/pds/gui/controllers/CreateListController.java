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
        System.out.println("Creando lista: " + listNameField.getText() + " con límite " + (int) capacitySlider.getValue());
        // TODO: Conectar con backend - LLamar a la API para crear una nueva lista en el tablero actual
        // Tras crear, volver al tablero
        MainLayoutController.getInstance().loadCenterView("BoardWorkspace");
    }

    @FXML
    private void handleClose() {
        System.out.println("Cerrando formulario de creación de lista...");
        MainLayoutController.getInstance().loadCenterView("BoardWorkspace");
    }
}