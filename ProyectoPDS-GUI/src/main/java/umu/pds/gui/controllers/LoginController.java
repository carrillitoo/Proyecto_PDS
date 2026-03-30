package umu.pds.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import umu.pds.gui.services.api.AuthService;

public class LoginController {

    // Estas variables se conectan con el FXML mediante el "fx:id"
    @FXML private TextField emailField;
    @FXML private Label statusLabel;
    
    // El motor de comunicación que creamos antes
    private final AuthService authService = new AuthService();

    /**
     * Este método se ejecuta cuando pulsas el botón "Solicitar Código"
     */
    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        
        if (email.isEmpty()) {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Por favor, introduce un email.");
            return;
        }

        try {
            // Llamamos al Backend (ProyectoPDS)
            boolean exito = authService.solicitarCodigo(email);
            
            if (exito) {
                statusLabel.setStyle("-fx-text-fill: #27ae60;"); // Un verde bonito
                statusLabel.setText("¡Código enviado! Revisa la consola del Backend.");
            } else {
                statusLabel.setStyle("-fx-text-fill: red;");
                statusLabel.setText("El servidor no reconoce ese email.");
            }
        } catch (Exception e) {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Error: ¿Está el Backend encendido?");
            e.printStackTrace();
        }
    }
}