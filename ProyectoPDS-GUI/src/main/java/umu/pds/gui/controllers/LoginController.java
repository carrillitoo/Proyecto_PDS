package umu.pds.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import umu.pds.gui.App;
import java.io.IOException;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private Label statusLabel;
    
    // MODO PROTOTIPO: No instanciamos el AuthService real aún

    /*@FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        
        // Si quieres, pon una pequeña validación visual
        if (email.isEmpty()) {
            System.out.println("El email está vacío, pero pasamos igual por ser prototipo.");
            email = "demo@pdsarchitect.com"; // Email por defecto para el prototipo
        }

        try {
            System.out.println("Login puenteado. Pasando a Verificar...");
            
            // 1. Cargamos la pantalla de verificación
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/umu/pds/gui/views/Verificar.fxml"));
            Parent root = loader.load();

            // 2. Le pasamos el email al controlador de verificación
            ValidationController valController = loader.getController();
            valController.setSessionData(email);

            // 3. Cambiamos la vista
            App.setRoot(root);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    
    @FXML
    private void handleLogin() {
        try {
            System.out.println("Login puenteado...");
            App.setRoot("Verificar"); // Salta a la pantalla de código
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
}