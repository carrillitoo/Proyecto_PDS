package umu.pds.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class TaskDetailController {

    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;

    @FXML
    public void initialize() {
        System.out.println("Cargando detalles de la tarjeta...");
    }

    @FXML
    private void handleClose() {
        System.out.println("Cerrando detalle de tarjeta...");
        MainLayoutController.getInstance().loadCenterView("BoardWorkspace");
    }

    @FXML
    private void handleOpenLabels() {
        System.out.println("Cargando vista de Gestión de Etiquetas...");
        MainLayoutController.getInstance().loadCenterView("GestionEtiquetas");
    }
}