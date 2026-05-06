package umu.pds.gui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import umu.pds.dto.EtiquetaDTO;
import umu.pds.dto.TableroResponseDTO;
import umu.pds.gui.services.GlobalState;
import umu.pds.gui.services.api.TableroService;

public class GestorEtiquetasController {

    @FXML
    private VBox noBoardWarningBox;
    @FXML
    private VBox mainContentBox;

    @FXML
    private Label formTitleLabel;
    @FXML
    private TextField nombreField;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private Button saveBtn;
    @FXML
    private Button cancelBtn;

    @FXML
    private VBox etiquetasListContainer;
    @FXML
    private Label emptyLabel;

    private TableroService tableroService;
    private String currentBoardId;

    // estado de edicion
    private boolean isEditing = false;
    private String editingEtiquetaName = null;

    @FXML
    public void initialize() {
        tableroService = new TableroService();
        currentBoardId = GlobalState.getInstance().getCurrentBoardId();

        if (currentBoardId == null || currentBoardId.isEmpty()) {
            noBoardWarningBox.setVisible(true);
            noBoardWarningBox.setManaged(true);
            mainContentBox.setVisible(false);
            mainContentBox.setManaged(false);
        } else {
            noBoardWarningBox.setVisible(false);
            noBoardWarningBox.setManaged(false);
            mainContentBox.setVisible(true);
            mainContentBox.setManaged(true);
            loadEtiquetas();
        }
    }

    private void loadEtiquetas() {
        etiquetasListContainer.getChildren().clear();

        try {
            TableroResponseDTO tablero = tableroService.getTableroById(currentBoardId, GlobalState.getInstance().getUserEmail());
            if (tablero != null && tablero.etiquetas() != null && !tablero.etiquetas().isEmpty()) {
                emptyLabel.setVisible(false);
                emptyLabel.setManaged(false);

                for (EtiquetaDTO et : tablero.etiquetas()) {
                    addEtiquetaToList(et.nombre(), et.colorHex());
                }
            } else {
                emptyLabel.setVisible(true);
                emptyLabel.setManaged(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "No se pudieron cargar las etiquetas: " + e.getMessage());
        }
    }

    private void addEtiquetaToList(String nombre, String colorHex) {
        HBox row = new HBox();
        row.setSpacing(15);
        row.setStyle(
                "-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 8; -fx-border-color: #dfe1e6; -fx-border-radius: 8; -fx-alignment: center-left;");

        // Color block + Name
        Label colorBlock = new Label();
        colorBlock.setPrefSize(40, 20);
        colorBlock.setStyle("-fx-background-color: " + colorHex + "; -fx-background-radius: 4;");

        Label nameLabel = new Label(nombre);
        nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #172b4d;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button editBtn = new Button("Editar");
        editBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #0079bf; -fx-cursor: hand;");
        editBtn.setOnAction(e -> startEditing(nombre, colorHex));

        Button deleteBtn = new Button("Borrar");
        deleteBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #d32f2f; -fx-cursor: hand;");
        deleteBtn.setOnAction(e -> deleteEtiqueta(nombre));

        row.getChildren().addAll(colorBlock, nameLabel, spacer, editBtn, deleteBtn);
        etiquetasListContainer.getChildren().add(row);
    }

    @FXML
    private void handleSaveEtiqueta() {
        String nombre = nombreField.getText();
        Color color = colorPicker.getValue();

        if (nombre == null || nombre.trim().isEmpty()) {
            showAlert("Campo requerido", "El nombre de la etiqueta no puede estar vacío.");
            return;
        }

        String hexColor = String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));

        try {
            if (isEditing) {
                boolean ok = tableroService.updateEtiqueta(currentBoardId, editingEtiquetaName, nombre, hexColor);
                if (ok) {
                    handleCancelEdit();
                    loadEtiquetas();
                } else {
                    showAlert("Error", "No se pudo actualizar la etiqueta.");
                }
            } else {
                boolean ok = tableroService.createEtiqueta(currentBoardId, nombre, hexColor);
                if (ok) {
                    nombreField.clear();
                    loadEtiquetas();
                } else {
                    showAlert("Error", "No se pudo crear la etiqueta.");
                }
            }
        } catch (Exception e) {
            showAlert("Error", "Excepción al guardar etiqueta: " + e.getMessage());
        }
    }

    private void startEditing(String oldNombre, String oldColorHex) {
        isEditing = true;
        editingEtiquetaName = oldNombre;

        formTitleLabel.setText("EDITAR ETIQUETA: " + oldNombre);
        nombreField.setText(oldNombre);
        colorPicker.setValue(Color.web(oldColorHex));

        saveBtn.setText("Actualizar Etiqueta");
        cancelBtn.setVisible(true);
        cancelBtn.setManaged(true);
    }

    @FXML
    private void handleCancelEdit() {
        isEditing = false;
        editingEtiquetaName = null;

        formTitleLabel.setText("AÑADIR NUEVA ETIQUETA");
        nombreField.clear();
        colorPicker.setValue(Color.WHITE);

        saveBtn.setText("Guardar Etiqueta");
        cancelBtn.setVisible(false);
        cancelBtn.setManaged(false);
    }

    private void deleteEtiqueta(String nombre) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Seguro que quieres borrar la etiqueta '" + nombre + "'?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait();
        if (confirm.getResult() == ButtonType.YES) {
            try {
                boolean ok = tableroService.deleteEtiqueta(currentBoardId, nombre);
                if (ok) {
                    if (isEditing && nombre.equals(editingEtiquetaName)) {
                        handleCancelEdit();
                    }
                    loadEtiquetas();
                } else {
                    showAlert("Error", "No se pudo eliminar la etiqueta.");
                }
            } catch (Exception e) {
                showAlert("Error", "Excepción al eliminar etiqueta: " + e.getMessage());
            }
        }
    }

    private void showAlert(String title, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }
}
