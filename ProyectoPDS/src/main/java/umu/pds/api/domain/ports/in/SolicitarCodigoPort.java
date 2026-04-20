package umu.pds.api.domain.ports.in;

import umu.pds.dto.SolicitarCodigoCommand;

public interface SolicitarCodigoPort {
	void ejecutar(SolicitarCodigoCommand comando);
}
