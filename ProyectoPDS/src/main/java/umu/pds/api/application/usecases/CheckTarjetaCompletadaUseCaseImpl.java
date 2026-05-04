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
public class CheckTarjetaCompletadaUseCaseImpl implements CheckTarjetaCompletadaUseCase {
	private final TableroRepositoryPort tableroRepository;
	
	public CheckTarjetaCompletadaUseCaseImpl(TableroRepositoryPort tableroRepository) {
		this.tableroRepository = tableroRepository;
	}
	
	//comando de orquestacion que marca una tarea como done y se va a la lista especial
	public void ejecutar(String tableroIdStr, String nombreLista, String tarjetaIdStr) {
        
		Tablero tablero = tableroRepository.buscarPorId(TableroId.stringToTableroId(tableroIdStr))
            							   .orElseThrow(() -> new TableroNoEncontradoException(tableroIdStr));
        
		UUID tarjetaId = Tarjeta.stringToUUID(tarjetaIdStr);
		
        tablero.checkTarjetaCompletada(tarjetaId, nombreLista);

        tableroRepository.guardar(tablero);
    }
}
