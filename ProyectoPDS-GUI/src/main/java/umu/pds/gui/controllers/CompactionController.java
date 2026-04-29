package umu.pds.gui.controllers;

import umu.pds.gui.services.GlobalState;
import umu.pds.gui.services.api.TableroService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class CompactionController {

    @FXML
    private Button activateBtn;
    @FXML
    private Button saveBtn;
    @FXML
    private Button freezeBtn;

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
        String boardId = GlobalState.getInstance().getCurrentBoardId();
        try {
            TableroService service = new TableroService();
            service.compactarTablero(boardId, 30);
            saveBtn.setText("¡Configuración Guardada!");
            saveBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #5aac44; -fx-font-weight: bold;");
        } catch (Exception e) {
            System.err.println("Error config: " + e.getMessage());
        }
    }

    @FXML
    private void handleToggleFreeze() {
        System.out.println("Alternando estado congelar/descongelar del tablero...");
        String boardId = GlobalState.getInstance().getCurrentBoardId();
        try {
            TableroService service = new TableroService();
            if (!isFrozen) {
                service.congelarTablero(boardId);
                isFrozen = true;
                freezeBtn.setText("Descongelar Tablero");
                freezeBtn.setStyle(
                        "-fx-background-color: transparent; -fx-border-color: #0079bf; -fx-text-fill: #0079bf;");
            } else {
                service.descongelarTablero(boardId);
                isFrozen = false;
                freezeBtn.setText("Congelar Tablero");
                freezeBtn.setStyle("");
            }
        } catch (Exception e) {
            System.err.println("Error toggle freeze: " + e.getMessage());
        }
    }
}