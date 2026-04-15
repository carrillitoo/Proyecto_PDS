package umu.pds.gui.services.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import umu.pds.gui.services.api.dto.AddTarjetaRequestDto;
import umu.pds.gui.services.api.dto.TarjetaResponseDto;

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

    public TarjetaResponseDto createCard(String titulo, String descripcion, String tipo) throws Exception {
        AddTarjetaRequestDto requestDto = new AddTarjetaRequestDto(titulo, descripcion, tipo, null);
        String json = objectMapper.writeValueAsString(requestDto);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200 || response.statusCode() == 201) {
            return objectMapper.readValue(response.body(), TarjetaResponseDto.class);
        }
        throw new RuntimeException("Error creando tarjeta: " + response.statusCode());
    }

    public boolean addLabelToCard(String tarjetaId, String nombreEtiqueta, String colorHex) throws Exception {
        String json = "{\"nombreEtiqueta\": \"" + nombreEtiqueta + "\", \"colorHex\": \"" + colorHex + "\"}";
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + tarjetaId + "/etiquetas"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200 || response.statusCode() == 201;
    }
}
