package umu.pds.api.adapters.out.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umu.pds.api.adapters.out.persistence.entities.TarjetaChecklistEntity;
import java.util.UUID;

@Repository
public interface SpringDataTarjetaChecklistRepository extends JpaRepository<TarjetaChecklistEntity, UUID> {}