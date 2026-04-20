package umu.pds.gui.services.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import umu.pds.dto.CrearTableroRequestDTO;
import umu.pds.dto.TableroResponseDTO;

public class TableroService {

    private final String BASE_URL = "http://localhost:8080/api/tableros";
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public TableroService() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public TableroResponseDTO createBoard(String nombre, String emailCreador) throws Exception {
        CrearTableroRequestDTO body = new CrearTableroRequestDTO(nombre, emailCreador);
        String json = objectMapper.writeValueAsString(body);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200 || response.statusCode() == 201) {
            return objectMapper.readValue(response.body(), TableroResponseDTO.class);
        }
        throw new RuntimeException("Error creando tablero: " + response.statusCode());
    }

    public TableroResponseDTO getBoard(String id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), TableroResponseDTO.class);
        }
        throw new RuntimeException("Error obteniendo tablero: " + response.statusCode());
    }

    public boolean freezeBoard(String id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id + "/congelar"))
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200;
    }

    public boolean unfreezeBoard(String id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id + "/descongelar"))
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200;
    }

    public boolean compactBoard(String id, int diasInactividad) throws Exception {
        String json = "{\"diasInactividad\": " + diasInactividad + "}";
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id + "/compactar"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200;
    }
}
