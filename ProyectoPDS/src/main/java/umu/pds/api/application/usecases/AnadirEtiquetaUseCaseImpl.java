package umu.pds.api.application.usecases;

import org.springframework.stereotype.Service;
import java.util.UUID;
import umu.pds.api.domain.exceptions.TarjetaNoEncontradaException;
import umu.pds.api.domain.models.Color;
import umu.pds.api.domain.models.Etiqueta;
import umu.pds.api.domain.models.Tarjeta;
import umu.pds.api.domain.ports.in.AnadirEtiquetaPort;
import umu.pds.api.domain.ports.out.TarjetaRepositoryPort;

@Service
public class AnadirEtiquetaUseCaseImpl implements AnadirEtiquetaPort {

    private final TarjetaRepositoryPort tarjetaRepositoryPort;

    public AnadirEtiquetaUseCaseImpl(final TarjetaRepositoryPort tarjetaRepositoryPort) {
        this.tarjetaRepositoryPort = tarjetaRepositoryPort;
    }

    @Override
    public Tarjeta ejecutar(UUID tarjetaId, String nombreEtiqueta, String colorHex) {
        // Recupera la tarjeta de la BD, y si no existe lanzamos nuestra excepción
        Tarjeta tarjeta = tarjetaRepositoryPort.buscarPorId(tarjetaId)
                .orElseThrow(() -> new TarjetaNoEncontradaException(tarjetaId));
 
        // Creamos los objetos, en este caso, el color y la etiqueta
        Color color = new Color(colorHex);
        Etiqueta nuevaEtiqueta = new Etiqueta(nombreEtiqueta, color);
 
        // Le decimos a la tarjeta que añada la etiqueta
        tarjeta.anadirEtiqueta(nuevaEtiqueta);
 
        //Guardam la tarjeta actualizada de vuelta a la BD
        return tarjetaRepositoryPort.guardar(tarjeta);
    }
}