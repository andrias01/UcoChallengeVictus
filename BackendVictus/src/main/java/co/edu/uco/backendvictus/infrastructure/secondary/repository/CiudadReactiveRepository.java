package co.edu.uco.backendvictus.infrastructure.secondary.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import co.edu.uco.backendvictus.infrastructure.secondary.entity.CiudadEntity;
import reactor.core.publisher.Mono;

public interface CiudadReactiveRepository extends ReactiveCrudRepository<CiudadEntity, UUID> {

    Mono<CiudadEntity> findByNombreIgnoreCase(String nombre);
}
