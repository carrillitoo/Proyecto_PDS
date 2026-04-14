package umu.pds.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class PermisosController {

    @FXML private ComboBox<String> roleCombo1;
    @FXML private ComboBox<String> roleCombo2;

    @FXML
    public void initialize() {
        System.out.println("Cargando lista de miembros y permisos...");
    }

    @FXML
    private void handleRoleChange(ActionEvent event) {
        @SuppressWarnings("unchecked")
        ComboBox<String> source = (ComboBox<String>) event.getSource();
        String newRole = source.getValue();
        System.out.println("Actualizando en la BD el nuevo rol a: " + newRole);
    }
}