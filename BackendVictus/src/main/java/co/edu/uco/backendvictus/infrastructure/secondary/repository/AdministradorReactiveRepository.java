package co.edu.uco.backendvictus.infrastructure.secondary.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import co.edu.uco.backendvictus.infrastructure.secondary.entity.AdministradorEntity;
import reactor.core.publisher.Mono;

public interface AdministradorReactiveRepository extends ReactiveCrudRepository<AdministradorEntity, UUID> {

    Mono<AdministradorEntity> findByEmailIgnoreCase(String email);

    Mono<AdministradorEntity> findByTelefono(String telefono);
}
