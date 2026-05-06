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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import umu.pds.dto.EtiquetaDTO;
import umu.pds.dto.ListaTareasResponseDTO;
import umu.pds.dto.TableroResponseDTO;
import umu.pds.dto.TarjetaResponseDTO;
import umu.pds.gui.services.GlobalState;
import umu.pds.gui.services.api.TableroService;
import umu.pds.gui.services.api.TarjetaService;

public class EtiquetasController {

    @FXML
    private FlowPane etiquetasContainer;
    @FXML
    private Label emptyLabel;
    @FXML
    private FlowPane etiquetasDisponiblesContainer;
    @FXML
    private Label emptyDisponiblesLabel;
    @FXML
    private TextField nombreEtiquetaField;
    @FXML
    private ColorPicker colorPicker;

    @FXML
    public void initialize() {
        System.out.println("Gestión de Etiquetas cargada.");
        loadCardLabels();
    }

    private void loadCardLabels() {
        String cardId = GlobalState.getInstance().getCurrentCardId();
        if (cardId == null)
            return;

        Set<String> cardLabelNames = new HashSet<>();

        try {
            TarjetaResponseDTO card = GlobalState.getInstance().getCurrentCard();

            if (card != null && card.etiquetas() != null) {
                etiquetasContainer.getChildren().clear();
                for (EtiquetaDTO et : card.etiquetas()) {
                    addTagToView(et.nombre(), et.colorHex());
                    cardLabelNames.add(et.nombre());
                }
                emptyLabel.setVisible(card.etiquetas().isEmpty());
            } else {
                etiquetasContainer.getChildren().clear();
                emptyLabel.setVisible(true);
            }

            // cargar etiquetas disponibles
            loadAvailableLabels(cardLabelNames);

        } catch (Exception e) {
            System.err.println("Error cargando etiquetas de la tarjeta: " + e.getMessage());
        }
    }

    private void loadAvailableLabels(Set<String> cardLabelNames) {
        String boardId = GlobalState.getInstance().getCurrentBoardId();
        if (boardId == null)
            return;

        if (etiquetasDisponiblesContainer != null) {
            etiquetasDisponiblesContainer.getChildren().clear();
        }

        try {
            TableroService tableroService = new TableroService();
            TableroResponseDTO tablero = tableroService.getTableroById(boardId,
                    GlobalState.getInstance().getUserEmail());

            if (tablero != null) {
                Map<String, String> uniqueLabels = new HashMap<>();

                if (tablero.etiquetas() != null) {
                    for (EtiquetaDTO et : tablero.etiquetas()) {
                        if (!cardLabelNames.contains(et.nombre())) {
                            uniqueLabels.put(et.nombre(), et.colorHex());
                        }
                    }
                }

                if (tablero.listas() != null) {
                    for (ListaTareasResponseDTO listData : tablero.listas()) {
                        if (listData.tarjetas() != null) {
                            for (TarjetaResponseDTO t : listData.tarjetas()) {
                                if (t.etiquetas() != null) {
                                    for (EtiquetaDTO et : t.etiquetas()) {
                                        if (!cardLabelNames.contains(et.nombre())) {
                                            uniqueLabels.put(et.nombre(), et.colorHex());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                for (Map.Entry<String, String> entry : uniqueLabels.entrySet()) {
                    addAvailableTagToView(entry.getKey(), entry.getValue());
                }

                if (emptyDisponiblesLabel != null) {
                    emptyDisponiblesLabel.setVisible(uniqueLabels.isEmpty());
                }
            }
        } catch (Exception e) {
            System.err.println("Error cargando etiquetas del tablero: " + e.getMessage());
        }
    }

    private void addAvailableTagToView(String nombre, String hexColor) {
        Label chip = new Label(nombre);
        chip.setStyle("-fx-background-color: " + hexColor
                + "; -fx-padding: 4 12; -fx-background-radius: 12; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 11px;");

        Button addBtn = new Button("+");
        addBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 10px; -fx-padding: 0 0 0 5;");

        HBox tagBox = new HBox(chip, addBtn);
        tagBox.setStyle("-fx-background-color: " + hexColor
                + "; -fx-background-radius: 12; -fx-padding: 2 8; -fx-alignment: center; -fx-cursor: hand;");

        tagBox.setOnMouseClicked(e -> {
            handleAddExistingEtiqueta(nombre, hexColor);
        });

        if (etiquetasDisponiblesContainer != null) {
            etiquetasDisponiblesContainer.getChildren().add(tagBox);
        }
    }

    private void handleAddExistingEtiqueta(String nombre, String hexColor) {
        String cardId = GlobalState.getInstance().getCurrentCardId();
        if (cardId == null)
            return;

        try {
            TarjetaService tarjetaService = new TarjetaService();
            TarjetaResponseDTO updatedCard = tarjetaService.addLabelToCard(cardId, nombre, hexColor);
            if (updatedCard != null) {
                GlobalState.getInstance().setCurrentCard(updatedCard);
                loadCardLabels();
            } else {
                showAlert("Error API", "La API falló al añadir la etiqueta existente.");
            }
        } catch (Exception ex) {
            showAlert("Error de Conexión", "Error al crear la etiqueta: " + ex.getMessage());
        }
    }

    private void addTagToView(String nombre, String hexColor) {
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

        String boardId = GlobalState.getInstance().getCurrentBoardId();
        if (boardId != null) {
            try {
                TableroService tableroService = new TableroService();
                tableroService.createEtiqueta(boardId, nombre, hexColor);
            } catch (Exception ignored) {
                System.out.println("Aviso: No se pudo añadir la etiqueta al tablero global (puede que ya exista).");
            }
        }

        try {
            TarjetaService tarjetaService = new TarjetaService();
            TarjetaResponseDTO updatedCard = tarjetaService.addLabelToCard(cardId, nombre, hexColor);
            if (updatedCard != null) {
                GlobalState.getInstance().setCurrentCard(updatedCard);
            } else {
                showAlert("Error API", "La API falló al añadir la etiqueta.");
                return;
            }
        } catch (Exception ex) {
            showAlert("Error de Conexión", "Error al crear la etiqueta: " + ex.getMessage());
            return; // don't render visually if API failed
        }

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

        // limpiar campos
        nombreEtiquetaField.clear();

        // recargar para que desaparezca de las disponibles si es necesario
        loadCardLabels();
    }

    @FXML
    private void handleClose() {
        System.out.println("Cerrando gestión de etiquetas...");
        MainLayoutController.getInstance().loadCenterView("Tarea");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
}
