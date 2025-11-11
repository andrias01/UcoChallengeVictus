package co.edu.uco.backendvictus.domain.port;

import java.util.UUID;

import co.edu.uco.backendvictus.domain.model.Pais;
import co.edu.uco.backendvictus.domain.model.filter.PaisFilter;
import co.edu.uco.backendvictus.domain.specification.Specification;
import co.edu.uco.backendvictus.domain.specification.pais.PaisSpecifications;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PaisRepository {

    Mono<Pais> save(Pais pais);

    Mono<Pais> findById(UUID id);

    Mono<Pais> findByNombreIgnoreCase(String nombre);

    Flux<Pais> findAll();

    Flux<Pais> findAll(Specification<Pais> specification);

    Mono<Void> deleteById(UUID id);

    default Flux<Pais> findAll(final PaisFilter filter) {
        return findAll(PaisSpecifications.matchFilter(filter));
    }
}
