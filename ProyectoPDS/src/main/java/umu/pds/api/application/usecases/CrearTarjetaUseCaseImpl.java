package umu.pds.api.application.usecases;

import org.springframework.stereotype.Service;
import umu.pds.api.domain.models.TipoTarjeta;
import umu.pds.api.domain.models.Tarea;
import umu.pds.api.domain.models.Tarjeta;
import umu.pds.api.domain.models.TarjetaChecklist;
import umu.pds.api.domain.models.TarjetaTarea;
import umu.pds.api.domain.ports.in.CrearTarjetaPort;
import umu.pds.api.domain.ports.out.TarjetaRepositoryPort;


// Orquestador de la creación de Tarjetas

@Service
public class CrearTarjetaUseCaseImpl implements CrearTarjetaPort {

    private final TarjetaRepositoryPort tarjetaRepositoryPort;

    // Aplicamos inyección de dependencias por el constructor 
    public CrearTarjetaUseCaseImpl(final TarjetaRepositoryPort tarjetaRepositoryPort) {
        this.tarjetaRepositoryPort = tarjetaRepositoryPort;
    }

    @Override
    public Tarjeta ejecutar(String tableroId, String listaId, String titulo, String descripcion, String tipo, String contenido) {
        TipoTarjeta tipoEnum = TipoTarjeta.valueOf(tipo);
        Tarjeta nuevaTarjeta = switch (tipoEnum) {
            case TAREA -> new TarjetaTarea(
                    titulo,
                    descripcion,
                    new Tarea(contenido != null ? contenido : "Nueva tarea sin contenido") // Usamos el contenido proporcionado
            );
            case CHECKLIST -> new TarjetaChecklist(
                    titulo,
                    descripcion
            );
        };

        // Guardamos en la base de datos usando el puerto de salida
        return tarjetaRepositoryPort.guardar(nuevaTarjeta);
    }
}