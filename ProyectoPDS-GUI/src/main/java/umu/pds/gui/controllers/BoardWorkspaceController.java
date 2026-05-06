package umu.pds.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import umu.pds.gui.services.api.TableroService;
import umu.pds.dto.TableroResponseDTO;
import umu.pds.gui.services.GlobalState;

import java.util.Map;

public class BoardWorkspaceController {

    @FXML
    private StackPane boardContentArea;
    @FXML
    private javafx.scene.control.Label boardTitleLabel;
    @FXML
    private javafx.scene.control.Label estadoBadge;
    @FXML
    private javafx.scene.control.Button btnReglas;
    @FXML
    private javafx.scene.control.Button btnCongelar;

    private TableroService tableroService = new TableroService();

    @FXML
    public void initialize() {
        System.out.println("BoardWorkspace cargado. Mostrando Kanban por defecto...");
        updateHeader();
        loadKanban();
    }

    private void updateHeader() {
        try {
            String boardId = GlobalState.getInstance().getCurrentBoardId();
            if (boardId != null) {
                TableroResponseDTO board = tableroService.getTableroById(boardId, GlobalState.getInstance().getUserEmail());
                boardTitleLabel.setText("Tablero: " + board.nombre());

                if ("CONGELADO".equalsIgnoreCase(board.estado())) {
                    estadoBadge.setText("CONGELADO");
                    estadoBadge.setStyle(
                            "-fx-background-color: #f8d7da; -fx-padding: 4 12; -fx-background-radius: 12; -fx-text-fill: #721c24; -fx-font-size: 11px; -fx-font-weight: bold;");
                    if (btnCongelar != null) btnCongelar.setText("Descongelar");
                } else {
                    estadoBadge.setText("ACTIVO");
                    estadoBadge.setStyle(
                            "-fx-background-color: #d4edda; -fx-padding: 4 12; -fx-background-radius: 12; -fx-text-fill: #155724; -fx-font-size: 11px; -fx-font-weight: bold;");
                    if (btnCongelar != null) btnCongelar.setText("Congelar");
                }
                
                String currentUserEmail = GlobalState.getInstance().getUserEmail().toLowerCase();
                boolean isPropietario = board.emailCreador() != null && board.emailCreador().toLowerCase().equals(currentUserEmail);
                
                String role = "LECTOR";
                if (isPropietario) {
                    role = "PROPIETARIO";
                } else if (board.miembros() != null) {
                    for (Map.Entry<String, String> entry : board.miembros().entrySet()) {
                        if (entry.getKey().toLowerCase().equals(currentUserEmail)) {
                            role = entry.getValue() != null ? entry.getValue().toUpperCase() : "LECTOR";
                            break;
                        }
                    }
                }
                GlobalState.getInstance().setCurrentUserRole(role);
                System.out.println("Role set for user " + currentUserEmail + " is: " + role);
                
                if ("LECTOR".equals(role) && btnReglas != null) {
                    btnReglas.setVisible(false);
                    btnReglas.setManaged(false);
                }
                
                boolean hasElevatedPermissions = "PROPIETARIO".equals(role) || "ESCRITOR".equals(role);
                if (btnCongelar != null) {
                    btnCongelar.setVisible(hasElevatedPermissions);
                    btnCongelar.setManaged(hasElevatedPermissions);
                }
            }
        } catch (Exception e) {
            System.err.println("Error actualizando cabecera de tablero: " + e.getMessage());
        }
    }

    @FXML
    private void loadKanban() {
        System.out.println("Cargando vista Kanban...");
        loadView("TableroAvanzado");
    }

    @FXML
    private void loadMembers() {
        System.out.println("Cargando vista de Miembros y Permisos...");
        loadView("PermisosTablero");
    }

    @FXML
    private void loadTags() {
        System.out.println("Cargando vista de Etiquetas...");
        loadView("GestorEtiquetas");
    }

    @FXML
    private void loadRules() {
        System.out.println("Cargando vista de Configuración y Reglas...");
        loadView("ConfiguracionTablero");
    }

    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/umu/pds/gui/views/" + fxmlFile + ".fxml"));
            Parent view = loader.load();
            boardContentArea.getChildren().setAll(view);
        } catch (Exception e) {
            System.err.println("Error al cargar la sub-vista del tablero: " + fxmlFile);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleToggleFreeze() {
        try {
            String boardId = GlobalState.getInstance().getCurrentBoardId();
            if (boardId == null) return;
            
            if ("Congelar".equals(btnCongelar.getText())) {
                tableroService.congelarTablero(boardId);
                System.out.println("Tablero congelado.");
            } else {
                tableroService.descongelarTablero(boardId);
                System.out.println("Tablero descongelado.");
            }
            updateHeader(); // refrescar estado y recargar vista
            loadKanban();
        } catch (Exception e) {
            System.err.println("Error al alternar estado del tablero: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
