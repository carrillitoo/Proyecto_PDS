package umu.pds.api.application.usecases;

import umu.pds.api.domain.models.Tablero;
import umu.pds.api.domain.ports.out.TableroRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface ListarTablerosUseCase {
    public List<Tablero> ejecutar(String emailCreador);
}
