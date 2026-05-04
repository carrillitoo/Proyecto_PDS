package umu.pds.api.domain.ports.in;

import java.util.UUID;

public interface EliminarEtiquetaTableroPort {
    void ejecutar(UUID tableroId, String nombreEtiqueta);
}
