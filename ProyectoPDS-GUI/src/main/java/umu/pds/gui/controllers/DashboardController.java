package umu.pds.gui.controllers;

import javafx.fxml.FXML;

public class DashboardController {

    @FXML
    public void initialize() {
        System.out.println("Dashboard cargado.");
    }

    // Este método lo puedes enlazar en tu Dashboard.fxml a las tarjetas de los tableros
    // usando onMouseClicked="#abrirTablero" en el VBox de la tarjeta.
    @FXML
    private void abrirTablero() {
        System.out.println("Abriendo el tablero...");
        MainLayoutController.getInstance().loadCenterView("TableroAvanzado");
    }

    @FXML
    private void handleNewBoard() {
        System.out.println("Abriendo modal para crear tablero...");
        // Aquí puedes cargar la vista CrearTablero.fxml en el centro o en una ventana nueva
        MainLayoutController.getInstance().loadCenterView("CrearTablero");
    }
}