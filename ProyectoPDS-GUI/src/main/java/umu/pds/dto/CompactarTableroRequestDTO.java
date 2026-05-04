package umu.pds.dto;

import jakarta.validation.constraints.Min;

public record CompactarTableroRequestDTO(
		@Min(value = 0, message = "Los días de inactividad no pueden ser negativos") int diasInactividad) {
}
