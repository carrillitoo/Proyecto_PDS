package umu.pds.api.adapters.out.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umu.pds.api.adapters.out.persistence.entities.TarjetaTareaEntity;
import java.util.UUID;

@Repository
public interface SpringDataTarjetaTareaRepository extends JpaRepository<TarjetaTareaEntity, UUID> {}