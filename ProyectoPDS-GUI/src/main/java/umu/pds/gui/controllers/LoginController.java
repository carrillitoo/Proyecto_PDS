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
            showAlert("Email Obligatorio", "Por favor, introduce tu correo electrónico.");
            return;
        }

        // Validación de Regex básica
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showAlert("Email Inválido", "El formato del correo electrónico no es válido.");
            return;
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

    @FXML
    private void handleRegister() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información de Registro");
        alert.setHeaderText("Autenticación Segura sin Contraseñas");
        alert.setContentText("Tu cuenta se crea automáticamente cuando accedes por primera vez. Para registrarte o iniciar sesión, simplemente introduce tu correo electrónico y haz clic en 'Entrar a mis Tableros'. Te enviaremos un código temporal a tu bandeja de entrada.");
        alert.show();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
}