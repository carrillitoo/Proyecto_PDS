package umu.pds.gui.controllers;

import umu.pds.gui.services.GlobalState;
import umu.pds.gui.services.api.TableroService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class CompactionController {

    @FXML
    private Button compactarBtn;
    
    @FXML
    private ComboBox<String> antiguedadCombo;

    @FXML
    public void initialize() {
        System.out.println("Cargando reglas de compactación del tablero...");
        antiguedadCombo.getItems().addAll("1 día", "1 semana", "1 mes");
        antiguedadCombo.getSelectionModel().select("1 semana");
    }

    @FXML
    private void handleCompact() {
        System.out.println("Compactando tablero...");
        String boardId = GlobalState.getInstance().getCurrentBoardId();
        
        String seleccion = antiguedadCombo.getValue();
        int dias = 7;
        if ("1 día".equals(seleccion)) dias = 1;
        else if ("1 mes".equals(seleccion)) dias = 30;
        
        try {
            TableroService service = new TableroService();
            service.compactarTablero(boardId, dias);
            compactarBtn.setText("¡Tablero Compactado!");
            compactarBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #5aac44; -fx-text-fill: #5aac44;");
        } catch (Exception e) {
            System.err.println("Error compactando: " + e.getMessage());
            compactarBtn.setText("Error");
            compactarBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #eb5a46; -fx-text-fill: #eb5a46;");
        }
    }
}