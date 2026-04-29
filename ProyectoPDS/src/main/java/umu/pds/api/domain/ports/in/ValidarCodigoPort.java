package umu.pds.api.domain.ports.in;

import umu.pds.dto.ValidarCodigoCommandDTO;

public interface ValidarCodigoPort {
	boolean ejecutar(ValidarCodigoCommandDTO comando);
}
