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

    // Método para recibir el email desde el controlador anterior
    public void setSessionData(String email) {
        this.userEmail = email;
    }

    @FXML
    private void handleVerify() {
        String codigo = codeField.getText();
        
        try {
            boolean esValido = authService.validarCodigo(userEmail, codigo);
            
            if (esValido) {
                statusLabel.setStyle("-fx-text-fill: green;");
                statusLabel.setText("¡Acceso concedido! Entrando...");
                // Aquí iríamos a la pantalla principal del Trello (próximamente)
            } else {
                statusLabel.setStyle("-fx-text-fill: red;");
                statusLabel.setText("Código incorrecto. Inténtalo de nuevo.");
            }
        } catch (Exception e) {
            statusLabel.setText("Error de conexión.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() throws Exception {
        App.setRoot("login");
    }
}