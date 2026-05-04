package umu.pds.api.adapters.out.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umu.pds.api.adapters.out.jpa.entity.UsuarioEntity;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, String> {
}

