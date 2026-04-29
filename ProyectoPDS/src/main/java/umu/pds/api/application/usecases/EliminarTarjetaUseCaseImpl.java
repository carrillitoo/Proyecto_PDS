package umu.pds.api.application.usecases;

import java.util.UUID;

import umu.pds.api.domain.exceptions.TableroNoEncontradoException;
import umu.pds.api.domain.models.Tablero;
import umu.pds.api.domain.models.TableroId;
import umu.pds.api.domain.models.Tarjeta;
import umu.pds.api.domain.ports.out.TableroRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EliminarTarjetaUseCaseImpl implements EliminarTarjetaUseCase {
	private final TableroRepositoryPort tableroRepository;
	
	public EliminarTarjetaUseCaseImpl(TableroRepositoryPort tableroRepository) {
		this.tableroRepository = tableroRepository;
	}
	
	//comando de orquestacion que elimina una tarjeta de una  lista en un tablero
	public void ejecutar(String tableroIdStr, String nombreLista, String tarjetaIdStr) {
        
		Tablero tablero = tableroRepository.buscarPorId(TableroId.stringToTableroId(tableroIdStr))
            							   .orElseThrow(() -> new TableroNoEncontradoException(tableroIdStr));
        
		UUID tarjetaId = Tarjeta.stringToUUID(tarjetaIdStr);
		
        tablero.eliminarTarjeta(nombreLista, tarjetaId);

        tableroRepository.guardar(tablero);
    }
}
