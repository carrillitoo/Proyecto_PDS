package umu.pds.api.domain.ports.out;

import java.util.List;
import java.util.Optional;

import umu.pds.api.domain.models.Tablero;
import umu.pds.api.domain.models.TableroId;

public interface TableroRepositoryPort {
    void guardar(Tablero tablero);

    Optional<Tablero> buscarPorId(TableroId id);

    void eliminar(TableroId id);

    List<Tablero> buscarPorEmailCreador(String email);
    List<Tablero> buscarPorEmailUsuario(String email);
}