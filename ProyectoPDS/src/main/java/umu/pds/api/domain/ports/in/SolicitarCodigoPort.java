package umu.pds.api.domain.ports.in;

import umu.pds.api.application.dto.SolicitarCodigoCommand;

public interface SolicitarCodigoPort {
	void ejecutar(SolicitarCodigoCommand comando);
}
