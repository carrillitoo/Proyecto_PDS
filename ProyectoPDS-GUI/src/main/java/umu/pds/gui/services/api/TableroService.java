package umu.pds.gui.services.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

import umu.pds.dto.CrearTableroRequestDTO;
import umu.pds.dto.TableroResponseDTO;
import umu.pds.dto.CrearListaRequestDTO;
import umu.pds.dto.CompactarTableroRequestDTO;
import umu.pds.dto.CompartirTableroCommandDTO;

public class TableroService {

    private final String BASE_URL = "http://localhost:8080/tablerellos/tableros";
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public TableroService() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        this.objectMapper = new ObjectMapper().findAndRegisterModules();
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

        throw new RuntimeException("Error creando tablero (" + response.statusCode() + "): " + response.body());
    }

    public TableroResponseDTO getBoard(String id, String userEmail) throws Exception {
        String encodedEmail = java.net.URLEncoder.encode(userEmail, java.nio.charset.StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id + "?usuario=" + encodedEmail))
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
        CompactarTableroRequestDTO req = new CompactarTableroRequestDTO(diasInactividad);
        String json = objectMapper.writeValueAsString(req);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id + "/compactar"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200 || response.statusCode() == 204;
    }

    public List<TableroResponseDTO> getDashboards(String userEmail) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "?usuario=" + userEmail))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<TableroResponseDTO>>() {});
        }
        throw new RuntimeException("Error obteniendo dashboards: " + response.statusCode());
    }

    public boolean shareBoard(String tableroId, String emailInvitado, String rol) throws Exception {
        CompartirTableroCommandDTO body = new CompartirTableroCommandDTO(emailInvitado, tableroId, rol);
        String json = objectMapper.writeValueAsString(body);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + tableroId + "/compartir"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200 || response.statusCode() == 201;
    }

    public boolean createList(String tableroId, String nombreLista, List<String> reglas, Integer limite) throws Exception {
        CrearListaRequestDTO body = new CrearListaRequestDTO(nombreLista, reglas, limite);
        String json = objectMapper.writeValueAsString(body);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + tableroId + "/listas"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200 || response.statusCode() == 201;
    }

    public TableroResponseDTO getTableroById(String idTablero, String userEmail) throws Exception {
        return getBoard(idTablero, userEmail);
    }

    public void compactarTablero(String idTablero, int diasInactividad) throws Exception {
        if (!compactBoard(idTablero, diasInactividad)) {
            throw new RuntimeException("Error compactando tablero");
        }
    }

    public void congelarTablero(String idTablero) throws Exception {
        if (!freezeBoard(idTablero)) {
            throw new RuntimeException("Error congelando tablero");
        }
    }

    public void descongelarTablero(String idTablero) throws Exception {
        if (!unfreezeBoard(idTablero)) {
            throw new RuntimeException("Error descongelando tablero");
        }
    }

    public boolean createEtiqueta(String tableroId, String nombre, String colorHex) throws Exception {
        String json = "{\"nombre\": \"" + nombre + "\", \"colorHex\": \"" + colorHex + "\"}";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + tableroId + "/etiquetas"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200 || response.statusCode() == 201;
    }

    public boolean updateEtiqueta(String tableroId, String oldNombre, String nuevoNombre, String nuevoColorHex) throws Exception {
        String json = "{\"nuevoNombre\": \"" + nuevoNombre + "\", \"nuevoColorHex\": \"" + nuevoColorHex + "\"}";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + tableroId + "/etiquetas/" + oldNombre.replace(" ", "%20")))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200;
    }

    public boolean deleteEtiqueta(String tableroId, String nombreEtiqueta) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + tableroId + "/etiquetas/" + nombreEtiqueta.replace(" ", "%20")))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200 || response.statusCode() == 204;
    }

    public boolean acceptInvitation(String tableroId, String email) throws Exception {
        String encodedEmail = java.net.URLEncoder.encode(email, java.nio.charset.StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + tableroId + "/unirse?email=" + encodedEmail))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200;
    }
}
