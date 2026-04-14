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
}