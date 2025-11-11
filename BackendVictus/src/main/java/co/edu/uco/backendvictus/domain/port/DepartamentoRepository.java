package co.edu.uco.backendvictus.domain.port;

import java.util.UUID;

import co.edu.uco.backendvictus.domain.model.Departamento;
import co.edu.uco.backendvictus.domain.model.filter.DepartamentoFilter;
import co.edu.uco.backendvictus.domain.specification.Specification;
import co.edu.uco.backendvictus.domain.specification.departamento.DepartamentoSpecifications;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DepartamentoRepository {

    Mono<Departamento> save(Departamento departamento);

    Mono<Departamento> findById(UUID id);

    Mono<Departamento> findByNombreIgnoreCase(String nombre);

    Flux<Departamento> findAll();

    Flux<Departamento> findAll(Specification<Departamento> specification);

    Mono<Void> deleteById(UUID id);

    default Flux<Departamento> findAll(final DepartamentoFilter filter) {
        return findAll(DepartamentoSpecifications.matchFilter(filter));
    }
}
