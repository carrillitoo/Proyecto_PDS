package umu.pds.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class PermisosController {

    @FXML private ComboBox<String> roleCombo1;
    @FXML private ComboBox<String> roleCombo2;
    @FXML private javafx.scene.layout.VBox usersTable;
    @FXML private javafx.scene.layout.HBox userRow2;
    @FXML private javafx.scene.layout.VBox urlShareBox;
    @FXML private javafx.scene.control.TextField urlField;
    @FXML private javafx.scene.control.Button copyButton;

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
        // TODO: Conectar con backend - Actualizar el rol del usuario en este tablero mediante la API
        // Llamará a: usuario.concederAccesoATablero(tableroId, Rol.valueOf(newRole))
    }

    @FXML
    private void handleInviteUser() {
        System.out.println("Invitando nuevo usuario al tablero...");
        urlShareBox.setVisible(true);
        urlShareBox.setManaged(true);
        urlField.setText("https://pdsarchitect.com/invite/q4-launch-" + System.currentTimeMillis() % 10000);
        copyButton.setText("Copiar");
        copyButton.setStyle("-fx-background-color: #0079bf; -fx-text-fill: white; -fx-font-weight: bold;");
    }

    @FXML
    private void handleCopyUrl() {
        String url = urlField.getText();
        ClipboardContent content = new ClipboardContent();
        content.putString(url);
        Clipboard.getSystemClipboard().setContent(content);
        System.out.println("URL del tablero copiada al portapapeles: " + url);
        copyButton.setText("¡Copiado!");
        copyButton.setStyle("-fx-background-color: #5aac44; -fx-text-fill: white; -fx-font-weight: bold;");
    }

    @FXML
    private void handleRevokeAccess() {
        System.out.println("Revocando acceso del usuario seleccionado...");
        if (usersTable != null && userRow2 != null) {
            usersTable.getChildren().remove(userRow2);
        }
    }
}