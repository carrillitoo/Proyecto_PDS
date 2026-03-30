package umu.pds.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // 1. Buscamos el archivo de diseño en la carpeta de recursos
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/umu/pds/gui/views/login.fxml"));
        
        // 2. Lo cargamos en un objeto "Parent"
        Parent root = fxmlLoader.load();
        
        // 3. Creamos la escena (400 ancho x 500 alto)
        Scene scene = new Scene(root, 400, 500);
        
        // 4. Configuramos la ventana
        stage.setTitle("Trello PDS - Login");
        stage.setScene(scene);
        stage.setResizable(false); // Para que el diseño no se rompa al estirar
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}