package umu.pds.gui.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.List;

import umu.pds.dto.TableroResponseDTO;
import umu.pds.gui.services.GlobalState;
import umu.pds.gui.services.api.TableroService;

public class DashboardController {

    @FXML
    private FlowPane boardsContainer;

    @FXML
    private VBox activityContainer;

    // Activity Logs Simulados (Actividad no tiene backend aun)
    public record ActivityLog(String description, String timestamp, String boardName) {
    }

    @FXML
    public void initialize() {
        System.out.println("Dashboard cargado.");
        renderBoards();
        renderActivity();
    }

    private void renderBoards() {
        if (boardsContainer == null)
            return;
        boardsContainer.getChildren().clear();

        try {
            TableroService tableroService = new TableroService();
            String userEmail = GlobalState.getInstance().getUserEmail();
            if (userEmail == null || userEmail.isBlank()) {
                System.out.println("No hay email de sesión, redirigiendo a login...");
                // Podríamos forzar un render later para redirigir si quisiéramos, 
                // pero por ahora solo evitamos el crash y usamos un test email.
                userEmail = "test@example.com";
            }

            List<TableroResponseDTO> myBoards = tableroService.getDashboards(userEmail);

            for (TableroResponseDTO board : myBoards) {
                VBox boardCard = new VBox();
                String colorHex = "#0079bf";
                boardCard.setStyle("-fx-background-color: " + colorHex + ";");
                boardCard.getStyleClass().add("board-card");
                boardCard.setOnMouseClicked(e -> abrirTablero(board.id()));

                Label titleLabel = new Label(board.nombre());
                titleLabel.getStyleClass().add("board-card-title");
                boardCard.getChildren().add(titleLabel);

                boardsContainer.getChildren().add(boardCard);
            }
        } catch (Exception ex) {
            System.err.println("Error obteniendo tableros: " + ex.getMessage());
            // No bloqueamos la interfaz si falla la API
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
        if (activityContainer == null)
            return;
        activityContainer.getChildren().clear();

        List<ActivityLog> logs = List.of(
                new ActivityLog("Actualizaste la tarjeta 'Moodboard Visual'", "Hace 2 horas", "Rediseño UI"),
                new ActivityLog("Creaste la lista 'To Do'", "Hace 5 horas", "Backend API v2"),
                new ActivityLog("Te invitaron al tablero", "Hace 1 día", "Marketing Campaing"));

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
        GlobalState.getInstance().setCurrentBoardId(boardId);
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