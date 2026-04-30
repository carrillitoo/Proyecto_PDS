package umu.pds.gui.controllers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import umu.pds.dto.TableroResponseDTO;
import umu.pds.dto.ListaTareasResponseDTO;
import umu.pds.gui.services.api.TableroService;
import umu.pds.gui.services.GlobalState;

public class CreateListController {

    @FXML
    private TextField listNameField;
    @FXML
    private Slider capacitySlider;
    @FXML
    private javafx.scene.control.CheckBox unlimitedCheckbox;
    @FXML
    private ComboBox<String> sourceRestrictionCombo;
    @FXML
    private javafx.scene.control.Label sliderValueLabel;

    private TableroService tableroService = new TableroService();

    @FXML
    public void initialize() {
        System.out.println("Modal de Crear Lista cargado.");
        if (unlimitedCheckbox != null && capacitySlider != null) {
            capacitySlider.disableProperty().bind(unlimitedCheckbox.selectedProperty());
            
            if (sliderValueLabel != null) {
                sliderValueLabel.textProperty().bind(capacitySlider.valueProperty().asString("%.0f"));
                sliderValueLabel.visibleProperty().bind(unlimitedCheckbox.selectedProperty().not());
                sliderValueLabel.managedProperty().bind(sliderValueLabel.visibleProperty());
            }
        }

        loadExistingLists();
    }

    private void loadExistingLists() {
        try {
            String currentBoardId = GlobalState.getInstance().getCurrentBoardId();
            if (currentBoardId != null) {
                TableroResponseDTO board = tableroService.getBoard(currentBoardId, GlobalState.getInstance().getUserEmail());
                List<String> listNames = board.listas().stream()
                        .map(ListaTareasResponseDTO::nombreLista)
                        .collect(Collectors.toList());
                
                sourceRestrictionCombo.setItems(FXCollections.observableArrayList(listNames));
            }
        } catch (Exception e) {
            System.err.println("Error cargando listas existentes: " + e.getMessage());
        }
    }

    @FXML
    private void handleCreateList() {
        String listName = listNameField.getText();
        if (listName == null || listName.trim().isEmpty()) {
            showAlert("Nombre Obligatorio", "El nombre de la lista es obligatorio.");
            return;
        }

        try {
            String currentBoardId = GlobalState.getInstance().getCurrentBoardId();

            if (currentBoardId == null) {
                showAlert("Error de Contexto", "No se puede crear lista porque no hay un tablero seleccionado.");
                return;
            }

            // Si el checkbox esta marcado, el limite es null (infinito en backend)
            Integer limite = (unlimitedCheckbox != null && unlimitedCheckbox.isSelected()) ? null : (int) capacitySlider.getValue();

            // Reglas de origen
            List<String> reglas = Collections.emptyList();
            String selectedSource = sourceRestrictionCombo.getValue();
            if (selectedSource != null && !selectedSource.isEmpty()) {
                reglas = List.of(selectedSource);
            }

            System.out.println("Creando lista: " + listName + " con límite " + (limite == null ? "Ilimitado" : limite) + " y origen " + selectedSource);

            boolean exito = tableroService.createList(currentBoardId, listName, reglas, limite);

            if (exito) {
                System.out.println("Lista creada exitosamente en la API.");
                // Tras crear con exito, volver al tablero (esto forzará el refresco)
                MainLayoutController.getInstance().loadCenterView("BoardWorkspace");
            } else {
                showAlert("Error API", "La API reportó un fallo al crear la lista.");
            }
        } catch (Exception e) {
            System.err.println("Error al contactar con la API: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClose() {
        System.out.println("Cerrando formulario de creación de lista...");
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