package co.edu.uco.backendvictus.infrastructure.secondary.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import co.edu.uco.backendvictus.infrastructure.secondary.entity.PaisEntity;
import reactor.core.publisher.Mono;

public interface PaisReactiveRepository extends ReactiveCrudRepository<PaisEntity, UUID> {

    Mono<PaisEntity> findByNombreIgnoreCase(String nombre);
}
