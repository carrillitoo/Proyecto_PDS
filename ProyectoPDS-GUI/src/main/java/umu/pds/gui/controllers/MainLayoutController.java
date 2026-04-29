package umu.pds.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import umu.pds.gui.App;
import umu.pds.gui.services.GlobalState;

public class MainLayoutController {

    @FXML private StackPane contentArea;
    @FXML private Circle avatarCircle;
    @FXML private Label userNameLabel;
    
    private static MainLayoutController instance;

    @FXML
    public void initialize() {
        instance = this;
        // Actualizar nombre de usuario si está disponible
        String userName = GlobalState.getInstance().getUserName();
        if (userName != null && !userName.isBlank()) {
            userNameLabel.setText(userName);
        } else {
            String email = GlobalState.getInstance().getUserEmail();
            if (email != null && email.contains("@")) {
                userNameLabel.setText(email.substring(0, email.indexOf("@")));
            }
        }
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
    @FXML private void goBoard() { loadCenterView("Dashboard"); }
    @FXML private void goEtiquetas() { loadCenterView("GestorEtiquetas"); }
    @FXML private void goHistory() { loadCenterView("HistorialActividad"); }

    @FXML
    private void handleOpenCreateMenu() {
        System.out.println("Botón Crear pulsado. Abriendo pantalla de creación...");
        loadCenterView("CrearTablero"); 
    }

    @FXML
    private void handleOpenJoinDialog() {
        System.out.println("Abriendo dialog para unirse a un tablero...");
        loadCenterView("UnirseTableroDialog");
    }

    @FXML
    private void handleOpenProfileDialog() {
        System.out.println("Abriendo dialog de edición de perfil...");
        loadCenterView("EditarPerfilDialog");
    }

    @FXML
    private void logout() {
        try {
            GlobalState.getInstance().setUserEmail(null);
            GlobalState.getInstance().setUserName(null);
            GlobalState.getInstance().setUserPhotoUrl(null);
            GlobalState.getInstance().setCurrentBoardId(null);
            App.setRoot("Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}