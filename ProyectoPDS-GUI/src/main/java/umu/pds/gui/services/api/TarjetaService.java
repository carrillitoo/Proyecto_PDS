package umu.pds.gui.services.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.UUID;

import umu.pds.dto.AddTarjetaRequestDTO;
import umu.pds.dto.TarjetaResponseDTO;
import umu.pds.dto.AnadirEtiquetaCommandDTO;
import umu.pds.dto.MoverTarjetaRequestDTO;

public class TarjetaService {

    private final String BASE_URL = "http://localhost:8080/tablerellos/tarjetas";
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public TarjetaService() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        this.objectMapper = new ObjectMapper().findAndRegisterModules();
    }

    public TarjetaResponseDTO createCard(String tableroId, String listaDestino, String titulo, String descripcion,
            String tipo, String contenidoTarea) throws Exception {
        AddTarjetaRequestDTO requestDto = new AddTarjetaRequestDTO(titulo, descripcion, tipo, contenidoTarea);
        String json = objectMapper.writeValueAsString(requestDto);

        String url = "http://localhost:8080/tablerellos/tableros/" + tableroId + "/listas/" + URLEncoder.encode(listaDestino, StandardCharsets.UTF_8).replace("+", "%20") + "/tarjetas";

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

    public TarjetaResponseDTO addLabelToCard(String tarjetaId, String nombreEtiqueta, String colorHex) throws Exception {
        AnadirEtiquetaCommandDTO body = new AnadirEtiquetaCommandDTO(UUID.fromString(tarjetaId), nombreEtiqueta, colorHex);
        String json = objectMapper.writeValueAsString(body);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + tarjetaId + "/etiquetas"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200 || response.statusCode() == 201) {
            return objectMapper.readValue(response.body(), TarjetaResponseDTO.class);
        }
        throw new Exception("Error al añadir etiqueta: " + response.body());
    }

    public boolean moveCard(String tableroId, String tarjetaId, String listaOrigen, String listaDestino)
            throws Exception {
        MoverTarjetaRequestDTO requestDto = new MoverTarjetaRequestDTO(listaOrigen, listaDestino);
        String json = objectMapper.writeValueAsString(requestDto);

        String url = "http://localhost:8080/tablerellos/tableros/" + tableroId + "/tarjetas/" + tarjetaId + "/mover";

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

    public boolean deleteCard(String tableroId, String nombreLista, String tarjetaId) throws Exception {
        String url = "http://localhost:8080/tablerellos/tableros/" + tableroId + "/listas/" + URLEncoder.encode(nombreLista, StandardCharsets.UTF_8).replace("+", "%20") + "/tarjetas/" + tarjetaId;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 204 || response.statusCode() == 200;
    }

    public boolean toggleChecklistItem(String tableroId, String nombreLista, String tarjetaId, String itemId) throws Exception {
        String url = "http://localhost:8080/tablerellos/tableros/" + tableroId + "/listas/" + URLEncoder.encode(nombreLista, StandardCharsets.UTF_8).replace("+", "%20") + "/tarjetas/" + tarjetaId + "/checklist/" + itemId + "/toggle";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200;
    }
}
