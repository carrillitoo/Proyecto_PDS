package umu.pds.gui.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import umu.pds.dto.TableroResponseDTO;
import umu.pds.gui.services.GlobalState;
import umu.pds.gui.services.api.TableroService;

public class PermisosController {

    @FXML
    private VBox membersContainer;
    @FXML
    private TextField urlField;
    @FXML
    private VBox urlShareBox;
    @FXML
    private Button btnInvitar;

    private TableroService tableroService = new TableroService();

    @FXML
    public void initialize() {
        System.out.println("Cargando gestión de permisos...");
        loadBoardMembers();
    }

    private void loadBoardMembers() {
        try {
            String boardId = GlobalState.getInstance().getCurrentBoardId();
            if (boardId == null)
                return;

            TableroResponseDTO board = tableroService.getBoard(boardId, GlobalState.getInstance().getUserEmail());
            if (board == null)
                return;

            if (membersContainer != null) {
                membersContainer.getChildren().clear();

                if (board.miembros() != null) {
                    board.miembros().forEach((email, role) -> {
                        boolean isCreator = email.equals(board.emailCreador());
                        HBox userRow = createMemberRow(email, isCreator, role, false);
                        membersContainer.getChildren().add(userRow);
                    });
                }
                if (board.invitaciones() != null) {
                    board.invitaciones().forEach((email, role) -> {
                        HBox userRow = createMemberRow(email, false, role, true);
                        membersContainer.getChildren().add(userRow);
                    });
                }
            }

            if (urlField != null) {
                urlField.setText(board.url());
            }

            String currentUserEmail = GlobalState.getInstance().getUserEmail().toLowerCase();
            boolean isPropietario = board.emailCreador().toLowerCase().equals(currentUserEmail);
            if (!isPropietario && btnInvitar != null) {
                btnInvitar.setVisible(false);
                btnInvitar.setManaged(false);
            }

        } catch (Exception e) {
            System.err.println("Error cargando miembros: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private HBox createMemberRow(String email, boolean isCreator, String role, boolean isPending) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("user-row");
        row.setSpacing(10);
        row.setPadding(new javafx.geometry.Insets(10));

        // Member Info
        HBox infoBox = new HBox();
        infoBox.setPrefWidth(300);
        infoBox.setSpacing(15);
        infoBox.setAlignment(Pos.CENTER_LEFT);

        VBox textStack = new VBox();

        // Fetch User profile to get name
        String nombreMostrar = email.split("@")[0];
        try {
            umu.pds.dto.UsuarioResponseDTO perfil = new umu.pds.gui.services.api.UsuarioService().getPerfil(email);
            if (perfil != null && perfil.nombre() != null && !perfil.nombre().trim().isEmpty()) {
                nombreMostrar = perfil.nombre();
            }
        } catch (Exception e) {
            // Default to email prefix
        }

        Label nameLabel = new Label(nombreMostrar);
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label emailLabel = new Label(email);
        emailLabel.setStyle("-fx-text-fill: #707882; -fx-font-size: 12px;");
        textStack.getChildren().addAll(nameLabel, emailLabel);
        infoBox.getChildren().addAll(textStack);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Role
        String rolText = isCreator ? "Administrador"
                : (role != null ? role.substring(0, 1).toUpperCase() + role.substring(1).toLowerCase() : "Lector");
        Label roleBadge = new Label(rolText);
        roleBadge.setMinWidth(100);
        roleBadge.setAlignment(Pos.CENTER);

        // Estilo basado en el rol
        roleBadge.getStyleClass().add("role-badge");

        if (rolText.equals("Administrador")) {
            roleBadge.getStyleClass().add("role-admin");
        } else if (rolText.equals("Escritor")) {
            roleBadge.getStyleClass().add("role-escritor");
        } else {
            roleBadge.getStyleClass().add("role-lector");
        }

        row.getChildren().addAll(infoBox, spacer, roleBadge);
        return row;
    }

    @FXML
    private void handleInviteUser() {
        System.out.println("Cargando popup de invitación...");
        MainLayoutController.getInstance().loadCenterView("InvitarMiembroDialog");
    }

    @FXML
    private void handleRoleChange() {
        System.out.println("Cambiando rol de usuario...");
    }

    @FXML
    private void handleRevokeAccess() {
        System.out.println("Revocando acceso...");
    }

    @FXML
    private void handleCopyUrl() {
        System.out.println("URL copiada al portapapeles.");
    }
}