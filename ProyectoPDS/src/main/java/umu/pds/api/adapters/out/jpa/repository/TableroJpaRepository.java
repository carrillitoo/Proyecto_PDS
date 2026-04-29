package umu.pds.api.adapters.out.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umu.pds.api.adapters.out.jpa.entity.TableroEntity;

import java.util.List;
import java.util.UUID;

@Repository // no hace falta crear los metodos porque vienen ya por herencia
// (si se hace Ctrl + "+" se puede ver todos los metodos que podemos
// sobreescribir)

// al final ha habido que meter una para buscar los tableros de un tablero  
public interface TableroJpaRepository extends JpaRepository<TableroEntity, UUID> {
    List<TableroEntity> findByEmailCreador(String emailCreador);
}