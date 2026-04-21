package umu.pds.gui.services.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

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
            ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
            return mapper.readValue(response.body(), TableroResponseDTO.class);
        }

        throw new RuntimeException("Error creando tablero (" + response.statusCode() + "): " + response.body());
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

    public List<TableroResponseDTO> getDashboards(String userEmail) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "?usuario=" + userEmail))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
            return mapper.readValue(response.body(),
                    new com.fasterxml.jackson.core.type.TypeReference<List<TableroResponseDTO>>() {
                    });
        }
        throw new RuntimeException("Error obteniendo dashboard: " + response.statusCode());
    }

    public boolean shareBoard(String tableroId, String emailInvitado, String rol) throws Exception {
        umu.pds.dto.CompartirTableroCommandDTO body = new umu.pds.dto.CompartirTableroCommandDTO(emailInvitado,
                tableroId, rol);
        String json = objectMapper.writeValueAsString(body);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + tableroId + "/compartir"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200 || response.statusCode() == 201;
    }

    public boolean createList(String tableroId, String nombreLista, List<String> reglas) throws Exception {
        umu.pds.dto.CrearListaRequestDTO body = new umu.pds.dto.CrearListaRequestDTO(nombreLista, reglas);
        String json = objectMapper.writeValueAsString(body);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + tableroId + "/listas"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200 || response.statusCode() == 201;
    }

    public umu.pds.dto.TableroResponseDTO getTableroById(String idTablero) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + idTablero))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            // ObjectMapper with JSR-310 registration for Dates
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            return mapper.readValue(response.body(), umu.pds.dto.TableroResponseDTO.class);
        }
        throw new RuntimeException("Error obteniendo tablero individual (" + idTablero + "): " + response.statusCode());
    }

    public void compactarTablero(String idTablero, int diasInactividad) throws Exception {
        umu.pds.dto.CompactarTableroRequestDTO req = new umu.pds.dto.CompactarTableroRequestDTO(diasInactividad);
        String json = objectMapper.writeValueAsString(req);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + idTablero + "/compactar"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200 && response.statusCode() != 204) {
            throw new RuntimeException("Error compactando tablero: " + response.statusCode());
        }
    }

    public void congelarTablero(String idTablero) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + idTablero + "/congelar"))
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200 && response.statusCode() != 204) {
            throw new RuntimeException("Error congelando tablero");
        }
    }

    public void descongelarTablero(String idTablero) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + idTablero + "/descongelar"))
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200 && response.statusCode() != 204) {
            throw new RuntimeException("Error descongelando tablero");
        }
    }
}
