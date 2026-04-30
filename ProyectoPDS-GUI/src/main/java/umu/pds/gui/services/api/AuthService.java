package umu.pds.gui.services.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import com.fasterxml.jackson.databind.ObjectMapper;
import umu.pds.dto.SolicitarCodigoCommandDTO;
import umu.pds.dto.ValidarCodigoCommandDTO;

public class AuthService {
    private final String BASE_URL = "http://localhost:8080/tablerellos/usuarios";
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public AuthService() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        this.objectMapper = new ObjectMapper().findAndRegisterModules();
    }

    public boolean solicitarCodigo(String email) throws Exception {
        SolicitarCodigoCommandDTO requestBody = new SolicitarCodigoCommandDTO(email);
        String json = objectMapper.writeValueAsString(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/login/solicitar"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200 && response.statusCode() != 202) {
            System.err.println("Error solicitando código: " + response.statusCode() + " - " + response.body());
            return false;
        }
        return true;
    }
    
    public boolean validarCodigo(String email, String codigo) throws Exception {
        ValidarCodigoCommandDTO requestBody = new ValidarCodigoCommandDTO(email, codigo);
        String json = objectMapper.writeValueAsString(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/login/validar"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return Boolean.parseBoolean(response.body());
        } else {
            System.err.println("Error validando código: " + response.statusCode() + " - " + response.body());
        }
        return false;
    }
}