package umu.pds.api.adapters.in.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import umu.pds.dto.CompactarTableroRequestDTO;
import umu.pds.dto.CrearTableroRequestDTO;
import umu.pds.api.application.usecases.*;
import umu.pds.api.domain.exceptions.TableroNoEncontradoException;
import umu.pds.api.domain.models.EstadoTablero;
import umu.pds.api.domain.models.Tablero;
import umu.pds.api.domain.models.TableroId;
import umu.pds.api.domain.ports.in.ActualizarEtiquetaTableroPort;
import umu.pds.api.domain.ports.in.CompartirTableroPort;
import umu.pds.api.domain.ports.in.CrearEtiquetaTableroPort;
import umu.pds.api.domain.ports.in.EliminarEtiquetaTableroPort;
import umu.pds.api.ApiApplication;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TableroController.class) // Carga TableroController y GlobalExceptionHandler
@ContextConfiguration(classes = ApiApplication.class)
class TableroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // toodo mockeado
    @MockBean
    private CrearTableroUseCase crearTableroUseCase;
    @MockBean
    private CongelarTableroUseCase congelarTableroUseCase;
    @MockBean
    private DescongelarTableroUseCase descongelarTableroUseCase;
    @MockBean
    private GetTableroUseCase getTableroUseCase;
    @MockBean
    private CompactarTableroUseCase compactarTableroUseCase;
    @MockBean
    private ListarTablerosUseCase listarTablerosUseCase;
    @MockBean
    private CrearEtiquetaTableroPort crearEtiquetaTableroPort;
    @MockBean
    private ActualizarEtiquetaTableroPort actualizarEtiquetaTableroPort;
    @MockBean
    private EliminarEtiquetaTableroPort eliminarEtiquetaTableroPort;
    @MockBean
    private MoverTarjetaUseCase moverTarjetaUseCase;
    @MockBean
    private CompartirTableroPort compartirTableroPort;

    private Tablero tableroMock;
    private final String ID_TABLERO = UUID.randomUUID().toString();

    @BeforeEach
    void setUp() {
        // un tablero nuevo siempre
        tableroMock = Mockito.mock(Tablero.class);
        when(tableroMock.getId()).thenReturn(new TableroId(UUID.fromString(ID_TABLERO)));
        when(tableroMock.getNombre()).thenReturn("Tablero de Prueba");
        when(tableroMock.getEmailCreador()).thenReturn("sega@pds.com");
        when(tableroMock.getEstado()).thenReturn(EstadoTablero.ACTIVO);
        when(tableroMock.getUrl()).thenReturn("http://api/tableros/123");
    }

    @Test // creamos un tablero y nos devuelve 201
    void crearTablero_Return201Created() throws Exception {
        CrearTableroRequestDTO request = new CrearTableroRequestDTO("Mi Tablero", "sega@pds.com");
        when(crearTableroUseCase.ejecutar("Mi Tablero", "sega@pds.com")).thenReturn(tableroMock);

        mockMvc.perform(post("/api/tableros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Tablero de Prueba"))
                .andExpect(jsonPath("$.emailCreador").value("sega@pds.com"));
    }

    @Test // un email vacio peta y devuelve un 400 que es no valido
    void crearTablero_EmailInvalido_Return400BadRequest() throws Exception {
        CrearTableroRequestDTO request = new CrearTableroRequestDTO("Mi Tablero", "");

        mockMvc.perform(post("/api/tableros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Error"));
    }

    @Test // crear tablero sin nombre devuelve 400 BR
    void crearTablero_NombreVacio_Return400BadRequest() throws Exception {
        CrearTableroRequestDTO request = new CrearTableroRequestDTO("", "sega@pds.com");

        mockMvc.perform(post("/api/tableros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Error"));
    }

    @Test // get un tablero que existe y devuelve 200 OK
    void getTablero_Return200OK() throws Exception {
        when(getTableroUseCase.ejecutar(ID_TABLERO)).thenReturn(tableroMock);

        mockMvc.perform(get("/api/tableros/" + ID_TABLERO))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID_TABLERO))
                .andExpect(jsonPath("$.estado").value("ACTIVO"));
    }

    @Test // get un tablero que no devuelve un 404 NF
    void getTablero_NoExiste_Return404NotFound() throws Exception {
        // Simulamos que el UseCase lanza la excepción
        when(getTableroUseCase.ejecutar("inexistente"))
                .thenThrow(new TableroNoEncontradoException("inexistente"));

        mockMvc.perform(get("/api/tableros/inexistente"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found")); // Verifica el GlobalExceptionHandler
    }

    @Test // congelar un rablero 200 OK
    void congelarTablero_Return200OK() throws Exception {
        doNothing().when(congelarTableroUseCase).ejecutar(ID_TABLERO);

        mockMvc.perform(put("/api/tableros/" + ID_TABLERO + "/congelar"))
                .andExpect(status().isOk());
    }

    @Test // descongelar un rablero 200 OK
    void descongelarTablero_Return200OK() throws Exception {
        doNothing().when(descongelarTableroUseCase).ejecutar(ID_TABLERO);

        mockMvc.perform(put("/api/tableros/" + ID_TABLERO + "/descongelar"))
                .andExpect(status().isOk());
    }

    @Test // compactar un tablero que existe 200 OK
    void compactarTablero_Return200OK() throws Exception {
        CompactarTableroRequestDTO request = new CompactarTableroRequestDTO(5);
        doNothing().when(compactarTableroUseCase).ejecutar(anyString(), anyInt());

        mockMvc.perform(post("/api/tableros/" + ID_TABLERO + "/compactar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}