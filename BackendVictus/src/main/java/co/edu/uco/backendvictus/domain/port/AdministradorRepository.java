package co.edu.uco.backendvictus.domain.port;

import java.util.UUID;

import co.edu.uco.backendvictus.domain.model.Administrador;
import co.edu.uco.backendvictus.domain.model.filter.AdministradorFilter;
import co.edu.uco.backendvictus.domain.specification.Specification;
import co.edu.uco.backendvictus.domain.specification.administrador.AdministradorSpecifications;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AdministradorRepository {

    Mono<Administrador> save(Administrador administrador);

    Mono<Administrador> findById(UUID id);

    Mono<Administrador> findByEmailIgnoreCase(String email);

    Mono<Administrador> findByTelefono(String telefono);

    Flux<Administrador> findAll();

    Flux<Administrador> findAll(Specification<Administrador> specification);

    Mono<Void> deleteById(UUID id);

    default Flux<Administrador> findAll(final AdministradorFilter filter) {
        return findAll(AdministradorSpecifications.matchFilter(filter));
    }
}
