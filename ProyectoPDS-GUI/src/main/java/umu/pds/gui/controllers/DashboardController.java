package umu.pds.gui.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class DashboardController {

    @FXML
    private FlowPane boardsContainer;

    @FXML
    private VBox activityContainer;

    // DTOs simulados
    public record BoardDto(String id, String title, String colorHex) {}
    public record ActivityLog(String description, String timestamp, String boardName) {}

    @FXML
    public void initialize() {
        System.out.println("Dashboard cargado.");
        // TODO: Conectar con backend - Obtener la lista de tableros del usuario actual y la actividad reciente
        renderBoards();
        renderActivity();
    }

    private void renderBoards() {
        if (boardsContainer == null) return;
        boardsContainer.getChildren().clear();

        // Datos mock parametrizados
        List<BoardDto> myBoards = List.of(
            new BoardDto("b1", "Lanzamiento Q4", "#0079bf"),
            new BoardDto("b2", "Rediseño UI", "#5aac44"),
            new BoardDto("b3", "Marketing Campaing", "#eb5a46"),
            new BoardDto("b4", "Backend API v2", "#89609e")
        );

        for (BoardDto board : myBoards) {
            VBox boardCard = new VBox();
            boardCard.setStyle("-fx-background-color: " + board.colorHex() + ";");
            boardCard.getStyleClass().add("board-card");
            // Efectos visuales premium dinámicos para hover se manejan en CSS, 
            // pero el click handler lo inyectamos aquí:
            boardCard.setOnMouseClicked(e -> abrirTablero(board.id()));

            Label titleLabel = new Label(board.title());
            titleLabel.getStyleClass().add("board-card-title");
            boardCard.getChildren().add(titleLabel);

            boardsContainer.getChildren().add(boardCard);
        }

        // Placeholder para crear nuevo tablero
        VBox newBoardPlaceholder = new VBox();
        newBoardPlaceholder.setAlignment(Pos.CENTER);
        newBoardPlaceholder.setPrefWidth(200);
        newBoardPlaceholder.setPrefHeight(120);
        newBoardPlaceholder.getStyleClass().add("new-board-placeholder");
        newBoardPlaceholder.setOnMouseClicked(e -> handleNewBoard());
        
        Label plusLabel = new Label("+");
        plusLabel.setStyle("-fx-font-size: 30px; -fx-text-fill: #707882;");
        Label textLabel = new Label("Nuevo Tablero");
        textLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #707882; -fx-font-weight: bold;");
        
        newBoardPlaceholder.getChildren().addAll(plusLabel, textLabel);
        boardsContainer.getChildren().add(newBoardPlaceholder);
    }

    private void renderActivity() {
        if (activityContainer == null) return;
        activityContainer.getChildren().clear();

        List<ActivityLog> logs = List.of(
            new ActivityLog("Actualizaste la tarjeta 'Moodboard Visual'", "Hace 2 horas", "Rediseño UI"),
            new ActivityLog("Creaste la lista 'To Do'", "Hace 5 horas", "Backend API v2"),
            new ActivityLog("Te invitaron al tablero", "Hace 1 día", "Marketing Campaing")
        );

        for (ActivityLog log : logs) {
            VBox logCard = new VBox();
            logCard.setSpacing(10);
            
            Label title = new Label(log.description());
            title.setStyle("-fx-font-weight: bold;");
            
            Label subtitle = new Label("En " + log.boardName() + " • " + log.timestamp());
            subtitle.setStyle("-fx-text-fill: #707882; -fx-font-size: 11px;");
            
            logCard.getChildren().addAll(title, subtitle);
            activityContainer.getChildren().add(logCard);
        }
    }

    private void abrirTablero(String boardId) {
        System.out.println("Abriendo el tablero (BoardWorkspace) asociado al ID: " + boardId);
        // En una app real, aquí guardaríamos el boardId actual en una sesión global
        MainLayoutController.getInstance().loadCenterView("BoardWorkspace");
    }

    @FXML
    private void handleNewBoard() {
        System.out.println("Abriendo modal para crear tablero...");
        MainLayoutController.getInstance().loadCenterView("CrearTablero");
    }

    @FXML
    private void abrirPlantillas() {
        System.out.println("Abriendo galería de plantillas YAML...");
        MainLayoutController.getInstance().loadCenterView("Plantillas");
    }
}