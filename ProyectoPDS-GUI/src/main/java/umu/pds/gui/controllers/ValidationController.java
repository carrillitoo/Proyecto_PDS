package umu.pds.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
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
                // Aquí podrías cargar la vista principal más adelante
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