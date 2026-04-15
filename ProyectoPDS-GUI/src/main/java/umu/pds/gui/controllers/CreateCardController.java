package umu.pds.gui.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

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

        if ("Tarea".equals(tipo)) {
            String contenido = contenidoTareaArea.getText();
            System.out.println("Creando TarjetaTarea: " + titulo + " | Tarea: " + contenido);
            // TODO: Conectar con backend - LLamar a CardService para crear la tarjeta de tipo TAREA
        } else {
            int numItems = checklistItemsContainer.getChildren().size();
            System.out.println("Creando TarjetaChecklist: " + titulo + " con " + numItems + " items");
            // TODO: Conectar con backend - LLamar a CardService para crear la tarjeta de tipo CHECKLIST
        }

        // Volver al tablero
        MainLayoutController.getInstance().loadCenterView("BoardWorkspace");
    }

    @FXML
    private void handleClose() {
        System.out.println("Cerrando formulario de creación de tarjeta...");
        MainLayoutController.getInstance().loadCenterView("BoardWorkspace");
    }
}
