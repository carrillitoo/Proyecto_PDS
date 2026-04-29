package umu.pds.gui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.VBox;
import umu.pds.gui.services.GlobalState;
import umu.pds.gui.services.api.TableroService;
import umu.pds.gui.services.api.UsuarioService;

import java.util.List;

public class InvitarMiembroController {

    @FXML private ComboBox<String> emailCombo;
    @FXML private ComboBox<String> rolCombo;
    
    @FXML private VBox linkContainer;
    @FXML private TextField linkGeneradoField;

    private TableroService tableroService;
    private UsuarioService usuarioService;
    private ObservableList<String> allEmails;

    @FXML
    public void initialize() {
        tableroService = new TableroService();
        usuarioService = new UsuarioService();
        
        rolCombo.setItems(FXCollections.observableArrayList("LECTURA", "EDICION"));
        rolCombo.setValue("LECTURA");

        loadUsers();

        // Autocomplete logica sencilla (al escribir, filtra)
        emailCombo.getEditor().textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.isEmpty() || allEmails == null) {
                emailCombo.setItems(allEmails);
            } else {
                ObservableList<String> filtered = FXCollections.observableArrayList();
                for (String email : allEmails) {
                    if (email.toLowerCase().contains(newVal.toLowerCase())) {
                        filtered.add(email);
                    }
                }
                emailCombo.setItems(filtered);
                emailCombo.show();
            }
        });
    }

    private void loadUsers() {
        try {
            List<umu.pds.dto.UsuarioResponseDTO> usuarios = usuarioService.getAllUsuarios();
            allEmails = FXCollections.observableArrayList();
            for (umu.pds.dto.UsuarioResponseDTO u : usuarios) {
                allEmails.add(u.email());
            }
            emailCombo.setItems(allEmails);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("No se pudieron cargar los usuarios para autocomplete");
        }
    }

    @FXML
    private void handleGenerarLink() {
        String email = emailCombo.getEditor().getText();
        String rol = rolCombo.getValue();
        String boardId = GlobalState.getInstance().getCurrentBoardId();

        if (email == null || email.trim().isEmpty()) {
            showAlert("Error", "Debes especificar un correo.");
            return;
        }
        if (boardId == null) {
            showAlert("Error", "No hay un tablero seleccionado.");
            return;
        }

        try {
            boolean ok = tableroService.shareBoard(boardId, email, rol);
            if (ok) {
                // Obtener url
                umu.pds.dto.TableroResponseDTO tablero = tableroService.getTableroById(boardId);
                
                linkContainer.setVisible(true);
                linkContainer.setManaged(true);
                linkGeneradoField.setText(tablero.url());
            } else {
                showAlert("Error API", "No se pudo compartir el tablero.");
            }
        } catch (Exception e) {
            showAlert("Error", "Hubo un error compartiendo el tablero: " + e.getMessage());
        }
    }

    @FXML
    private void handleCopiarLink() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(linkGeneradoField.getText());
        clipboard.setContent(content);
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Copiado");
        alert.setHeaderText(null);
        alert.setContentText("Enlace copiado al portapapeles.");
        alert.showAndWait();
    }

    @FXML
    private void handleClose() {
        MainLayoutController.getInstance().loadCenterView("BoardWorkspace");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
