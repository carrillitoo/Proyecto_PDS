package umu.pds.gui.controllers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import umu.pds.dto.TableroResponseDTO;
import umu.pds.dto.TrazaAccionResponseDTO;
import umu.pds.gui.services.GlobalState;
import umu.pds.gui.services.api.TableroService;

public class DashboardController {

    @FXML
    private FlowPane boardsContainer;

    @FXML
    private VBox activityContainer;

    @FXML
    private VBox activitySection;

    private TableroService tableroService = new TableroService();

    @FXML
    public void initialize() {
        System.out.println("Dashboard cargado. Actividad visible: " + GlobalState.getInstance().isShowActivityInDashboard());
        
        List<TableroResponseDTO> myBoards = new ArrayList<>();
        try {
            String userEmail = GlobalState.getInstance().getUserEmail();
            if (userEmail == null || userEmail.isBlank()) {
                userEmail = "test@example.com";
            }
            myBoards = tableroService.getDashboards(userEmail);
        } catch (Exception e) {
            System.err.println("Error obteniendo tableros: " + e.getMessage());
        }

        renderBoards(myBoards);
        renderActivity(myBoards);
    }

    private void renderBoards(List<TableroResponseDTO> myBoards) {
        if (boardsContainer == null)
            return;
        boardsContainer.getChildren().clear();

        try {

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

    private void renderActivity(List<TableroResponseDTO> myBoards) {
        if (activityContainer == null || activitySection == null)
            return;

        boolean showActivity = GlobalState.getInstance().isShowActivityInDashboard();
        activitySection.setVisible(showActivity);
        activitySection.setManaged(showActivity);

        if (!showActivity) return;

        activityContainer.getChildren().clear();

        // 1. Agregamos todas las trazas de todos los tableros
        List<GlobalTraza> allTrazas = new ArrayList<>();
        for (TableroResponseDTO board : myBoards) {
            if (board.historial() != null) {
                for (TrazaAccionResponseDTO traza : board.historial()) {
                    allTrazas.add(new GlobalTraza(traza, board.nombre()));
                }
            }
        }

        // 2. Ordenamos por fecha (mas reciente primero) y tomamos 5
        List<GlobalTraza> top5 = allTrazas.stream()
                .sorted(Comparator.comparing((GlobalTraza gt) -> gt.traza.fecha()).reversed())
                .limit(5)
                .collect(Collectors.toList());

        if (top5.isEmpty()) {
            Label noAct = new Label("No hay actividad reciente.");
            noAct.setStyle("-fx-text-fill: #707882; -fx-font-style: italic;");
            activityContainer.getChildren().add(noAct);
            return;
        }

        for (GlobalTraza gt : top5) {
            VBox logCard = new VBox();
            logCard.setSpacing(5);
            logCard.setStyle("-fx-padding: 10; -fx-background-color: #f4f5f7; -fx-background-radius: 5;");

            String desc = gt.traza.accion() + " en tarjeta " + gt.traza.tarjetaId();
            Label title = new Label(desc);
            title.setStyle("-fx-font-weight: bold; -fx-text-fill: #172b4d;");

            String timeStr = gt.traza.fecha() != null ? gt.traza.fecha().toString() : "Recientemente";
            Label subtitle = new Label("En " + gt.boardName + " • " + timeStr);
            subtitle.setStyle("-fx-text-fill: #707882; -fx-font-size: 11px;");

            logCard.getChildren().addAll(title, subtitle);
            activityContainer.getChildren().add(logCard);
        }
    }

    private record GlobalTraza(TrazaAccionResponseDTO traza, String boardName) {}

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

}