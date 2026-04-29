package umu.pds.api.domain.ports.in;

import umu.pds.dto.SolicitarCodigoCommandDTO;

public interface SolicitarCodigoPort {
	void ejecutar(SolicitarCodigoCommandDTO comando);
}
