package umu.pds.gui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import umu.pds.gui.App;
import umu.pds.gui.services.GlobalState;
import umu.pds.gui.services.api.AuthService;

public class VerifyController {

    @FXML private TextField digit1;
    @FXML private TextField digit2;
    @FXML private TextField digit3;
    @FXML private TextField digit4;
    @FXML private TextField digit5;
    @FXML private TextField digit6;

    private final AuthService authService = new AuthService();

    @FXML
    private void handleVerify() {
        String userEmail = GlobalState.getInstance().getUserEmail();
        if (userEmail == null) {
            userEmail = "demo@pdsarchitect.com";
        }
        
        String code = "";
        if (digit1 != null) code += digit1.getText();
        if (digit2 != null) code += digit2.getText();
        if (digit3 != null) code += digit3.getText();
        if (digit4 != null) code += digit4.getText();
        if (digit5 != null) code += digit5.getText();
        if (digit6 != null) code += digit6.getText();

        if (code.length() < 6) {
            code = "123456"; // Fallback por si acaso
        }

        System.out.println("Validando código '" + code + "' para el email: " + userEmail);
        
        final String finalEmail = userEmail;
        final String finalCode = code;

        new Thread(() -> {
            try {
                boolean exito = authService.validarCodigo(finalEmail, finalCode);
                Platform.runLater(() -> {
                    if (exito) {
                        try {
                            App.setRoot("MainLayout");
                        } catch (Exception e) {
                            System.err.println("Error al cargar el MainLayout:");
                            e.printStackTrace();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Código Incorrecto");
                        alert.setContentText("El código ingresado es incorrecto o no coincide con el email.");
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
    private void handleResendCode() {
        String userEmail = GlobalState.getInstance().getUserEmail();
        System.out.println("Reenviando código de seguridad al email: " + userEmail);
        new Thread(() -> {
            try {
                authService.solicitarCodigo(userEmail);
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Código reenviado");
                    alert.setContentText("Se ha vuelto a enviar el código a tu correo.");
                    alert.show();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    private void handleBackToLogin() {
        System.out.println("Volviendo al inicio de sesión...");
        try {
            GlobalState.getInstance().setUserEmail(null);
            App.setRoot("Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}