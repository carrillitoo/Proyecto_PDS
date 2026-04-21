package umu.pds.gui.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import umu.pds.gui.services.GlobalState;
import umu.pds.gui.services.api.TarjetaService;

public class CreateCardController {

    @FXML private ComboBox<String> tipoCombo;
    @FXML private TextField tituloField;
    @FXML private TextArea descripcionArea;
    @FXML private VBox tareaSection;
    @FXML private VBox checklistSection;
    @FXML private VBox checklistItemsContainer;
    @FXML private TextArea contenidoTareaArea;

    @FXML
    public void initialize() {
        tipoCombo.setItems(FXCollections.observableArrayList("Tarea", "Checklist"));
        tipoCombo.setValue("Tarea");

        tipoCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            boolean esTarea = "Tarea".equals(newVal);
            tareaSection.setVisible(esTarea);
            tareaSection.setManaged(esTarea);
            checklistSection.setVisible(!esTarea);
            checklistSection.setManaged(!esTarea);
        });
    }

    @FXML
    private void handleAddChecklistItem() {
        TextField nuevoItem = new TextField();
        nuevoItem.setPromptText("Nuevo item del checklist...");
        nuevoItem.getStyleClass().add("text-field");
        checklistItemsContainer.getChildren().add(nuevoItem);
    }

    @FXML
    private void handleCreateCard() {
        String tipo = tipoCombo.getValue();
        String titulo = tituloField.getText();
        String descripcion = descripcionArea.getText();

        if (titulo == null || titulo.trim().isEmpty()) {
            showAlert("Título Obligatorio", "El título de la tarjeta no puede estar vacío");
            return;
        }

        if (descripcion == null || descripcion.trim().isEmpty()) {
            showAlert("Descripción Obligatoria", "La descripción de la tarjeta no puede estar vacía");
            return;
        }

        String boardId = GlobalState.getInstance().getCurrentBoardId();
        String listId = GlobalState.getInstance().getCurrentListId();

        try {
            String contenido = "TAREA".equals(tipo) ? contenidoTareaArea.getText() : "{}";
            
            if ("Checklist".equals(tipo)) {
                // Formatting checklist items basic approach
                java.util.List<String> items = new java.util.ArrayList<>();
                for (javafx.scene.Node node : checklistItemsContainer.getChildren()) {
                    if (node instanceof TextField) {
                        String text = ((TextField) node).getText();
                        if (!text.trim().isEmpty()) items.add(text);
                    }
                }
                contenido = String.join("||", items);
            }

            boolean exito = tarjetaService.createCard(boardId, listId, titulo, descripcion, tipo.toUpperCase(), contenido);
            if (!exito) {
                showAlert("Error API", "La API falló al crear la tarjeta");
                return;
            }
            
            // Volver al tablero
            MainLayoutController.getInstance().loadCenterView("BoardWorkspace");
        } catch (Exception ex) {
            showAlert("Error de Conexión", "Error contactando con la API: " + ex.getMessage());
        }
    }

    @FXML
    private void handleClose() {
        System.out.println("Cerrando formulario de creación de tarjeta...");
        MainLayoutController.getInstance().loadCenterView("BoardWorkspace");
    }

    private void showAlert(String title, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
}
