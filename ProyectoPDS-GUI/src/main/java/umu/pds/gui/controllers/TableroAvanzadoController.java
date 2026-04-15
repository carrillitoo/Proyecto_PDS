package umu.pds.gui.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TableroAvanzadoController {

    @FXML
    private HBox listsContainer;

    // DTOs simulados
    public record LabelDto(String name, String colorHex) {}
    
    public static class KanbanCard {
        public String id;
        public String title;
        public List<LabelDto> labels;
        public KanbanCard(String id, String title, List<LabelDto> labels) {
            this.id = id; this.title = title; this.labels = new ArrayList<>(labels);
        }
    }
    
    public static class KanbanList {
        public String id;
        public String title;
        public List<KanbanCard> cards;
        public KanbanList(String id, String title, List<KanbanCard> cards) {
            this.id = id; this.title = title; this.cards = new ArrayList<>(cards);
        }
    }

    private List<KanbanList> boardData = new ArrayList<>();

    @FXML
    public void initialize() {
        System.out.println("Inicializando Tablero Avanzado (Kanban Dinámico con DnD)...");
        // TODO: Conectar con backend - Obtener el tablero y sus listas/tarjetas desde la API
        initializeMockData();
        renderBoard();
    }

    private void initializeMockData() {
        // Datos mock
        List<LabelDto> feDev = List.of(new LabelDto("FE DEV", "#2196f3"));
        List<LabelDto> urgente = List.of(new LabelDto("URGENTE", "#f44336"));
        List<LabelDto> backend = List.of(new LabelDto("BACKEND", "#9c27b0"));

        KanbanList todo = new KanbanList("list-1", "To Do", List.of(
            new KanbanCard("c-1", "Optimizar renderizado del dashboard para navegadores antiguos", feDev),
            new KanbanCard("c-2", "Crear endpoint de compactación", backend),
            new KanbanCard("c-3", "Fix bug de sesión caducada", urgente)
        ));

        KanbanList inProgress = new KanbanList("list-2", "In Progress", List.of(
            new KanbanCard("c-4", "Renovar paleta de colores", feDev)
        ));

        KanbanList done = new KanbanList("list-3", "Done", List.of(
            new KanbanCard("c-5", "Mock base de datos", backend)
        ));

        boardData.addAll(List.of(todo, inProgress, done));
    }

    private void renderBoard() {
        if (listsContainer == null) return;
        listsContainer.getChildren().clear();

        for (KanbanList listData : boardData) {
            VBox listColumn = createListColumn(listData);
            listsContainer.getChildren().add(listColumn);
        }

        // El placeholder de nueva lista
        VBox addListPlaceholder = new VBox();
        addListPlaceholder.setAlignment(Pos.CENTER);
        addListPlaceholder.setPrefWidth(280.0);
        addListPlaceholder.setMinHeight(50.0);
        addListPlaceholder.getStyleClass().add("new-board-placeholder");
        addListPlaceholder.setOnMouseClicked(e -> handleAddList());
        
        Label addListLabel = new Label("+ Añadir otra lista");
        addListLabel.setStyle("-fx-text-fill: #707882; -fx-font-weight: bold;");
        addListPlaceholder.getChildren().add(addListLabel);
        
        listsContainer.getChildren().add(addListPlaceholder);
    }

    private VBox createListColumn(KanbanList listData) {
        VBox column = new VBox();
        column.setPrefWidth(280.0);
        column.setSpacing(10.0);
        column.getStyleClass().add("list-column");

        // Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        Label titleLabel = new Label(listData.title);
        titleLabel.getStyleClass().add("list-header-text");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button optionsBtn = new Button("...");
        optionsBtn.setStyle("-fx-background-color: transparent;");
        header.getChildren().addAll(titleLabel, spacer, optionsBtn);

        // Contenedor de Tarjetas (el drop target de DnD principal)
        VBox cardsContainer = new VBox();
        cardsContainer.setSpacing(10.0);
        cardsContainer.setMinHeight(50); // Para poder dropear si está vacía
        VBox.setVgrow(cardsContainer, Priority.ALWAYS);

        for (KanbanCard cardData : listData.cards) {
            VBox cardNode = createCardNode(cardData, listData.id);
            cardsContainer.getChildren().add(cardNode);
        }

        // Configuración de Drag & Drop para esta columna
        setupDropTarget(cardsContainer, listData.id);

        // Add Card button at bottom
        Button addCardBtn = new Button("+ Añadir tarjeta");
        addCardBtn.setMaxWidth(Double.MAX_VALUE);
        addCardBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #707882; -fx-cursor: hand;");
        addCardBtn.setOnAction(e -> handleAddCard());

        column.getChildren().addAll(header, cardsContainer, addCardBtn);
        return column;
    }

    private VBox createCardNode(KanbanCard cardData, String sourceListId) {
        VBox cardNode = new VBox();
        cardNode.setSpacing(10.0);
        cardNode.getStyleClass().add("task-card");
        
        // Labels (etiquetas)
        HBox labelsBox = new HBox();
        labelsBox.setSpacing(5.0);
        for (LabelDto label : cardData.labels) {
            Region colorIndicator = new Region();
            colorIndicator.setPrefWidth(40.0);
            colorIndicator.setPrefHeight(8.0);
            colorIndicator.setStyle("-fx-background-color: " + label.colorHex() + "; -fx-background-radius: 4;");
            labelsBox.getChildren().add(colorIndicator);
        }

        // Título interactivo
        Text textTitle = new Text(cardData.title);
        textTitle.setWrappingWidth(240.0);
        textTitle.setStyle("-fx-font-size: 14px; -fx-fill: #172b4d;");

        cardNode.getChildren().addAll(labelsBox, textTitle);

        // Click handler (abre el modal)
        cardNode.setOnMouseClicked(e -> handleOpenCard());

        // Configuración de DRAG source
        cardNode.setOnDragDetected((MouseEvent event) -> {
            Dragboard db = cardNode.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(cardData.id + "|" + sourceListId); // ID + SourceListID
            db.setContent(content);
            cardNode.getStyleClass().add("dragging-card");
            event.consume();
        });

        cardNode.setOnDragDone((DragEvent event) -> {
            cardNode.getStyleClass().remove("dragging-card");
            if (event.getTransferMode() == TransferMode.MOVE) {
                // Se completó exitosamente, forzamos re-render
                renderBoard();
            }
            event.consume();
        });

        return cardNode;
    }

    private void setupDropTarget(VBox cardsContainer, String targetListId) {
        cardsContainer.setOnDragOver((DragEvent event) -> {
            if (event.getGestureSource() != cardsContainer && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        cardsContainer.setOnDragEntered((DragEvent event) -> {
            if (event.getGestureSource() != cardsContainer && event.getDragboard().hasString()) {
                cardsContainer.getStyleClass().add("drag-target");
            }
            event.consume();
        });

        cardsContainer.setOnDragExited((DragEvent event) -> {
            cardsContainer.getStyleClass().remove("drag-target");
            event.consume();
        });

        cardsContainer.setOnDragDropped((DragEvent event) -> {
            boolean success = false;
            Dragboard db = event.getDragboard();
            if (db.hasString()) {
                String payload = db.getString();
                String[] parts = payload.split("\\|");
                if (parts.length == 2) {
                    String cardId = parts[0];
                    String sourceListId = parts[1];
                    
                    if (!sourceListId.equals(targetListId)) {
                        moveCard(cardId, sourceListId, targetListId);
                    }
                    success = true;
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void moveCard(String cardId, String sourceListId, String targetListId) {
        KanbanCard cardToMove = null;
        
        // Quitar de source
        Optional<KanbanList> sourceListOpt = boardData.stream().filter(l -> l.id.equals(sourceListId)).findFirst();
        if (sourceListOpt.isPresent()) {
            KanbanList sourceList = sourceListOpt.get();
            Optional<KanbanCard> cardOpt = sourceList.cards.stream().filter(c -> c.id.equals(cardId)).findFirst();
            if (cardOpt.isPresent()) {
                cardToMove = cardOpt.get();
                sourceList.cards.remove(cardToMove);
            }
        }

        // Añadir a target
        if (cardToMove != null) {
            Optional<KanbanList> targetListOpt = boardData.stream().filter(l -> l.id.equals(targetListId)).findFirst();
            if (targetListOpt.isPresent()) {
                targetListOpt.get().cards.add(cardToMove);
                System.out.println("Tarjeta movida con éxito al drag & drop.");
                // TODO: Conectar con backend - Actualizar la lista destino de la tarjeta en la BD
            }
        }
    }

    // -- Manejo de Botones y Handlers (ya existentes, listos para la app real) --

    @FXML
    private void handleInvite() {
        System.out.println("Abriendo panel de invitaciones...");
    }

    @FXML
    private void handleFilterByLabel() {
        System.out.println("Abriendo filtro por etiquetas...");
    }

    @FXML
    private void handleRemoveFilter() {
        System.out.println("Eliminando filtro de etiqueta...");
    }

    @FXML
    private void handleClearFilters() {
        System.out.println("Limpiando todos los filtros...");
    }

    @FXML
    private void handleOpenCard() {
        System.out.println("Abriendo detalle de tarjeta...");
        MainLayoutController.getInstance().loadCenterView("Tarea");
    }

    @FXML
    private void handleAddCard() {
        System.out.println("Abriendo formulario para añadir tarjeta...");
        MainLayoutController.getInstance().loadCenterView("CrearTarjeta");
    }

    @FXML
    private void handleAddList() {
        System.out.println("Abriendo formulario para crear lista...");
        MainLayoutController.getInstance().loadCenterView("CrearLista");
    }
}