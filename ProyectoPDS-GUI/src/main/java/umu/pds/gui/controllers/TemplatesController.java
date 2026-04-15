package umu.pds.gui.controllers;

import javafx.fxml.FXML;

public class TemplatesController {

    @FXML private javafx.scene.control.Button uploadBtn;
    @FXML private javafx.scene.control.Button createBtn;

    @FXML
    public void initialize() {
        System.out.println("Cargando galería de plantillas YAML...");
    }

    @FXML
    private void handleUploadYaml() {
        System.out.println("Subiendo yaml...");
        uploadBtn.setText("Archivo Subido ✓");
        uploadBtn.setStyle("-fx-background-color: #5aac44;");
    }

    @FXML
    private void handleCreateFromYaml() {
        System.out.println("Creando tablero desde YAML...");
        // TODO: Conectar con backend - Enviar el contenido del YAML a la API para generar el tablero
        createBtn.setText("¡Tablero Creado! 🎉");
    }
}