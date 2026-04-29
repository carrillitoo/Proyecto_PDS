package umu.pds.gui.services.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class UsuarioService {
    private final String BASE_URL = "http://localhost:8080/api/usuarios";
    private final HttpClient client;

    public UsuarioService() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    public umu.pds.dto.UsuarioResponseDTO getPerfil(String email) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + email))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.body(), umu.pds.dto.UsuarioResponseDTO.class);
        }
        throw new RuntimeException("Error obteniendo perfil: " + response.statusCode());
    }

    public umu.pds.dto.UsuarioResponseDTO updatePerfil(String email, String nuevoNombre) throws Exception {
        String json = "{\"nombre\": \"" + nuevoNombre + "\"}";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + email))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.body(), umu.pds.dto.UsuarioResponseDTO.class);
        }
        throw new RuntimeException("Error actualizando perfil: " + response.statusCode());
    }

    public java.util.List<umu.pds.dto.UsuarioResponseDTO> getAllUsuarios() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            umu.pds.dto.UsuarioResponseDTO[] array = mapper.readValue(response.body(), umu.pds.dto.UsuarioResponseDTO[].class);
            return java.util.Arrays.asList(array);
        }
        throw new RuntimeException("Error obteniendo usuarios (" + response.statusCode() + ")");
    }
}
