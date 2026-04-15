package umu.pds.gui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import umu.pds.gui.App;
import umu.pds.gui.services.GlobalState;
import umu.pds.gui.services.api.AuthService;

public class LoginController {

    @FXML private TextField emailField;
    private final AuthService authService = new AuthService();

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        if (email == null || email.trim().isEmpty()) {
            email = "demo@pdsarchitect.com";
        }
        
        final String finalEmail = email;
        System.out.println("Solicitando código para email: " + finalEmail + "...");
        
        // Llamada asíncrona para no bloquear el hilo de la UI
        new Thread(() -> {
            try {
                boolean exito = authService.solicitarCodigo(finalEmail);
                Platform.runLater(() -> {
                    if (exito) {
                        try {
                            GlobalState.getInstance().setUserEmail(finalEmail);
                            App.setRoot("Verificar");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error de Autenticación");
                        alert.setContentText("No se pudo solicitar el código de acceso (servidor devolvió falso).");
                        alert.show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error de Conexión");
                    alert.setContentText("Error contactando al servidor. Asegúrate de que el backend esté en ejecución.");
                    alert.show();
                });
            }
        }).start();
    }
}