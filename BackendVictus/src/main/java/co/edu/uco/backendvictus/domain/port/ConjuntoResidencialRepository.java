package co.edu.uco.backendvictus.domain.port;

import java.util.UUID;

import co.edu.uco.backendvictus.domain.model.ConjuntoResidencial;
import co.edu.uco.backendvictus.domain.model.filter.ConjuntoResidencialFilter;
import co.edu.uco.backendvictus.domain.specification.Specification;
import co.edu.uco.backendvictus.domain.specification.conjunto.ConjuntoResidencialSpecifications;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ConjuntoResidencialRepository {

    Mono<ConjuntoResidencial> save(ConjuntoResidencial conjuntoResidencial);

    Mono<ConjuntoResidencial> findById(UUID id);

    Mono<ConjuntoResidencial> findByNombreIgnoreCase(String nombre);

    Flux<ConjuntoResidencial> findAll();

    Flux<ConjuntoResidencial> findAll(Specification<ConjuntoResidencial> specification);

    Mono<Void> deleteById(UUID id);

    default Flux<ConjuntoResidencial> findAll(final ConjuntoResidencialFilter filter) {
        return findAll(ConjuntoResidencialSpecifications.matchFilter(filter));
    }
}
