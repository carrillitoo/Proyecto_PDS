package umu.pds.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class EtiquetasController {

    @FXML private FlowPane etiquetasContainer;
    @FXML private Label emptyLabel;
    @FXML private TextField nombreEtiquetaField;
    @FXML private ColorPicker colorPicker;

    @FXML
    public void initialize() {
        System.out.println("Gestión de Etiquetas cargada.");
    }

    @FXML
    private void handleAddEtiqueta() {
        String nombre = nombreEtiquetaField.getText();
        Color color = colorPicker.getValue();

        if (nombre == null || nombre.trim().isEmpty()) {
            System.out.println("El nombre de la etiqueta no puede estar vacío.");
            return;
        }

        String hexColor = String.format("#%02X%02X%02X",
            (int)(color.getRed()*255),
            (int)(color.getGreen()*255),
            (int)(color.getBlue()*255));

        System.out.println("Añadiendo etiqueta: " + nombre + " con color " + hexColor);
        // TODO: Conectar con backend - Llamar a CardService.addLabelToCard(cardId, nombre, hexColor)

        // Crear chip visual
        Label chip = new Label(nombre);
        chip.setStyle("-fx-background-color: " + hexColor + "; -fx-padding: 4 12; -fx-background-radius: 12; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 11px;");

        Button removeBtn = new Button("x");
        removeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 10px; -fx-padding: 0 0 0 5;");
        
        HBox tagBox = new HBox(chip, removeBtn);
        tagBox.setStyle("-fx-background-color: " + hexColor + "; -fx-background-radius: 12; -fx-padding: 2 8; -fx-alignment: center;");
        removeBtn.setOnAction(e -> {
            etiquetasContainer.getChildren().remove(tagBox);
            if (etiquetasContainer.getChildren().isEmpty()) emptyLabel.setVisible(true);
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
}
