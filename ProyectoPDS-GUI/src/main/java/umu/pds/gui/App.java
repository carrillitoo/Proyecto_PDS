package umu.pds.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    private static Scene scene;
    private static Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        App.stage = stage;
        // Arrancamos directamente en el Login
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/umu/pds/gui/views/Login.fxml"));
        scene = new Scene(loader.load(), 1200, 760);
        stage.setTitle("Trello PDS - Prototipo");
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws Exception {
        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(App.class.getResource("/umu/pds/gui/views/" + fxml + ".fxml"));
        scene.setRoot(loader.load());
    }

    public static void main(String[] args) {
        launch(args);
    }
}