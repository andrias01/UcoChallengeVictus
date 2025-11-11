package co.edu.uco.backendvictus.infrastructure.secondary.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import co.edu.uco.backendvictus.domain.model.Pais;
import co.edu.uco.backendvictus.domain.port.PaisRepository;
import co.edu.uco.backendvictus.domain.specification.Specification;
import co.edu.uco.backendvictus.infrastructure.secondary.entity.PaisEntity;
import co.edu.uco.backendvictus.infrastructure.secondary.mapper.PaisEntityMapper;
import co.edu.uco.backendvictus.infrastructure.secondary.repository.PaisReactiveRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class PaisRepositoryAdapter implements PaisRepository {

    private final PaisReactiveRepository repository;
    private final PaisEntityMapper mapper;

    public PaisRepositoryAdapter(final PaisReactiveRepository repository, final PaisEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Pais> save(final Pais pais) {
        return repository.existsById(pais.getId())
                .flatMap(exists -> {
                    final PaisEntity entity = mapper.toEntity(pais).markNew(!exists);
                    return repository.save(entity).map(mapper::toDomain);
                });
    }

    @Override
    public Mono<Pais> findById(final UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Mono<Pais> findByNombreIgnoreCase(final String nombre) {
        return repository.findByNombreIgnoreCase(nombre).map(mapper::toDomain);
    }

    @Override
    public Flux<Pais> findAll() {
        return findAll(candidate -> true);
    }

    @Override
    public Flux<Pais> findAll(final Specification<Pais> specification) {
        return repository.findAll()
                .map(mapper::toDomain)
                .filter(specification::isSatisfiedBy);
    }

    @Override
    public Mono<Void> deleteById(final UUID id) {
        return repository.deleteById(id);
    }
}
