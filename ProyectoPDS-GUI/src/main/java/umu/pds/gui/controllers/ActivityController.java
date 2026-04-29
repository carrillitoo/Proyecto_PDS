package umu.pds.gui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import umu.pds.dto.TableroResponseDTO;
import umu.pds.dto.TrazaAccionResponseDTO;
import umu.pds.gui.services.GlobalState;
import umu.pds.gui.services.api.TableroService;

import java.util.List;

public class ActivityController {

    @FXML
    private ComboBox<TableroResponseDTO> tableroFilterCombo;
    @FXML
    private VBox historialContainer;
    @FXML
    private Label emptyLabel;

    private TableroService tableroService;

    @FXML
    public void initialize() {
        tableroService = new TableroService();

        tableroFilterCombo.setConverter(new StringConverter<TableroResponseDTO>() {
            @Override
            public String toString(TableroResponseDTO object) {
                return object == null ? "" : object.nombre();
            }

            @Override
            public TableroResponseDTO fromString(String string) {
                return null;
            }
        });

        loadTableros();
    }

    private void loadTableros() {
        try {
            String email = GlobalState.getInstance().getUserEmail();
            List<TableroResponseDTO> tableros = tableroService.getDashboards(email);
            ObservableList<TableroResponseDTO> obList = FXCollections.observableArrayList(tableros);
            tableroFilterCombo.setItems(obList);

            if (!obList.isEmpty()) {
                tableroFilterCombo.getSelectionModel().selectFirst();
                handleTableroFilterChange();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleTableroFilterChange() {
        TableroResponseDTO selected = tableroFilterCombo.getValue();
        if (selected == null) {
            historialContainer.getChildren().clear();
            emptyLabel.setVisible(true);
            emptyLabel.setManaged(true);
            return;
        }

        try {
            TableroResponseDTO details = tableroService.getTableroById(selected.id());
            renderHistory(details.historial());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void renderHistory(List<TrazaAccionResponseDTO> historial) {
        historialContainer.getChildren().clear();

        if (historial == null || historial.isEmpty()) {
            emptyLabel.setVisible(true);
            emptyLabel.setManaged(true);
            emptyLabel.setText("No hay actividad reciente en este tablero.");
            return;
        }

        emptyLabel.setVisible(false);
        emptyLabel.setManaged(false);

        for (TrazaAccionResponseDTO traza : historial) {
            HBox item = createHistoryItem(traza);
            historialContainer.getChildren().add(item);
        }
    }

    private HBox createHistoryItem(TrazaAccionResponseDTO traza) {
        HBox hbox = new HBox(15);
        hbox.setStyle(
                "-fx-padding: 15; -fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");

        Circle avatar = new Circle(18);
        avatar.setStyle("-fx-fill: #0079bf;");

        VBox content = new VBox(5);
        String desc = "Acción: " + traza.accion() + " sobre la tarjeta " + traza.tarjetaId();
        if (traza.listaOrigen() != null)
            desc += " (Origen: " + traza.listaOrigen() + ")";
        if (traza.listaDestino() != null)
            desc += " (Destino: " + traza.listaDestino() + ")";

        Text text = new Text(desc);
        text.setStyle("-fx-fill: #172b4d; -fx-font-size: 14px;");

        Label date = new Label(traza.fecha() != null ? traza.fecha().toString() : "");
        date.setStyle("-fx-text-fill: #5e6c84; -fx-font-size: 12px;");

        content.getChildren().addAll(text, date);
        hbox.getChildren().addAll(avatar, content);

        return hbox;
    }
}