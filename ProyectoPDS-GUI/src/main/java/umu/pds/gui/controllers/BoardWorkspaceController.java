package umu.pds.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class BoardWorkspaceController {

    @FXML private StackPane boardContentArea;

    @FXML
    public void initialize() {
        System.out.println("BoardWorkspace cargado. Mostrando Kanban por defecto...");
        loadKanban();
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
}
