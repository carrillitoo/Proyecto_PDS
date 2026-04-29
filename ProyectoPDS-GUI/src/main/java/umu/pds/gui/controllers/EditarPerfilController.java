package umu.pds.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import umu.pds.gui.services.GlobalState;
import umu.pds.gui.services.api.UsuarioService;

import java.io.File;

public class EditarPerfilController {

    @FXML private ImageView avatarPreview;
    @FXML private TextField nombreField;
    @FXML private Label fileNameLabel;

    private UsuarioService usuarioService;
    private File selectedFile;

    @FXML
    public void initialize() {
        usuarioService = new UsuarioService();
        
        // Clip circular para la previsualización
        Circle clip = new Circle(40, 40, 40);
        avatarPreview.setClip(clip);

        // Cargar datos actuales
        String nombre = GlobalState.getInstance().getUserName();
        if (nombre != null) {
            nombreField.setText(nombre);
        }
        
        String photoUrl = GlobalState.getInstance().getUserPhotoUrl();
        if (photoUrl != null && !photoUrl.isBlank()) {
            try {
                // En un caso real se cargaría desde la URL servida (e.g. http://localhost:8080/resources/...)
                // Para la previsualización de la ruta podemos intentar cargar si es válida.
                avatarPreview.setImage(new Image("http://localhost:8080/resources/" + photoUrl, true));
            } catch (Exception e) {
                // Ignore
            }
        }
    }

    @FXML
    private void handleSubirFoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Foto de Perfil");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.png", "*.jpeg")
        );
        
        File file = fileChooser.showOpenDialog(nombreField.getScene().getWindow());
        if (file != null) {
            selectedFile = file;
            fileNameLabel.setText(file.getName());
            
            try {
                avatarPreview.setImage(new Image(file.toURI().toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleSave() {
        String nuevoNombre = nombreField.getText();
        String email = GlobalState.getInstance().getUserEmail();

        if (email == null) return;

        try {
            // Guardar nombre
            if (nuevoNombre != null && !nuevoNombre.trim().isEmpty() && !nuevoNombre.equals(GlobalState.getInstance().getUserName())) {
                umu.pds.dto.UsuarioResponseDTO actualizado = usuarioService.updatePerfil(email, nuevoNombre.trim());
                GlobalState.getInstance().setUserName(actualizado.nombre());
            }

            // Subir foto si hay una seleccionada
            if (selectedFile != null) {
                // Simulamos llamada a uploadPhoto
                System.out.println("Subiendo foto: " + selectedFile.getAbsolutePath());
                // Por ahora lanzará unsupported exception si lo llamamos según la implementacion actual, 
                // así que solo mostramos info. En el futuro, se descomentaría esto:
                // umu.pds.dto.UsuarioResponseDTO fotoSubida = usuarioService.uploadPhoto(email, selectedFile);
                // GlobalState.getInstance().setUserPhotoUrl(fotoSubida.urlFoto());
            }

            // Recargar UI principal
            MainLayoutController.getInstance().initialize(); // refresca navbar

            // Volver
            handleClose();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo guardar el perfil: " + e.getMessage());
        }
    }

    @FXML
    private void handleClose() {
        MainLayoutController.getInstance().loadCenterView("Dashboard");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
