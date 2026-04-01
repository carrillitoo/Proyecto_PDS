package umu.pds.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class App extends Application {
    private static Scene scene;
    private static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        // Cargamos la primera vista (Login)
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/umu/pds/gui/views/login.fxml"));
        Parent root = loader.load();
        
        scene = new Scene(root, 400, 500);
        stage.setTitle("Trello PDS - Login");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    // Para cuando ya tienes el Parent cargado
    public static void setRoot(Parent root) {
        scene.setRoot(root);
        //ajustar tamaño ventana al conteido de ella
        stage.sizeToScene(); 
        stage.centerOnScreen();
    }

    // Para cargar pasando solo el nombre del archivo
    public static void setRoot(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/umu/pds/gui/views/" + fxml + ".fxml"));
        Parent root = loader.load();
        scene.setRoot(root);
        stage.sizeToScene();
        stage.centerOnScreen();
    }

    public static void main(String[] args) {
        launch(args);
    }
}