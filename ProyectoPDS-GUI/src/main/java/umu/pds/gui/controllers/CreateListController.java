package umu.pds.gui.controllers;

import java.util.Collections;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import umu.pds.gui.services.api.TableroService;
import umu.pds.gui.services.GlobalState;

public class CreateListController {

    @FXML
    private TextField listNameField;
    @FXML
    private Slider capacitySlider;

    @FXML
    public void initialize() {
        System.out.println("Modal de Crear Lista cargado.");
    }

    @FXML
    private void handleCreateList() {
        String listName = listNameField.getText();
        if (listName == null || listName.trim().isEmpty()) {
            showAlert("Nombre Obligatorio", "El nombre de la lista es obligatorio.");
            return;
        }

        System.out.println("Creando lista: " + listName + " con límite " + (int) capacitySlider.getValue());

        try {
            TableroService tableroService = new TableroService();
            String currentBoardId = GlobalState.getInstance().getCurrentBoardId();

            if (currentBoardId == null) {
                showAlert("Error de Contexto", "No se puede crear lista porque no hay un tablero seleccionado.");
                return;
            }

            boolean exito = tableroService.createList(currentBoardId, listName, Collections.emptyList());

            if (exito) {
                System.out.println("Lista creada exitosamente en la API.");
            } else {
                showAlert("Error API", "La API reportó un fallo al crear la lista.");
            }
        } catch (Exception e) {
            System.err.println("Error al contactar con la API: " + e.getMessage());
            e.printStackTrace();
        }

        // Tras crear (o al menos notificar), volver al tablero
        MainLayoutController.getInstance().loadCenterView("BoardWorkspace");
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