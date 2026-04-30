package umu.pds.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EtiquetaDTO(
    @JsonProperty("nombre") String nombre,
    @JsonProperty("colorHex") String colorHex
) {}
