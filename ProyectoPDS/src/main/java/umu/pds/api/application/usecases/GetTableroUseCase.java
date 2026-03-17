package umu.pds.api.application.usecases;


import umu.pds.api.domain.exceptions.TableroNoEncontradoException;
import umu.pds.api.domain.models.Tablero;
import umu.pds.api.domain.models.TableroId;
import umu.pds.api.domain.ports.out.TableroRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GetTableroUseCase {
	private final TableroRepositoryPort tableroRepository;
	
	public GetTableroUseCase(TableroRepositoryPort tableroRepository) {
		this.tableroRepository = tableroRepository;
	}
	
	//comando de orquestacion que devuelve un tablero en modo lectura
	public Tablero ejecutar(String tableroIdStr) {
		return tableroRepository.buscarPorId(TableroId.stringToTableroId(tableroIdStr))
                				.orElseThrow(() -> new TableroNoEncontradoException(tableroIdStr));
    }
}
