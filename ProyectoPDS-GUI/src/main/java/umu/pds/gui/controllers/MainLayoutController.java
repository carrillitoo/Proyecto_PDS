package umu.pds.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import umu.pds.gui.App;

public class MainLayoutController {

    @FXML private StackPane contentArea;
    
    // Instancia estática para poder cambiar vistas desde otros controladores
    private static MainLayoutController instance;

    @FXML
    public void initialize() {
        instance = this;
        goDashboard(); // Al cargar el layout, cargamos el Dashboard en el centro por defecto
    }

    public static MainLayoutController getInstance() {
        return instance;
    }

    // Método maestro para cambiar la vista central
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
    @FXML private void goBoard() { loadCenterView("TableroAvanzado"); }
    @FXML private void goPermissions() { loadCenterView("PermisosTablero"); }
    @FXML private void goCompaction() { loadCenterView("ConfiguracionTablero"); }
    @FXML private void goTemplates() { loadCenterView("Plantillas"); }

    // --- ¡AQUÍ ESTÁ EL MÉTODO QUE FALTABA PARA EL BOTÓN CREAR! ---
    @FXML
    private void handleOpenCreateMenu() {
        System.out.println("Botón Crear pulsado. Abriendo pantalla de creación...");
        loadCenterView("CrearTablero"); 
    }

    @FXML
    private void logout() {
        try {
            App.setRoot("Login"); // Vuelve a la pantalla completa de Login
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}