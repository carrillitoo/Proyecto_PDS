package umu.pds.api.application.usecases;


import umu.pds.api.domain.exceptions.AccesoDenegadoException;
import umu.pds.api.domain.exceptions.TableroNoEncontradoException;
import umu.pds.api.domain.models.Tablero;
import umu.pds.api.domain.models.TableroId;
import umu.pds.api.domain.ports.out.TableroRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GetTableroUseCaseImpl implements GetTableroUseCase {
	private final TableroRepositoryPort tableroRepository;
	
	public GetTableroUseCaseImpl(TableroRepositoryPort tableroRepository) {
		this.tableroRepository = tableroRepository;
	}
	
	//comando de orquestacion que devuelve un tablero en modo lectura
	public Tablero ejecutar(String tableroIdStr, String emailUsuario) {
		Tablero tablero = tableroRepository.buscarPorId(TableroId.stringToTableroId(tableroIdStr))
                				.orElseThrow(() -> new TableroNoEncontradoException(tableroIdStr));
		
		if (emailUsuario == null) {
			throw new AccesoDenegadoException("No tienes acceso a este tablero");
		}
		
		String emailNormalizado = emailUsuario.toLowerCase();
		boolean esCreador = tablero.getEmailCreador().equalsIgnoreCase(emailNormalizado);
		boolean esMiembro = tablero.getMiembros().containsKey(emailNormalizado);
		boolean esInvitado = tablero.getInvitaciones().containsKey(emailNormalizado);

		if (!esCreador && !esMiembro && !esInvitado) {
			throw new AccesoDenegadoException("No tienes acceso a este tablero");
		}
		
		return tablero;
    }
}
