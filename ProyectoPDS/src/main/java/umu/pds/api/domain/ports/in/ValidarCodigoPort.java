package umu.pds.api.domain.ports.in;

import umu.pds.api.application.dto.ValidarCodigoCommand;

public interface ValidarCodigoPort {
	boolean ejecutar(ValidarCodigoCommand comando);
}
