package umu.pds.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import umu.pds.dto.TableroResponseDTO;
import umu.pds.gui.services.GlobalState;
import umu.pds.gui.services.api.TableroService;

public class UnirseTableroController {

    @FXML private TextField linkField;

    private TableroService tableroService;

    @FXML
    public void initialize() {
        tableroService = new TableroService();
    }

    @FXML
    private void handleJoin() {
        String link = linkField.getText();
        if (link == null || link.trim().isEmpty()) {
            showAlert("Enlace requerido", "Por favor, pega el enlace del tablero.");
            return;
        }

        // extraer el ID del tablero del enlace.
        String boardId = extractBoardId(link.trim());
        if (boardId == null) {
            showAlert("Enlace inválido", "No se pudo reconocer el identificador del tablero en el enlace.");
            return;
        }

        try {
            String currentUserEmail = GlobalState.getInstance().getUserEmail();
            
            // Intentar unirse al tablero 
            try {
                tableroService.acceptInvitation(boardId, currentUserEmail);
            } catch (Exception e) {
                // si falla puede ser porque ya es miembro o porque no esta invitado.
                // Intentamos obtener el tablero igual ver si ya tiene acceso.
                System.out.println("Aviso al unirse: " + e.getMessage());
            }

            // verificar si el usuario tiene acceso al tablero llamando al endpoint de detalle
            TableroResponseDTO tablero = tableroService.getTableroById(boardId, currentUserEmail);
            if (tablero != null) {
                GlobalState.getInstance().setCurrentBoardId(boardId);
                MainLayoutController.getInstance().loadCenterView("BoardWorkspace");
            }
        } catch (Exception e) {
            String errorMsg = e.getMessage() != null ? e.getMessage() : "";
            if (errorMsg.contains("403") || errorMsg.contains("401")) {
                showAlert("Acceso Denegado", "No tienes permisos para acceder a este tablero. Necesitas recibir una invitación.");
            } else if (errorMsg.contains("404")) {
                showAlert("Tablero no encontrado", "El enlace proporcionado no es válido o el tablero ha sido eliminado.");
            } else {
                showAlert("Error al unirse", "No se pudo acceder al tablero. Verifica el enlace e inténtalo de nuevo.");
            }
        }
    }

    private String extractBoardId(String link) {
        String[] parts = link.split("/");
        if (parts.length > 0) {
            return parts[parts.length - 1]; 
        }
        return null;
    }

    @FXML
    private void handleClose() {
        MainLayoutController.getInstance().loadCenterView("Dashboard");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
