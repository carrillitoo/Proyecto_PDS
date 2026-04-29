package umu.pds.api.domain.ports.in;

import umu.pds.api.adapters.in.rest.dto.ValidarCodigoCommandDTO;

public interface ValidarCodigoPort {
	boolean ejecutar(ValidarCodigoCommandDTO comando);
}
