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


import umu.pds.dto.ListaTareasResponseDTO;
import umu.pds.dto.TableroResponseDTO;
import umu.pds.dto.TarjetaResponseDTO;
import umu.pds.gui.services.GlobalState;
import umu.pds.gui.services.api.TableroService;
import umu.pds.gui.services.api.TarjetaService;

public class TableroAvanzadoController {

    @FXML
    private HBox listsContainer;

    private String currentBoardId;
    private TableroService tableroService = new TableroService();
    private TarjetaService tarjetaService = new TarjetaService();

    @FXML
    public void initialize() {
        System.out.println("Inicializando Tablero Avanzado (Kanban Dinámico con DnD)...");
        currentBoardId = GlobalState.getInstance().getCurrentBoardId();
        if (currentBoardId != null) {
            fetchAndRenderBoard();
        } else {
            System.err.println("Advertencia: No hay un boardId seleccionado.");
        }
    }

    private void fetchAndRenderBoard() {
        try {
            TableroResponseDTO tablero = tableroService.getTableroById(currentBoardId);
            renderBoard(tablero);
        } catch (Exception ex) {
            System.err.println("Error recuperando tablero avanzado: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void renderBoard(TableroResponseDTO tablero) {
        if (listsContainer == null) return;
        listsContainer.getChildren().clear();

        if (tablero.listas() != null) {
            for (ListaTareasResponseDTO listData : tablero.listas()) {
                VBox listColumn = createListColumn(listData);
                listsContainer.getChildren().add(listColumn);
            }
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

    private VBox createListColumn(ListaTareasResponseDTO listData) {
        VBox column = new VBox();
        column.setPrefWidth(280.0);
        column.setSpacing(10.0);
        column.getStyleClass().add("list-column");

        // Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        Label titleLabel = new Label(listData.nombreLista());
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

        if (listData.tarjetas() != null) {
            for (TarjetaResponseDTO cardData : listData.tarjetas()) {
                VBox cardNode = createCardNode(cardData, listData.nombreLista());
                cardsContainer.getChildren().add(cardNode);
            }
        }

        // Configuración de Drag & Drop para esta columna (Usamos el nombre de la lista como ID de lista)
        setupDropTarget(cardsContainer, listData.nombreLista());

        // Add Card button at bottom
        Button addCardBtn = new Button("+ Añadir tarjeta");
        addCardBtn.setMaxWidth(Double.MAX_VALUE);
        addCardBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #707882; -fx-cursor: hand;");
        addCardBtn.setOnAction(e -> handleAddCard(listData.nombreLista()));

        column.getChildren().addAll(header, cardsContainer, addCardBtn);
        return column;
    }

    private VBox createCardNode(TarjetaResponseDTO cardData, String sourceListId) {
        VBox cardNode = new VBox();
        cardNode.setSpacing(10.0);
        cardNode.getStyleClass().add("task-card");
        
        // Labels mockeadas de momento (backend DTO no las incluye aún)
        HBox labelsBox = new HBox();
        labelsBox.setSpacing(5.0);
        // Podríamos pintar prioridades o tags simulados si es necesario.

        // Título interactivo
        Text textTitle = new Text(cardData.titulo());
        textTitle.setWrappingWidth(240.0);
        textTitle.setStyle("-fx-font-size: 14px; -fx-fill: #172b4d;");

        cardNode.getChildren().addAll(labelsBox, textTitle);

        // Click handler (abre el modal)
        cardNode.setOnMouseClicked(e -> handleOpenCard(cardData.id()));

        // Configuración de DRAG source
        cardNode.setOnDragDetected((MouseEvent event) -> {
            Dragboard db = cardNode.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(cardData.id() + "|" + sourceListId); // ID + SourceListID
            db.setContent(content);
            cardNode.getStyleClass().add("dragging-card");
            event.consume();
        });

        cardNode.setOnDragDone((DragEvent event) -> {
            cardNode.getStyleClass().remove("dragging-card");
            if (event.getTransferMode() == TransferMode.MOVE) {
                // Se completó exitosamente, no re-rendereamos enseguida porque el drop maneja la llamada API
                // fetchAndRenderBoard();
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
        try {
            boolean exito = tarjetaService.moveCard(currentBoardId, cardId, sourceListId, targetListId);
            if (exito) {
                System.out.println("Tarjeta movida con éxito al drag & drop.");
                // Recargamos los datos del servidor para pintar bien todo
                fetchAndRenderBoard();
            } else {
                System.err.println("La API reportó un fallo moviendo la tarjeta.");
            }
        } catch (Exception ex) {
            System.err.println("Error procesando moveCard en API: " + ex.getMessage());
            ex.printStackTrace();
            fetchAndRenderBoard(); // Refrescar para revertir movimiento visual en todo caso
        }
    }

    // -- Manejo de Botones y Handlers (ya existentes, listos para la app real) --

    @FXML
    private void handleInvite() {
        System.out.println("Abriendo panel de invitaciones...");
        MainLayoutController.getInstance().loadCenterView("InvitarMiembroDialog");
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
    private void handleOpenCard(String cardId) {
        System.out.println("Abriendo detalle de tarjeta con ID: " + cardId);
        GlobalState.getInstance().setCurrentCardId(cardId);
        MainLayoutController.getInstance().loadCenterView("Tarea");
    }

    @FXML
    private void handleAddCard(String listId) {
        System.out.println("Abriendo formulario para añadir tarjeta en la lista: " + listId);
        GlobalState.getInstance().setCurrentListId(listId);
        MainLayoutController.getInstance().loadCenterView("CrearTarjeta");
    }

    @FXML
    private void handleAddList() {
        System.out.println("Abriendo formulario para crear lista...");
        MainLayoutController.getInstance().loadCenterView("CrearLista");
    }
}