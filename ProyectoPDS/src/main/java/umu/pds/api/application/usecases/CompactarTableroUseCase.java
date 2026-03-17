package umu.pds.api.application.usecases;


import umu.pds.api.domain.exceptions.TableroNoEncontradoException;
import umu.pds.api.domain.models.Tablero;
import umu.pds.api.domain.models.TableroId;
import umu.pds.api.domain.ports.out.TableroRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CompactarTableroUseCase {
	private final TableroRepositoryPort tableroRepository;
	
	public CompactarTableroUseCase(TableroRepositoryPort tableroRepository) {
		this.tableroRepository = tableroRepository;
	}
	
	//comando de orquestacion que compacta el tablero segun una fecha
	public void ejecutar(String tableroIdStr, int diasAntiguedad) {
		Tablero tablero = tableroRepository.buscarPorId(TableroId.stringToTableroId(tableroIdStr))
                .orElseThrow(() -> new TableroNoEncontradoException(tableroIdStr));

        // llamamos a la func general para que haga to-do el trabajo en el core de dominio
        tablero.compactarTablero(diasAntiguedad);

        tableroRepository.guardar(tablero);
    }
}
