package umu.pds.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    private static Scene scene;

    @Override
    public void start(Stage stage) throws Exception {
        // Arrancamos directamente en el Login
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/umu/pds/gui/views/Login.fxml"));
        scene = new Scene(loader.load(), 1200, 760);
        stage.setTitle("Tablerellos - Prototipo");
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws Exception {
        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                App.class.getResource("/umu/pds/gui/views/" + fxml + ".fxml"));
        scene.setRoot(loader.load());
    }

    public static void main(String[] args) {
        System.setProperty("prism.allowhidpi", "true");
        System.setProperty("glass.gtk.uiScale", "1.5");
        launch(args);
    }
}