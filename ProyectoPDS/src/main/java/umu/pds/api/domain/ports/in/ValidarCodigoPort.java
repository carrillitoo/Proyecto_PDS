package umu.pds.api.domain.ports.in;

import umu.pds.dto.ValidarCodigoCommand;

public interface ValidarCodigoPort {
	boolean ejecutar(ValidarCodigoCommand comando);
}
