package umu.pds.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import umu.pds.gui.services.GlobalState;
import umu.pds.gui.services.api.TarjetaService;

public class EtiquetasController {

    @FXML
    private FlowPane etiquetasContainer;
    @FXML
    private Label emptyLabel;
    @FXML
    private TextField nombreEtiquetaField;
    @FXML
    private ColorPicker colorPicker;

    @FXML
    public void initialize() {
        System.out.println("Gestión de Etiquetas cargada.");
    }

    @FXML
    private void handleAddEtiqueta() {
        String nombre = nombreEtiquetaField.getText();
        Color color = colorPicker.getValue();

        if (nombre == null || nombre.trim().isEmpty()) {
            showAlert("Nombre Requerido", "El nombre de la etiqueta no puede estar vacío.");
            return;
        }

        String hexColor = String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));

        System.out.println("Añadiendo etiqueta: " + nombre + " con color " + hexColor);
        String cardId = GlobalState.getInstance().getCurrentCardId();
        if (cardId == null) {
            showAlert("Error de Contexto", "No hay una tarjeta seleccionada para añadir la etiqueta.");
            return;
        }

        try {
            TarjetaService tarjetaService = new TarjetaService();
            boolean exito = tarjetaService.addLabelToCard(cardId, nombre, hexColor);
            if (!exito) {
                showAlert("Error API", "La API falló al añadir la etiqueta.");
                return;
            }
        } catch (Exception ex) {
            showAlert("Error de Conexión", "Error al crear la etiqueta: " + ex.getMessage());
            return; // don't render visually if API failed
        }

        // Crear chip visual
        Label chip = new Label(nombre);
        chip.setStyle("-fx-background-color: " + hexColor
                + "; -fx-padding: 4 12; -fx-background-radius: 12; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 11px;");

        Button removeBtn = new Button("x");
        removeBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 10px; -fx-padding: 0 0 0 5;");

        HBox tagBox = new HBox(chip, removeBtn);
        tagBox.setStyle("-fx-background-color: " + hexColor
                + "; -fx-background-radius: 12; -fx-padding: 2 8; -fx-alignment: center;");
        removeBtn.setOnAction(e -> {
            etiquetasContainer.getChildren().remove(tagBox);
            if (etiquetasContainer.getChildren().isEmpty())
                emptyLabel.setVisible(true);
        });

        etiquetasContainer.getChildren().add(tagBox);
        emptyLabel.setVisible(false);

        // Limpiar campos
        nombreEtiquetaField.clear();
    }

    @FXML
    private void handleClose() {
        System.out.println("Cerrando gestión de etiquetas...");
        MainLayoutController.getInstance().loadCenterView("BoardWorkspace");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
}
