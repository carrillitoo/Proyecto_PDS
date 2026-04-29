package umu.pds.api.application.usecases;


import umu.pds.api.domain.exceptions.TableroNoEncontradoException;
import umu.pds.api.domain.models.Tablero;
import umu.pds.api.domain.models.TableroId;
import umu.pds.api.domain.ports.out.TableroRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DescongelarTableroUseCaseImpl implements DescongelarTableroUseCase {
	private final TableroRepositoryPort tableroRepository;
	
	public DescongelarTableroUseCaseImpl(TableroRepositoryPort tableroRepository) {
		this.tableroRepository = tableroRepository;
	}
	
	//comando de orquestacion que descongela un tablero
	public void ejecutar(String tableroIdStr) {
		Tablero tablero = tableroRepository.buscarPorId(TableroId.stringToTableroId(tableroIdStr))
										   .orElseThrow(() -> new TableroNoEncontradoException(tableroIdStr));
		
		tablero.descongelar(); //UNfreeeze
		tableroRepository.guardar(tablero);
    }
}
