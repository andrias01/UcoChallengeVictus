package co.edu.uco.backendvictus.domain.port;

import java.util.UUID;

import co.edu.uco.backendvictus.domain.model.Ciudad;
import co.edu.uco.backendvictus.domain.model.filter.CiudadFilter;
import co.edu.uco.backendvictus.domain.specification.Specification;
import co.edu.uco.backendvictus.domain.specification.ciudad.CiudadSpecifications;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CiudadRepository {

    Mono<Ciudad> save(Ciudad ciudad);

    Mono<Ciudad> findById(UUID id);

    Mono<Ciudad> findByNombreIgnoreCase(String nombre);

    Flux<Ciudad> findAll();

    Flux<Ciudad> findAll(Specification<Ciudad> specification);

    Mono<Void> deleteById(UUID id);

    default Flux<Ciudad> findAll(final CiudadFilter filter) {
        return findAll(CiudadSpecifications.matchFilter(filter));
    }
}
