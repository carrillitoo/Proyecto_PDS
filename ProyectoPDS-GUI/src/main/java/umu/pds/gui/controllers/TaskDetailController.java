package umu.pds.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.time.format.DateTimeFormatter;
import umu.pds.dto.TarjetaResponseDTO;
import umu.pds.dto.ChecklistItemDTO;
import umu.pds.gui.services.GlobalState;
import umu.pds.gui.services.api.TarjetaService;

public class TaskDetailController {

    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private Text listNameText;
    @FXML private Text createdDateText;
    @FXML private HBox labelsBox;
    @FXML private VBox contentContainer;
    @FXML private Button deleteBtn;

    private final TarjetaService tarjetaService = new TarjetaService();

    @FXML
    public void initialize() {
        System.out.println("Cargando detalles de la tarjeta...");
        TarjetaResponseDTO card = GlobalState.getInstance().getCurrentCard();
        String listName = GlobalState.getInstance().getCurrentListName();

        if (card != null) {
            titleField.setText(card.titulo());
            descriptionArea.setText(card.descripcion() != null ? card.descripcion() : "");
            
            if (listName != null) {
                listNameText.setText("en la lista " + listName);
            }

            if (card.fechaCreacion() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d 'DE' MMMM, yyyy");
                createdDateText.setText("CREADO EL " + card.fechaCreacion().format(formatter).toUpperCase());
            }

            // Limpiar mocks visuales de FXML
            labelsBox.getChildren().clear(); 
            
            // Renderizar etiquetas reales si existen
            if (card.etiquetas() != null) {
                for (umu.pds.dto.EtiquetaDTO etiqueta : card.etiquetas()) {
                    javafx.scene.control.Label label = new javafx.scene.control.Label(etiqueta.nombre());
                    label.getStyleClass().add("badge-label");
                    label.setStyle("-fx-background-color: " + etiqueta.colorHex() + ";");
                    labelsBox.getChildren().add(label);
                }
            }

            // Añadir botón de "+" para gestionar etiquetas
            Button addLabelBtn = new Button("+");
            addLabelBtn.getStyleClass().add("add-label-badge");
            addLabelBtn.setOnAction(e -> handleOpenLabels());
            labelsBox.getChildren().add(addLabelBtn);

            // Renderizar contenido dinámico (Tarea o Checklist)
            contentContainer.getChildren().clear();
            
            if ("TAREA".equals(card.tipo())) {
                String content = card.contenidoTarea();
                if (content == null || content.isBlank() || content.equals("{}")) {
                    content = "Esta tarea no tiene contenido adicional.";
                }
                Label taskLabel = new Label(content);
                taskLabel.setWrapText(true);
                taskLabel.setStyle("-fx-font-size: 14px; -fx-padding: 10; -fx-background-color: #f4f5f7; -fx-background-radius: 5;");
                contentContainer.getChildren().add(taskLabel);
            } else if ("CHECKLIST".equals(card.tipo())) {
                if (card.itemsChecklist() != null && !card.itemsChecklist().isEmpty()) {
                    for (ChecklistItemDTO item : card.itemsChecklist()) {
                        CheckBox checkBox = new CheckBox(item.descripcion());
                        checkBox.setSelected(item.completado());
                        checkBox.getStyleClass().add("checklist-item");
                        
                        String role = GlobalState.getInstance().getCurrentUserRole();
                        boolean hasElevatedPermissions = "PROPIETARIO".equals(role) || "ESCRITOR".equals(role);
                        
                        if (!hasElevatedPermissions) {
                            checkBox.setDisable(true);
                        } else {
                            checkBox.setOnAction(e -> {
                                handleToggleChecklist(item, checkBox);
                            });
                        }

                        contentContainer.getChildren().add(checkBox);
                    }
                } else {
                    Label emptyLabel = new Label("Esta checklist no tiene elementos.");
                    emptyLabel.setStyle("-fx-text-fill: #707882; -fx-font-style: italic;");
                    contentContainer.getChildren().add(emptyLabel);
                }
            }
            
            String role = GlobalState.getInstance().getCurrentUserRole();
            boolean hasElevatedPermissions = "PROPIETARIO".equals(role) || "ESCRITOR".equals(role);
            
            if (!hasElevatedPermissions) {
                titleField.setEditable(false);
                descriptionArea.setEditable(false);
                if (deleteBtn != null) {
                    deleteBtn.setVisible(false);
                    deleteBtn.setManaged(false);
                }
                addLabelBtn.setVisible(false);
                addLabelBtn.setManaged(false);
            }
        }
    }

    private void handleToggleChecklist(ChecklistItemDTO item, CheckBox checkBox) {
        try {
            String boardId = GlobalState.getInstance().getCurrentBoardId();
            String listName = GlobalState.getInstance().getCurrentListName();
            TarjetaResponseDTO card = GlobalState.getInstance().getCurrentCard();
            
            if (boardId != null && listName != null && card != null) {
                tarjetaService.toggleChecklistItem(boardId, listName, card.id(), item.id());
            }
        } catch (Exception ex) {
            System.err.println("Error al alternar item del checklist: " + ex.getMessage());
            checkBox.setSelected(!checkBox.isSelected()); // Revertir en caso de error
        }
    }

    @FXML
    private void handleDelete() {
        System.out.println("Eliminando tarjeta...");
        String boardId = GlobalState.getInstance().getCurrentBoardId();
        String listName = GlobalState.getInstance().getCurrentListName();
        TarjetaResponseDTO card = GlobalState.getInstance().getCurrentCard();

        if (boardId != null && listName != null && card != null) {
            try {
                boolean deleted = tarjetaService.deleteCard(boardId, listName, card.id());
                if (deleted) {
                    System.out.println("Tarjeta eliminada con éxito.");
                    handleClose();
                } else {
                    System.err.println("No se pudo eliminar la tarjeta.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleClose() {
        System.out.println("Cerrando detalle de tarjeta...");
        MainLayoutController.getInstance().loadCenterView("BoardWorkspace");
    }

    @FXML
    private void handleOpenLabels() {
        System.out.println("Cargando vista de Gestión de Etiquetas...");
        MainLayoutController.getInstance().loadCenterView("GestionEtiquetas");
    }
}