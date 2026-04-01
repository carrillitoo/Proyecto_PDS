package umu.pds.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import umu.pds.gui.App;

public class DashboardController {

    @FXML private Label userLabel;
    @FXML private FlowPane boardsContainer;

    private String currentUserEmail;

    /**
     * Se llama desde el ValidationController al entrar con éxito
     */
    public void setUserInfo(String email) {
        this.currentUserEmail = email;
        userLabel.setText(email);
        loadBoards(); // Cargaremos los tableros del usuario
    }

    /**
     * Simulación de carga de tableros (Luego vendrá del Backend)
     */
    private void loadBoards() {
        // Por ahora lo dejamos limpio o añadimos uno de prueba
        // boardsContainer.getChildren().clear(); 
    }

    /**
     * Acción para el botón "+ Crear nuevo tablero"
     */
    @FXML
    private void handleNewBoard() {
        System.out.println("Abriendo diálogo para nuevo tablero...");
        // TODO: Aquí abriremos una ventanita (Stage) para pedir el nombre
    }

    /**
     * Vuelve al Login
     */
    @FXML
    private void handleLogout() {
        try {
            App.setRoot("login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}