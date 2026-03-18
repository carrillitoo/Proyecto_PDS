package umu.pds.api.adapters.out.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umu.pds.api.adapters.out.jpa.entity.TarjetaEntity;

import java.util.UUID;

@Repository
public interface TarjetaJpaRepository extends JpaRepository<TarjetaEntity, UUID> {
}
