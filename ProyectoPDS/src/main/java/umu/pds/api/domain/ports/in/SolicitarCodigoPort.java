package umu.pds.api.domain.ports.in;

import umu.pds.api.adapters.in.rest.dto.SolicitarCodigoCommandDTO;

public interface SolicitarCodigoPort {
	void ejecutar(SolicitarCodigoCommandDTO comando);
}
