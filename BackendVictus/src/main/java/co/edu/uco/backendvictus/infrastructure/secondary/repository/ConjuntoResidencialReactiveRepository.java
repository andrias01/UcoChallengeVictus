package co.edu.uco.backendvictus.infrastructure.secondary.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import co.edu.uco.backendvictus.infrastructure.secondary.entity.ConjuntoResidencialEntity;
import reactor.core.publisher.Mono;

public interface ConjuntoResidencialReactiveRepository
        extends ReactiveCrudRepository<ConjuntoResidencialEntity, UUID> {

    Mono<ConjuntoResidencialEntity> findByNombreIgnoreCase(String nombre);
}
