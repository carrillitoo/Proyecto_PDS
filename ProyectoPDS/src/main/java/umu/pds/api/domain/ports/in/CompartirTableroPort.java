package umu.pds.api.domain.ports.in;

import umu.pds.api.application.dto.CompartirTableroCommand;

//Puerto de entrada para compartir tableros
public interface CompartirTableroPort {
	void ejecutar(CompartirTableroCommand comando);
}
