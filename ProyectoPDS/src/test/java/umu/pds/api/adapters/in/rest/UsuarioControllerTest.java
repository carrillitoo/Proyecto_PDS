package umu.pds.api.adapters.in.rest;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import umu.pds.api.domain.ports.in.SolicitarCodigoPort;
import umu.pds.api.domain.ports.in.ValidarCodigoPort;
import umu.pds.api.adapters.dto.SolicitarCodigoCommandDTO;
import umu.pds.api.adapters.dto.ValidarCodigoCommandDTO;
import umu.pds.api.domain.ports.in.ObtenerUsuarioPort;
import umu.pds.api.domain.ports.in.ActualizarUsuarioPort;
import umu.pds.api.domain.ports.in.ListarUsuariosPort;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SolicitarCodigoPort solicitarCodigoPort;

    @MockBean
    private ValidarCodigoPort validarCodigoPort;

    @MockBean
    private ObtenerUsuarioPort obtenerUsuarioPort;

    @MockBean
    private ActualizarUsuarioPort actualizarUsuarioPort;

    @MockBean
    private ListarUsuariosPort listarUsuariosPort;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void solicitarCodigo_DeberiaRetornar200() throws Exception {
        SolicitarCodigoCommandDTO cmd = new SolicitarCodigoCommandDTO("test@email.com");

        mockMvc.perform(post("/tablerellos/usuarios/login/solicitar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cmd)))
                .andExpect(status().isOk());
    }

    @Test
    void validarCodigo_DeberiaRetornar200_CuandoEsValido() throws Exception {
        ValidarCodigoCommandDTO cmd = new ValidarCodigoCommandDTO("test@email.com", "123456");

        when(validarCodigoPort.ejecutar(anyString(), anyString())).thenReturn(true);

        mockMvc.perform(post("/tablerellos/usuarios/login/validar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cmd)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void validarCodigo_DeberiaRetornar401_CuandoEsInvalido() throws Exception {
        ValidarCodigoCommandDTO cmd = new ValidarCodigoCommandDTO("test@email.com", "wrong");

        when(validarCodigoPort.ejecutar(anyString(), anyString())).thenReturn(false);

        mockMvc.perform(post("/tablerellos/usuarios/login/validar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cmd)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("false"));
    }
}
