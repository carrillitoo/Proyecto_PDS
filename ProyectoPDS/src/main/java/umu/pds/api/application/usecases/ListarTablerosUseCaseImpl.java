package umu.pds.api.application.usecases;

import umu.pds.api.domain.models.Tablero;
import umu.pds.api.domain.ports.out.TableroRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ListarTablerosUseCaseImpl implements ListarTablerosUseCase {
	private final TableroRepositoryPort tableroRepository;
	
	public ListarTablerosUseCaseImpl(TableroRepositoryPort tableroRepository) {
		this.tableroRepository = tableroRepository;
	}
	
	//devuelve todos los tableros en los que participa el usuario
	public List<Tablero> ejecutar(String email) {
		return tableroRepository.buscarPorEmailUsuario(email != null ? email.toLowerCase() : null);
    }
}
