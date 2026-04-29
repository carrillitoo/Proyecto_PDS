package umu.pds.api.adapters.out.jpa;

import org.springframework.stereotype.Component;
import umu.pds.api.adapters.out.jpa.entity.TableroEntity;
import umu.pds.api.adapters.out.jpa.mapper.TableroMapper;
import umu.pds.api.adapters.out.jpa.repository.TableroJpaRepository;
import umu.pds.api.domain.models.Tablero;
import umu.pds.api.domain.models.TableroId;
import umu.pds.api.domain.ports.out.TableroRepositoryPort;

import java.util.List;
import java.util.Optional;

@Component // para que pueda meter spring los usecases
public class TableroRepositoryAdapterImpl implements TableroRepositoryPort {

    private final TableroJpaRepository jpaRepository;
    private final TableroMapper mapper;

    // para tener el mapper y el repositorio
    public TableroRepositoryAdapterImpl(TableroJpaRepository jpaRepository, TableroMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void guardar(Tablero tablero) {

        // cogemos el objeto y lo pasamos a entidad de db
        TableroEntity entity = mapper.toEntity(tablero);

        jpaRepository.save(entity);
    }

    @Override
    public Optional<Tablero> buscarPorId(TableroId id) {
        // buscamos con el uuid interno
        Optional<TableroEntity> entityOptional = jpaRepository.findById(id.valor());

        // necesitaremos pasar a dominio,
        return entityOptional.map(mapper::toDomain);
    }

    @Override
    public void eliminar(TableroId id) {
        // le decimos al repo que borre segun el uuid interno
        jpaRepository.deleteById(id.valor());
    }

    @Override
    public List<Tablero> buscarPorEmailCreador(String email) {
        return jpaRepository.findByEmailCreador(email).stream()
                .map(mapper::toDomain)
                .toList();
    }
}