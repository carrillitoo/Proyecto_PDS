package umu.pds.api.adapters.in.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import umu.pds.api.adapters.dto.AddTarjetaRequestDTO;
import umu.pds.api.adapters.dto.CrearListaRequestDTO;
import umu.pds.api.application.usecases.*;
import umu.pds.api.domain.exceptions.LimiteListaExcedidoException;
import umu.pds.api.domain.models.Color;
import umu.pds.api.domain.models.Etiqueta;
import umu.pds.api.domain.models.Tarjeta;
import umu.pds.api.domain.models.TipoTarjeta;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ListaTareasController.class)
class ListaTareasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Mockeamos los UseCases de las listas y tarjetas
    @MockBean
    private CrearListaTareasUseCase crearListaTareasUseCase;
    @MockBean
    private AddTarjetaListaUseCase addTarjetaListaUseCase;
    @MockBean
    private EliminarTarjetaUseCase eliminarTarjetaUseCase;
    @MockBean
    private CheckTarjetaCompletadaUseCase checkTarjetaCompletadaUseCase;
    @MockBean
    private ToggleChecklistItemUseCase toggleChecklistItemUseCase;

    private Tarjeta tarjetaMock;
    private final String ID_TABLERO = "tablero-123";
    private final String NOMBRE_LISTA = "TODO";
    private final String ID_TARJETA = UUID.randomUUID().toString();

    @BeforeEach
    void setUp() {
        // iwal que tablero, una tarjeta mocked
        tarjetaMock = new Tarjeta(UUID.fromString(ID_TARJETA), "Comprar pan", "Urgente", false, LocalDateTime.now()) {
            @Override
            protected void marcarComoCompletada() {
                this.completada = true;
            }

            @Override
            public TipoTarjeta getTipo() {
                return TipoTarjeta.TAREA;
            }
        };
        tarjetaMock.anadirEtiqueta(new Etiqueta("Importante", new Color("#FF0000")));
    }

    @Test // add lista a un tablero correctamente devuelve created
    void addLista_Return201Created() throws Exception {
        CrearListaRequestDTO request = new CrearListaRequestDTO(NOMBRE_LISTA, Collections.emptyList(), null);
        doNothing().when(crearListaTareasUseCase).ejecutar(ID_TABLERO, NOMBRE_LISTA, request.reglas(),
                request.limite());

        mockMvc.perform(post("/tablerellos/tableros/" + ID_TABLERO + "/listas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test // add tarjeta a una lista correctamente devuelve created
    void addTarjeta_Return201Created() throws Exception {
        AddTarjetaRequestDTO request = new AddTarjetaRequestDTO("Comprar pan", "Urgente", "TAREA", "Contenido general");
        when(addTarjetaListaUseCase.ejecutar(ID_TABLERO, NOMBRE_LISTA, "Comprar pan", "Urgente", "TAREA",
                "Contenido general"))
                .thenReturn(tarjetaMock);

        mockMvc.perform(post("/tablerellos/tableros/" + ID_TABLERO + "/listas/" + NOMBRE_LISTA + "/tarjetas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(ID_TARJETA))
                .andExpect(jsonPath("$.titulo").value("Comprar pan"));
    }

    @Test // add tarjeta a una lista sin nombre devuelve BR
    void addTarjeta_TituloVacio_Return400BadRequest() throws Exception {
        AddTarjetaRequestDTO request = new AddTarjetaRequestDTO("", "Urgente", "TAREA", "Contenido general");

        mockMvc.perform(post("/tablerellos/tableros/" + ID_TABLERO + "/listas/" + NOMBRE_LISTA + "/tarjetas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Error"));
    }

    @Test // add tarjeta que pasa limite devuelve conflicot de negocio con nuestra super
          // excepcion personalizada !Ex!cep!cion!
    void addTarjeta_LimiteExcedido_Return409Conflict() throws Exception {
        AddTarjetaRequestDTO request = new AddTarjetaRequestDTO("Comprar pan", "Urgente", "TAREA", "Contenido general");

        // Simulamos la violación de la regla de negocio
        when(addTarjetaListaUseCase.ejecutar(anyString(), anyString(), anyString(), anyString(),
                org.mockito.ArgumentMatchers.nullable(String.class),
                org.mockito.ArgumentMatchers.nullable(String.class)))
                .thenThrow(new LimiteListaExcedidoException(NOMBRE_LISTA));

        mockMvc.perform(post("/tablerellos/tableros/" + ID_TABLERO + "/listas/" + NOMBRE_LISTA + "/tarjetas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Conflict")); // Verificamos el manejador de excepciones
    }

    @Test // eliminar una tarjeta devuelve que no hay contenido ya, es decir que ya se ha
          // eliminado xd
    void eliminarTarjeta_Return204NoContent() throws Exception {
        doNothing().when(eliminarTarjetaUseCase).ejecutar(ID_TABLERO, NOMBRE_LISTA, ID_TARJETA);

        mockMvc.perform(
                delete("/tablerellos/tableros/" + ID_TABLERO + "/listas/" + NOMBRE_LISTA + "/tarjetas/" + ID_TARJETA))
                .andExpect(status().isNoContent()); // Verificamos que devuelve el 204
    }

    @Test // checkear tarjeta devuelve ok
    void completarTarjeta_Return200OK() throws Exception {
        doNothing().when(checkTarjetaCompletadaUseCase).ejecutar(ID_TABLERO, NOMBRE_LISTA, ID_TARJETA);

        mockMvc.perform(put("/tablerellos/tableros/" + ID_TABLERO + "/listas/" + NOMBRE_LISTA + "/tarjetas/"
                + ID_TARJETA + "/completar"))
                .andExpect(status().isOk());
    }
}