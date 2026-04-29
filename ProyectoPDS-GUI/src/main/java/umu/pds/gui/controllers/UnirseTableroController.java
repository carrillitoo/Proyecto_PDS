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
            // Verificar si el usuario tiene acceso al tablero llamando al endpoint de detalle
            umu.pds.dto.TableroResponseDTO tablero = tableroService.getTableroById(boardId);
            if (tablero != null) {
                // Hay acceso. Lo abrimos.
                GlobalState.getInstance().setCurrentBoardId(boardId);
                MainLayoutController.getInstance().loadCenterView("BoardWorkspace");
            }
        } catch (Exception e) {
            showAlert("Acceso denegado o Error", "No se pudo acceder al tablero. ¿Seguro que tienes permisos?\n" + e.getMessage());
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
