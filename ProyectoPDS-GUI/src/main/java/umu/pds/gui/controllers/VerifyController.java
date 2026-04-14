package umu.pds.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import umu.pds.gui.App;

public class VerifyController {

    private String userEmail;

    public void setSessionData(String email) {
        this.userEmail = email;
    }

    @FXML
    private void handleVerify() {
        System.out.println("Código verificado en prototipo. Entrando a la App...");
        try {
            // Saltamos directamente al Layout Principal usando el método simplificado
            App.setRoot("MainLayout");
        } catch (Exception e) {
            System.err.println("Error al cargar el MainLayout:");
            e.printStackTrace();
        }
    }
   /* @FXML
    private void handleVerify(ActionEvent event) {
        System.out.println("Lógica de verificación irá aquí. Abriendo MainLayout...");
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/umu/pds/gui/views/MainLayout.fxml"));
            Parent root = loader.load();

            MainLayoutController mainController = loader.getController();
            mainController.iniciarSesion(userEmail);

            App.setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}