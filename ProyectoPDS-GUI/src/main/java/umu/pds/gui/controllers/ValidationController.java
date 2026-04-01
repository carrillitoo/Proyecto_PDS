package umu.pds.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import umu.pds.gui.App;
import umu.pds.gui.services.api.AuthService;

public class ValidationController {

    @FXML private TextField codeField;
    @FXML private Label statusLabel;

    private final AuthService authService = new AuthService();
    private String userEmail;

    public void setSessionData(String email) {
        this.userEmail = email;
    }

    @FXML
    private void handleVerify() {
        String codigo = codeField.getText().trim();
        
        try {
            boolean esValido = authService.validarCodigo(userEmail, codigo);
            
            if (esValido) {
                statusLabel.setStyle("-fx-text-fill: #27ae60;");
                statusLabel.setText("¡Acceso concedido! Entrando...");

                // 1. Cargamos el FXML del Dashboard
                FXMLLoader loader = new FXMLLoader(App.class.getResource("/umu/pds/gui/views/dashboard.fxml"));
                Parent root = loader.load();

                // 2. Le pasamos el email para que lo muestre arriba
                DashboardController dashController = loader.getController();
                dashController.setUserInfo(userEmail);

                // 3. ¡CAMBIAMOS DE PANTALLA!
                App.setRoot(root);
            } else {
                statusLabel.setStyle("-fx-text-fill: #e74c3c;");
                statusLabel.setText("Código incorrecto. Inténtalo de nuevo.");
            }
        } catch (Exception e) {
            statusLabel.setStyle("-fx-text-fill: #e74c3c;");
            statusLabel.setText("Error de conexión con el servidor.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() {
        try {
            App.setRoot("login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}