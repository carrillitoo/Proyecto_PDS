package umu.pds.api.domain.ports.in;

import umu.pds.dto.CompartirTableroCommandDTO;

//Puerto de entrada para compartir tableros
public interface CompartirTableroPort {
	void ejecutar(CompartirTableroCommandDTO comando);
}
