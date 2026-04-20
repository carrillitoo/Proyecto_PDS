package umu.pds.gui.services.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import com.fasterxml.jackson.databind.ObjectMapper;
import umu.pds.dto.SolicitarCodigoCommand;
import umu.pds.dto.ValidarCodigoCommand;

public class AuthService {
    private final String BASE_URL = "http://localhost:8080/api/usuarios";
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public AuthService() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public boolean solicitarCodigo(String email) throws Exception {
        SolicitarCodigoCommand requestBody = new SolicitarCodigoCommand(email);
        String json = objectMapper.writeValueAsString(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/login/solicitar"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200;
    }
    
    public boolean validarCodigo(String email, String codigo) throws Exception {
        
        ValidarCodigoCommand requestBody = new ValidarCodigoCommand(email, codigo);
        String json = objectMapper.writeValueAsString(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/login/validar"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

       //enviamos y esperando una respuesta
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            // devolvemos true encaso de codigo correcto
            return Boolean.parseBoolean(response.body());
        }
        return false;
    }
}