package umu.pds.api.adapters.in.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import umu.pds.api.domain.models.Tarea;
import umu.pds.api.domain.models.Tarjeta;
import umu.pds.api.domain.models.TarjetaTarea;
import umu.pds.api.domain.ports.in.AnadirEtiquetaPort;
import umu.pds.dto.AnadirEtiquetaCommandDTO;
import umu.pds.dto.AnadirEtiquetaWebRequestDTO;

@WebMvcTest(TarjetaController.class)
public class TarjetaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnadirEtiquetaPort anadirEtiquetaPort;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void anadirEtiqueta_DeberiaRetornar200() throws Exception {
        UUID id = UUID.randomUUID();
        AnadirEtiquetaWebRequestDTO request = new AnadirEtiquetaWebRequestDTO("Backend", "#0000FF");
        Tarjeta tarjeta = new TarjetaTarea(id, "Tarea", "Desc", false, LocalDateTime.now(), new Tarea("Tarea"));

        when(anadirEtiquetaPort.ejecutar(any(AnadirEtiquetaCommandDTO.class))).thenReturn(tarjeta);

        mockMvc.perform(post("/api/tarjetas/" + id + "/etiquetas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
