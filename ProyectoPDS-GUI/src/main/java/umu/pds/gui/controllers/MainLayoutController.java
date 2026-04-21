package umu.pds.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import umu.pds.gui.App;

public class MainLayoutController {

    @FXML private StackPane contentArea;
    
    private static MainLayoutController instance;

    @FXML
    public void initialize() {
        instance = this;
        goDashboard();
    }

    public static MainLayoutController getInstance() {
        return instance;
    }

    public void loadCenterView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/umu/pds/gui/views/" + fxmlFile + ".fxml"));
            Parent view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (Exception e) {
            System.err.println("Error al cargar la vista: " + fxmlFile);
            e.printStackTrace();
        }
    }

    // --- ACCIONES DEL MENÚ LATERAL ---
    @FXML private void goDashboard() { loadCenterView("Dashboard"); }
    @FXML private void goBoard() {
        String boardId = umu.pds.gui.services.GlobalState.getInstance().getCurrentBoardId();
        if (boardId != null && !boardId.isBlank()) {
            loadCenterView("BoardWorkspace");
        } else {
            System.out.println("No hay tablero seleccionado. Redirigiendo al Dashboard.");
            loadCenterView("Dashboard");
        }
    }
    @FXML private void goPermissions() { loadCenterView("PermisosTablero"); }
    @FXML private void goCompaction() { loadCenterView("ConfiguracionTablero"); }
    @FXML private void goTemplates() { loadCenterView("Plantillas"); }
    @FXML private void goHistory() { loadCenterView("HistorialActividad"); }

    @FXML
    private void handleOpenCreateMenu() {
        System.out.println("Botón Crear pulsado. Abriendo pantalla de creación...");
        loadCenterView("CrearTablero"); 
    }

    @FXML
    private void logout() {
        try {
            App.setRoot("Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}