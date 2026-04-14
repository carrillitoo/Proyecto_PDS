package umu.pds.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class CreateBoardController {

    @FXML private TextField boardNameField;
    @FXML private Button createButton;

    @FXML
    private void handleCreate(ActionEvent event) {
        String boardName = boardNameField.getText();
        System.out.println("Guardando en BD el nuevo tablero: " + boardName);
        // Lógica de BD aquí y luego cerrar el modal
    }
}