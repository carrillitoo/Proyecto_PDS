package umu.pds.gui.services.api.dto;
import java.util.List;

public record ListaTareasResponseDto(String nombreLista, int limiteN, List<TarjetaResponseDto> tarjetas, List<String> listasPreviasRequeridas) {}
