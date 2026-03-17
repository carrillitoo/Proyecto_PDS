package umu.pds.api.adapters.out.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umu.pds.api.adapters.out.jpa.entity.TableroEntity;

import java.util.UUID;

@Repository  //no hace falta crear los metodos porque vienen ya por herencia 
//(si se hace Ctrl + "+" se puede ver todos los metodos que podemos sobreescribir)
public interface TableroJpaRepository extends JpaRepository<TableroEntity, UUID> {}