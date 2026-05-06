package umu.pds.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import umu.pds.dto.TableroResponseDTO;
import umu.pds.gui.services.api.TableroService;
import umu.pds.gui.services.GlobalState;

public class CreateBoardController {

    @FXML
    private TextField boardNameField;
    @FXML
    private Button createButton;

    @FXML
    private void handleCreate(ActionEvent event) {
        String boardName = boardNameField.getText();
        if (boardName == null || boardName.trim().isEmpty()) {
            showAlert("Nombre Obligatorio", "El nombre del tablero no puede estar vacío");
            return;
        }

        System.out.println("Creando tablero: " + boardName);

        try {
            TableroService tableroService = new TableroService();
            String currentUserEmail = GlobalState.getInstance().getUserEmail();
            if (currentUserEmail == null || currentUserEmail.isBlank()) {
                showAlert("Sesión no válida", "Error: no hay email de sesión válido para crear el tablero");
                return;
            }

            // llama a base de datos real
            TableroResponseDTO createdBoard = tableroService.createBoard(boardName, currentUserEmail);

            System.out.println("Tablero creado exitosamente en API con ID: " + createdBoard.id());
            // guardamos ID del tablero recien creado 
            GlobalState.getInstance().setCurrentBoardId(createdBoard.id());
            // despues de crear volver al dashboard
            MainLayoutController.getInstance().loadCenterView("Dashboard");

        } catch (Exception e) {
            System.err.println("Error al crear el tablero en el API: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClose() {
        System.out.println("Cerrando formulario de creación de tablero...");
        MainLayoutController.getInstance().loadCenterView("Dashboard");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
}