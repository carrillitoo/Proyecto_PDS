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
    public void initialize() {
        setupTextFieldAutoAdvance(digit1, null, digit2);
        setupTextFieldAutoAdvance(digit2, digit1, digit3);
        setupTextFieldAutoAdvance(digit3, digit2, digit4);
        setupTextFieldAutoAdvance(digit4, digit3, digit5);
        setupTextFieldAutoAdvance(digit5, digit4, digit6);
        setupTextFieldAutoAdvance(digit6, digit5, null);
    }

    private void setupTextFieldAutoAdvance(TextField current, TextField previous, TextField next) {
        if (current == null) return;
        
        current.textProperty().addListener((observable, oldValue, newValue) -> {
            // Asegurarnos de que solo se introduzcan números
            if (newValue != null && !newValue.matches("\\d*")) {
                current.setText(newValue.replaceAll("[^\\d]", ""));
                return;
            }
            
            // Limitar la longitud a 1 caracter para no tener que estar tabulando
            if (newValue != null && newValue.length() > 0) {
                if (newValue.length() > 1) {
                    current.setText(newValue.substring(0, 1));
                }
                if (next != null) {
                    next.requestFocus();
                } else if (current == digit6) {
                    // Si es el último digito, verificar si todos están llenos
                    checkAndSubmit();
                }
            }
        });

        current.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case BACK_SPACE:
                    if (current.getText().isEmpty() && previous != null) {
                        previous.requestFocus();
                    }
                    break;
                case LEFT:
                    if (previous != null) {
                        previous.requestFocus();
                    }
                    break;
                case RIGHT:
                    if (next != null) {
                        next.requestFocus();
                    }
                    break;
                default:
                    break;
            }
        });
    }

    private void checkAndSubmit() {
        if (digit1.getText().length() == 1 && digit2.getText().length() == 1 && 
            digit3.getText().length() == 1 && digit4.getText().length() == 1 && 
            digit5.getText().length() == 1 && digit6.getText().length() == 1) {
            handleVerify();
        }
    }

    @FXML
    private void handleVerify() {
        String userEmail = GlobalState.getInstance().getUserEmail();
        if (userEmail == null) {
            userEmail = "demo@pdsarchitect.com";
        }
        
        String code = "";
        if (digit1 != null) code += digit1.getText().trim();
        if (digit2 != null) code += digit2.getText().trim();
        if (digit3 != null) code += digit3.getText().trim();
        if (digit4 != null) code += digit4.getText().trim();
        if (digit5 != null) code += digit5.getText().trim();
        if (digit6 != null) code += digit6.getText().trim();

        if (code.length() < 6) {
            showAlert("Código Incompleto", "El código debe tener 6 dígitos.");
            return;
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

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
}