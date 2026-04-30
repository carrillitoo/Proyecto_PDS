package umu.pds.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
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

        // Extraer el ID del tablero del enlace.
        // Asumimos formato: http://trello-pds.com/board/UUID
        String boardId = extractBoardId(link.trim());
        if (boardId == null) {
            showAlert("Enlace inválido", "No se pudo reconocer el identificador del tablero en el enlace.");
            return;
        }

        try {
            String currentUserEmail = GlobalState.getInstance().getUserEmail();
            
            // Intentar unirse al tablero (aceptar invitación)
            try {
                tableroService.acceptInvitation(boardId, currentUserEmail);
            } catch (Exception e) {
                // Si falla puede ser porque ya es miembro o porque no está invitado.
                // Intentamos obtener el tablero de todos modos para ver si ya tiene acceso.
                System.out.println("Aviso al unirse: " + e.getMessage());
            }

            // Verificar si el usuario tiene acceso al tablero llamando al endpoint de detalle
            umu.pds.dto.TableroResponseDTO tablero = tableroService.getTableroById(boardId, currentUserEmail);
            if (tablero != null) {
                // Hay acceso. Lo abrimos.
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
            return parts[parts.length - 1]; // Devuelve el ultimo fragmento (que asuminos es el UUID)
        }
        return null;
    }

    @FXML
    private void handleClose() {
        // Volvemos a la vista anterior, normalmente el dashboard
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
