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
    @FXML
    private javafx.scene.control.MenuButton filterMenuBtn;
    @FXML
    private HBox activeFiltersContainer;
    @FXML
    private Button clearFiltersBtn;

    private String currentBoardId;
    private boolean isBoardCongelado = false;
    private TableroService tableroService = new TableroService();
    private TarjetaService tarjetaService = new TarjetaService();
    private TableroResponseDTO currentBoard;
    private java.util.Set<String> activeFilters = new java.util.HashSet<>();

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
            TableroResponseDTO tablero = tableroService.getTableroById(currentBoardId, GlobalState.getInstance().getUserEmail());
            if (tablero != null) {
                renderBoard(tablero);
            }
        } catch (Exception ex) {
            System.err.println("Error recuperando tablero avanzado: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void renderBoard(TableroResponseDTO tablero) {
        if (tablero == null || listsContainer == null) return;
        this.currentBoard = tablero;
        
        isBoardCongelado = "CONGELADO".equalsIgnoreCase(tablero.estado());

        updateLabelsMenu(tablero);
        renderLists(tablero);
    }

    private void updateLabelsMenu(TableroResponseDTO tablero) {
        if (filterMenuBtn == null) return;
        filterMenuBtn.getItems().clear();
        
        java.util.Set<String> uniqueLabels = new java.util.HashSet<>();
        
        if (tablero.etiquetas() != null) {
            for (umu.pds.dto.EtiquetaDTO et : tablero.etiquetas()) {
                uniqueLabels.add(et.nombre());
            }
        }
        
        if (tablero.listas() != null) {
            for (ListaTareasResponseDTO listData : tablero.listas()) {
                if (listData.tarjetas() != null) {
                    for (TarjetaResponseDTO card : listData.tarjetas()) {
                        if (card.etiquetas() != null) {
                            for (umu.pds.dto.EtiquetaDTO et : card.etiquetas()) {
                                uniqueLabels.add(et.nombre());
                            }
                        }
                    }
                }
            }
        }
        
        if (!uniqueLabels.isEmpty()) {
            for (String labelName : uniqueLabels) {
                javafx.scene.control.CheckMenuItem item = new javafx.scene.control.CheckMenuItem(labelName);
                item.setSelected(activeFilters.contains(labelName));
                item.setOnAction(e -> {
                    if (item.isSelected()) {
                        activeFilters.add(labelName);
                    } else {
                        activeFilters.remove(labelName);
                    }
                    applyFilters();
                });
                filterMenuBtn.getItems().add(item);
            }
        } else {
            javafx.scene.control.MenuItem emptyItem = new javafx.scene.control.MenuItem("Sin etiquetas disponibles");
            emptyItem.setDisable(true);
            filterMenuBtn.getItems().add(emptyItem);
        }
    }

    private void applyFilters() {
        if (activeFiltersContainer != null) {
            activeFiltersContainer.getChildren().clear();
            for (String filter : activeFilters) {
                Label chip = new Label(filter);
                chip.setStyle("-fx-background-color: #0079bf; -fx-text-fill: white; -fx-padding: 4 10; -fx-background-radius: 12; -fx-font-size: 12px;");
                activeFiltersContainer.getChildren().add(chip);
            }
        }
        
        if (clearFiltersBtn != null) {
            boolean hasFilters = !activeFilters.isEmpty();
            clearFiltersBtn.setVisible(hasFilters);
            clearFiltersBtn.setManaged(hasFilters);
        }
        
        if (currentBoard != null) {
            renderLists(currentBoard);
        }
    }

    private void renderLists(TableroResponseDTO tablero) {

        isBoardCongelado = "CONGELADO".equalsIgnoreCase(tablero.estado());

        listsContainer.getChildren().clear();

        // El placeholder de nueva lista (Leftmost)
        String role = GlobalState.getInstance().getCurrentUserRole();
        boolean hasElevatedPermissions = "PROPIETARIO".equals(role) || "ESCRITOR".equals(role);
        boolean canAddOrDelete = hasElevatedPermissions && !isBoardCongelado;
        
        if (canAddOrDelete) {
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

        // Separar las listas para ordenarlas
        ListaTareasResponseDTO completadasList = null;
        ListaTareasResponseDTO archivadasList = null;
        java.util.List<ListaTareasResponseDTO> normalLists = new java.util.ArrayList<>();

        if (tablero.listas() != null) {
            for (ListaTareasResponseDTO listData : tablero.listas()) {
                if ("Completadas".equalsIgnoreCase(listData.nombreLista())) {
                    completadasList = listData;
                } else if ("Archivadas".equalsIgnoreCase(listData.nombreLista())) {
                    archivadasList = listData;
                } else {
                    normalLists.add(listData);
                }
            }
        }

        // Listas normales
        for (ListaTareasResponseDTO listData : normalLists) {
            VBox listColumn = createListColumn(listData);
            listsContainer.getChildren().add(listColumn);
        }

        // Completadas y Archivadas al final
        if (completadasList != null) {
            VBox listColumn = createListColumn(completadasList);
            listsContainer.getChildren().add(listColumn);
        }
        if (archivadasList != null) {
            VBox listColumn = createListColumn(archivadasList);
            listsContainer.getChildren().add(listColumn);
        }
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
        
        String role = GlobalState.getInstance().getCurrentUserRole();
        boolean hasElevatedPermissions = "PROPIETARIO".equals(role) || "ESCRITOR".equals(role);
        boolean canAddOrDelete = hasElevatedPermissions && !isBoardCongelado;
        boolean canMoveCards = hasElevatedPermissions;
        
        if (!canAddOrDelete) {
            optionsBtn.setVisible(false);
            optionsBtn.setManaged(false);
        }
        header.getChildren().addAll(titleLabel, spacer, optionsBtn);

        // Contenedor de Tarjetas (el drop target de DnD principal)
        VBox cardsContainer = new VBox();
        cardsContainer.setSpacing(10.0);
        cardsContainer.setMinHeight(50); // Para poder dropear si está vacía
        VBox.setVgrow(cardsContainer, Priority.ALWAYS);

        if (listData.tarjetas() != null) {
            for (TarjetaResponseDTO cardData : listData.tarjetas()) {
                if (!matchesFilters(cardData)) continue;
                
                VBox cardNode = createCardNode(cardData, listData.nombreLista());
                cardsContainer.getChildren().add(cardNode);
            }
        }

        if (canMoveCards) {
            // Configuración de Drag & Drop para esta columna (Usamos el nombre de la lista como ID de lista)
            setupDropTarget(cardsContainer, listData.nombreLista());
        }

        boolean isSpecialList = listData.nombreLista().equalsIgnoreCase("Completadas") || listData.nombreLista().equalsIgnoreCase("Archivadas");

        if (canAddOrDelete && !isSpecialList) {
            // Add Card button at bottom
            Button addCardBtn = new Button("+ Añadir tarjeta");
            addCardBtn.setMaxWidth(Double.MAX_VALUE);
            addCardBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #707882; -fx-cursor: hand;");
            addCardBtn.setOnAction(e -> handleAddCard(listData.nombreLista()));
            column.getChildren().addAll(header, cardsContainer, addCardBtn);
        } else {
            column.getChildren().addAll(header, cardsContainer);
        }

        return column;
    }

    private VBox createCardNode(TarjetaResponseDTO cardData, String sourceListName) {
        VBox cardNode = new VBox();
        cardNode.setSpacing(10.0);
        cardNode.getStyleClass().add("task-card");
        
        // Etiquetas
        HBox labelsRow = new HBox(5);
        labelsRow.setAlignment(Pos.CENTER_LEFT);
        
        if (cardData.etiquetas() != null) {
            for (umu.pds.dto.EtiquetaDTO et : cardData.etiquetas()) {
                Label etLabel = new Label();
                etLabel.getStyleClass().add("card-label-bar");
                etLabel.setStyle("-fx-background-color: " + et.colorHex() + ";");
                labelsRow.getChildren().add(etLabel);
            }
        }
        
        cardNode.getChildren().add(labelsRow);

        HBox titleRow = new HBox(5);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        Text textTitle = new Text(cardData.titulo());
        textTitle.setWrappingWidth(210.0);
        textTitle.setStyle("-fx-font-size: 14px; -fx-fill: #172b4d;");
        
        Region titleSpacer = new Region();
        HBox.setHgrow(titleSpacer, Priority.ALWAYS);

        titleRow.getChildren().addAll(textTitle, titleSpacer);

        String role = GlobalState.getInstance().getCurrentUserRole();
        boolean hasElevatedPermissions = "PROPIETARIO".equals(role) || "ESCRITOR".equals(role);
        boolean canMoveCards = hasElevatedPermissions;

        if (canMoveCards && !sourceListName.equalsIgnoreCase("Completadas")) {
            Button completeBtn = new Button("✔");
            completeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #28a745; -fx-font-weight: bold; -fx-cursor: hand; -fx-font-size: 14px;");
            completeBtn.setOnAction(e -> {
                e.consume(); // Prevent card click event (opening details)
                handleCompleteCard(cardData.id(), sourceListName);
            });
            titleRow.getChildren().add(completeBtn);
        }

        cardNode.getChildren().add(titleRow);

        // Click handler (abre el modal de detalle)
        cardNode.setOnMouseClicked(e -> handleOpenCard(cardData, sourceListName));
        
        if (canMoveCards) {
            // Configuración de DRAG source
            cardNode.setOnDragDetected((MouseEvent event) -> {
                Dragboard db = cardNode.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(cardData.id() + "|" + sourceListName); // ID + SourceListName
                db.setContent(content);
                cardNode.getStyleClass().add("dragging-card");
                event.consume();
            });

            cardNode.setOnDragDone((DragEvent event) -> {
                cardNode.getStyleClass().remove("dragging-card");
                event.consume();
            });
        }

        return cardNode;
    }

    private void setupDropTarget(VBox cardsContainer, String targetListName) {
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
                    String sourceListName = parts[1];
                    
                    if (!sourceListName.equals(targetListName)) {
                        moveCard(cardId, sourceListName, targetListName);
                    }
                    success = true;
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void moveCard(String cardId, String sourceListName, String targetListName) {
        try {
            boolean exito = tarjetaService.moveCard(currentBoardId, cardId, sourceListName, targetListName);
            if (exito) {
                System.out.println("Tarjeta movida con éxito al drag & drop.");
                fetchAndRenderBoard();
            } else {
                System.err.println("La API reportó un fallo moviendo la tarjeta.");
            }
        } catch (Exception ex) {
            System.err.println("Error procesando moveCard en API: " + ex.getMessage());
            ex.printStackTrace();
            fetchAndRenderBoard();
        }
    }

    private void handleCompleteCard(String cardId, String sourceListName) {
        if ("Completadas".equalsIgnoreCase(sourceListName)) return;
        System.out.println("Completando tarjeta " + cardId + " moviéndola a 'Completadas'...");
        moveCard(cardId, sourceListName, "Completadas");
    }
    
    private boolean matchesFilters(TarjetaResponseDTO card) {
        if (activeFilters.isEmpty()) return true;
        if (card.etiquetas() == null) return false;
        for (umu.pds.dto.EtiquetaDTO et : card.etiquetas()) {
            if (activeFilters.contains(et.nombre())) return true;
        }
        return false;
    }

    // -- Manejo de Botones y Handlers (ya existentes, listos para la app real) --

    @FXML
    private void handleInvite() {
        System.out.println("Abriendo panel de invitaciones...");
        MainLayoutController.getInstance().loadCenterView("PermisosTablero");
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
        activeFilters.clear();
        if (filterMenuBtn != null) {
            for (javafx.scene.control.MenuItem item : filterMenuBtn.getItems()) {
                if (item instanceof javafx.scene.control.CheckMenuItem) {
                    ((javafx.scene.control.CheckMenuItem) item).setSelected(false);
                }
            }
        }
        applyFilters();
    }

    @FXML
    private void handleOpenCard(TarjetaResponseDTO cardData, String listName) {
        System.out.println("Abriendo detalle de tarjeta: " + cardData.titulo());
        GlobalState.getInstance().setCurrentCardId(cardData.id());
        GlobalState.getInstance().setCurrentCard(cardData);
        GlobalState.getInstance().setCurrentListName(listName);
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