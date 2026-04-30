package umu.pds.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import umu.pds.gui.services.GlobalState;
import umu.pds.gui.services.api.UsuarioService;

public class EditarPerfilController {

    @FXML private TextField nombreField;

    private UsuarioService usuarioService;

    @FXML
    public void initialize() {
        usuarioService = new UsuarioService();
        
        // Cargar datos actuales
        String nombre = GlobalState.getInstance().getUserName();
        if (nombre != null) {
            nombreField.setText(nombre);
        }
    }

    @FXML
    private void handleSave() {
        String nuevoNombre = nombreField.getText();
        String email = GlobalState.getInstance().getUserEmail();

        if (email == null) return;

        try {
            // Guardar nombre
            if (nuevoNombre != null && !nuevoNombre.trim().isEmpty() && !nuevoNombre.equals(GlobalState.getInstance().getUserName())) {
                umu.pds.dto.UsuarioResponseDTO actualizado = usuarioService.updatePerfil(email, nuevoNombre.trim());
                GlobalState.getInstance().setUserName(actualizado.nombre());
            }

            // Recargar UI principal
            MainLayoutController.getInstance().initialize(); // refresca navbar

            // Volver
            handleClose();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo guardar el perfil: " + e.getMessage());
        }
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
