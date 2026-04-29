package umu.pds.api.application.usecases;

import java.util.UUID;

import umu.pds.api.domain.exceptions.LimiteListaExcedidoException;
import umu.pds.api.domain.exceptions.TableroNoEncontradoException;
import umu.pds.api.domain.models.Tablero;
import umu.pds.api.domain.models.TableroId;
import umu.pds.api.domain.models.Tarjeta;
import umu.pds.api.domain.ports.out.TableroRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MoverTarjetaUseCaseImpl implements MoverTarjetaUseCase {
	private final TableroRepositoryPort tableroRepository;
	
	public MoverTarjetaUseCaseImpl(TableroRepositoryPort tableroRepository) {
		this.tableroRepository = tableroRepository;
	}
	
	//comando de orquestacion que mueve unaa tarea de una lista a otra
	public void ejecutar(String tableroIdStr, String tarjetaIdStr, String listaOrigen, String listaDestino) throws LimiteListaExcedidoException{
        
		TableroId id = TableroId.stringToTableroId(tableroIdStr);
        Tablero tablero = tableroRepository.buscarPorId(id)
    									   .orElseThrow(() -> new TableroNoEncontradoException(tableroIdStr));
        
        UUID tarjetaId = Tarjeta.stringToUUID(tarjetaIdStr);
        
        tablero.moverTarjeta(tarjetaId, listaOrigen, listaDestino);

        tableroRepository.guardar(tablero);
    }
}
