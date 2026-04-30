package umu.pds.api.adapters.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ActualizarUsuarioRequestDTO(@JsonProperty("nombre") String nombre) {}
