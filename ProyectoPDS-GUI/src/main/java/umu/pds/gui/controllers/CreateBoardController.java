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
        System.out.println("Tablero creado: " + boardName);
        // TODO: Conectar con backend - LLamar a BoardService.createBoard(boardName, email)
        // Tras crear, volver al Dashboard
        MainLayoutController.getInstance().loadCenterView("Dashboard");
    }

    @FXML
    private void handleClose() {
        System.out.println("Cerrando formulario de creación de tablero...");
        MainLayoutController.getInstance().loadCenterView("Dashboard");
    }
}