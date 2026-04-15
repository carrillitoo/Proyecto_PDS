package umu.pds.gui.controllers;

import javafx.fxml.FXML;

public class CompactionController {

    @FXML private javafx.scene.control.Button activateBtn;
    @FXML private javafx.scene.control.Button saveBtn;
    @FXML private javafx.scene.control.Button freezeBtn;

    private boolean isFrozen = false;
    private boolean isActivated = false;

    @FXML
    public void initialize() {
        System.out.println("Cargando reglas de compactación del tablero...");
    }

    @FXML
    private void handleActivateCompaction() {
        System.out.println("Alternando compactación automática...");
        isActivated = !isActivated;
        if (isActivated) {
            activateBtn.setText("Compactación Activada");
            activateBtn.setStyle("-fx-background-color: #5aac44; -fx-text-fill: white; -fx-font-weight: bold;");
        } else {
            activateBtn.setText("Activar Compactacion");
            activateBtn.setStyle(""); 
        }
    }

    @FXML
    private void handleSaveConfig() {
        System.out.println("Guardando configuración de compactación...");
        // TODO: Conectar con backend - Llamar a BoardService.compactBoard(id, dias)
        saveBtn.setText("¡Configuración Guardada!");
        saveBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #5aac44; -fx-font-weight: bold;");
    }

    @FXML
    private void handleToggleFreeze() {
        System.out.println("Alternando estado congelar/descongelar del tablero...");
        // TODO: Conectar con backend - Llamar a BoardService.freezeBoard(id) o unfreezeBoard(id) dependiendo de isFrozen
        isFrozen = !isFrozen;
        if (isFrozen) {
            freezeBtn.setText("Descongelar Tablero");
            freezeBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #0079bf; -fx-text-fill: #0079bf;");
        } else {
            freezeBtn.setText("Congelar Tablero");
            freezeBtn.setStyle("");
        }
    }
}