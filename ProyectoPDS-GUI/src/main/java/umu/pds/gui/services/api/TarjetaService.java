package umu.pds.gui.services.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.UUID;

import umu.pds.dto.AddTarjetaRequestDTO;
import umu.pds.dto.TarjetaResponseDTO;

public class TarjetaService {

    private final String BASE_URL = "http://localhost:8080/api/tarjetas";
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public TarjetaService() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public TarjetaResponseDTO createCard(String tableroId, String listaDestino, String titulo, String descripcion,
            String tipo, String contenidoTarea) throws Exception {
        AddTarjetaRequestDTO requestDto = new AddTarjetaRequestDTO(titulo, descripcion, tipo, contenidoTarea);
        String json = objectMapper.writeValueAsString(requestDto);

        // POST /api/tableros/{idTablero}/listas/{nombreLista}/tarjetas
        String url = "http://localhost:8080/api/tableros/" + tableroId + "/listas/" + listaDestino + "/tarjetas";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200 || response.statusCode() == 201) {
            return objectMapper.readValue(response.body(), TarjetaResponseDTO.class);
        }
        throw new RuntimeException("Error creando tarjeta: " + response.statusCode());
    }

    public boolean addLabelToCard(String tarjetaId, String nombreEtiqueta, String colorHex) throws Exception {
        umu.pds.dto.AnadirEtiquetaCommandDTO body = new umu.pds.dto.AnadirEtiquetaCommandDTO(
                UUID.fromString(tarjetaId), nombreEtiqueta, colorHex);
        String json = objectMapper.writeValueAsString(body);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + tarjetaId + "/etiquetas"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200 || response.statusCode() == 201;
    }

    public boolean moveCard(String tableroId, String tarjetaId, String listaOrigen, String listaDestino)
            throws Exception {
        umu.pds.dto.MoverTarjetaRequestDTO requestDto = new umu.pds.dto.MoverTarjetaRequestDTO(listaOrigen,
                listaDestino);
        String json = objectMapper.writeValueAsString(requestDto);

        // Path matches backend:
        // /api/tableros/{idTablero}/tarjetas/{idTarjeta}/mover
        String url = "http://localhost:8080/api/tableros/" + tableroId + "/tarjetas/" + tarjetaId + "/mover";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            System.err.println("Error moviendo tarjeta: " + response.statusCode() + " - " + response.body());
        }
        return response.statusCode() == 200;
    }
}
