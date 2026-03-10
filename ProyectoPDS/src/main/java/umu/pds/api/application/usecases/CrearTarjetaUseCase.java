package umu.pds.api.application.usecases;

import org.springframework.stereotype.Service;
import umu.pds.api.application.dto.CrearTarjetaCommand;
import umu.pds.api.domain.models.Tarea;
import umu.pds.api.domain.models.Tarjeta;
import umu.pds.api.domain.models.TarjetaChecklist;
import umu.pds.api.domain.models.TarjetaTarea;
import umu.pds.api.domain.ports.in.CrearTarjetaPort;
import umu.pds.api.domain.ports.out.TarjetaRepositoryPort;


// Orquestador de la creación de Tarjetas

@Service
public class CrearTarjetaUseCase implements CrearTarjetaPort {

    private final TarjetaRepositoryPort tarjetaRepositoryPort;

    // Aplicamos inyección de dependencias por el constructor 
    public CrearTarjetaUseCase(final TarjetaRepositoryPort tarjetaRepositoryPort) {
        this.tarjetaRepositoryPort = tarjetaRepositoryPort;
    }

    @Override
    public Tarjeta ejecutar(CrearTarjetaCommand command) {
        Tarjeta nuevaTarjeta = switch (command.tipo()) {
            case TAREA -> new TarjetaTarea(
                    null, // El ID se genera dentro del constructor del domain
                    command.titulo(),
                    command.descripcion(),
                    new Tarea("Nueva tarea sin contenido") // Estado de inicio predeterminado
            );
            case CHECKLIST -> new TarjetaChecklist(
                    null,
                    command.titulo(),
                    command.descripcion()
            );
        };

        // Guardamos en la base de datos usando el puerto de salida
        return tarjetaRepositoryPort.guardar(nuevaTarjeta);
    }
}