package co.edu.uco.backendvictus.infrastructure.secondary.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import co.edu.uco.backendvictus.domain.model.Departamento;
import co.edu.uco.backendvictus.domain.port.DepartamentoRepository;
import co.edu.uco.backendvictus.domain.specification.Specification;
import co.edu.uco.backendvictus.infrastructure.secondary.entity.DepartamentoEntity;
import co.edu.uco.backendvictus.infrastructure.secondary.repository.DepartamentoReactiveRepository;
import co.edu.uco.backendvictus.infrastructure.secondary.repository.PaisReactiveRepository;
import co.edu.uco.backendvictus.infrastructure.secondary.mapper.DepartamentoEntityMapper;
import co.edu.uco.backendvictus.infrastructure.secondary.mapper.PaisEntityMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class DepartamentoRepositoryAdapter implements DepartamentoRepository {

    private final DepartamentoReactiveRepository departamentoRepository;
    private final PaisReactiveRepository paisRepository;
    private final DepartamentoEntityMapper mapper;
    private final PaisEntityMapper paisMapper;

    public DepartamentoRepositoryAdapter(final DepartamentoReactiveRepository departamentoRepository,
            final PaisReactiveRepository paisRepository, final DepartamentoEntityMapper mapper,
            final PaisEntityMapper paisMapper) {
        this.departamentoRepository = departamentoRepository;
        this.paisRepository = paisRepository;
        this.mapper = mapper;
        this.paisMapper = paisMapper;
    }

    @Override
    public Mono<Departamento> save(final Departamento departamento) {
        return departamentoRepository.existsById(departamento.getId())
                .flatMap(exists -> {
                    final DepartamentoEntity entity = mapper.toEntity(departamento).markNew(!exists);
                    return departamentoRepository.save(entity).flatMap(this::mapToDomain);
                });
    }

    @Override
    public Mono<Departamento> findById(final UUID id) {
        return departamentoRepository.findById(id).flatMap(this::mapToDomain);
    }

    @Override
    public Mono<Departamento> findByNombreIgnoreCase(final String nombre) {
        return departamentoRepository.findByNombreIgnoreCase(nombre).flatMap(this::mapToDomain);
    }

    @Override
    public Flux<Departamento> findAll() {
        return findAll(candidate -> true);
    }

    @Override
    public Flux<Departamento> findAll(final Specification<Departamento> specification) {
        return departamentoRepository.findAll()
                .flatMap(this::mapToDomain)
                .filter(specification::isSatisfiedBy);
    }

    @Override
    public Mono<Void> deleteById(final UUID id) {
        return departamentoRepository.deleteById(id);
    }

    private Mono<Departamento> mapToDomain(final DepartamentoEntity entity) {
        return paisRepository.findById(entity.getPaisId()).map(paisMapper::toDomain)
                .map(pais -> mapper.toDomain(entity, pais));
    }
}
