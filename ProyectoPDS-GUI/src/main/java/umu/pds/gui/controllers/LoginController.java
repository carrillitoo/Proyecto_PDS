package umu.pds.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import umu.pds.gui.App;
import umu.pds.gui.services.api.AuthService;

import java.io.IOException;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private Label statusLabel;
    
    private final AuthService authService = new AuthService();

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        
        if (email.isEmpty()) {
            statusLabel.setStyle("-fx-text-fill: #e74c3c;");
            statusLabel.setText("Por favor, introduce un email.");
            return;
        }

        try {
            // 1. Llamamos al Backend para solicitar el código
            boolean exito = authService.solicitarCodigo(email);
            
            if (exito) {
                // 2. Si hay éxito, cargamos la siguiente pantalla (verificar.fxml)
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/umu/pds/gui/views/verificar.fxml"));
                Parent root = loader.load();

                // 3. Obtenemos el controlador de la pantalla de verificación
                ValidationController valController = loader.getController();
                
                // 4. LE PASAMOS EL EMAIL (importante para que sepa a quién validar luego)
                valController.setSessionData(email);

                // 5. Cambiamos la vista principal
                App.setRoot(root);
                
            } else {
                statusLabel.setStyle("-fx-text-fill: #e74c3c;");
                statusLabel.setText("El servidor rechazó el email.");
            }
            
        } catch (IOException e) {
            statusLabel.setStyle("-fx-text-fill: #e74c3c;");
            statusLabel.setText("Error al cargar la siguiente pantalla.");
            e.printStackTrace();
        } catch (Exception e) {
            statusLabel.setStyle("-fx-text-fill: #e74c3c;");
            statusLabel.setText("No se pudo conectar con el servidor.");
            e.printStackTrace();
        }
    }
}