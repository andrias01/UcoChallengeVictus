package co.edu.uco.backendvictus.infrastructure.secondary.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import co.edu.uco.backendvictus.domain.model.Ciudad;
import co.edu.uco.backendvictus.domain.model.Departamento;
import co.edu.uco.backendvictus.domain.port.CiudadRepository;
import co.edu.uco.backendvictus.domain.specification.Specification;
import co.edu.uco.backendvictus.infrastructure.secondary.entity.CiudadEntity;
import co.edu.uco.backendvictus.infrastructure.secondary.entity.DepartamentoEntity;
import co.edu.uco.backendvictus.infrastructure.secondary.repository.CiudadReactiveRepository;
import co.edu.uco.backendvictus.infrastructure.secondary.repository.DepartamentoReactiveRepository;
import co.edu.uco.backendvictus.infrastructure.secondary.repository.PaisReactiveRepository;
import co.edu.uco.backendvictus.infrastructure.secondary.mapper.CiudadEntityMapper;
import co.edu.uco.backendvictus.infrastructure.secondary.mapper.DepartamentoEntityMapper;
import co.edu.uco.backendvictus.infrastructure.secondary.mapper.PaisEntityMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class CiudadRepositoryAdapter implements CiudadRepository {

    private final CiudadReactiveRepository ciudadRepository;
    private final DepartamentoReactiveRepository departamentoRepository;
    private final PaisReactiveRepository paisRepository;
    private final CiudadEntityMapper mapper;
    private final DepartamentoEntityMapper departamentoMapper;
    private final PaisEntityMapper paisMapper;

    public CiudadRepositoryAdapter(final CiudadReactiveRepository ciudadRepository,
            final DepartamentoReactiveRepository departamentoRepository, final PaisReactiveRepository paisRepository,
            final CiudadEntityMapper mapper, final DepartamentoEntityMapper departamentoMapper,
            final PaisEntityMapper paisMapper) {
        this.ciudadRepository = ciudadRepository;
        this.departamentoRepository = departamentoRepository;
        this.paisRepository = paisRepository;
        this.mapper = mapper;
        this.departamentoMapper = departamentoMapper;
        this.paisMapper = paisMapper;
    }

    @Override
    public Mono<Ciudad> save(final Ciudad ciudad) {
        return ciudadRepository.existsById(ciudad.getId())
                .flatMap(exists -> {
                    final CiudadEntity entity = mapper.toEntity(ciudad).markNew(!exists);
                    return ciudadRepository.save(entity).flatMap(this::mapToDomain);
                });
    }

    @Override
    public Mono<Ciudad> findById(final UUID id) {
        return ciudadRepository.findById(id).flatMap(this::mapToDomain);
    }

    @Override
    public Mono<Ciudad> findByNombreIgnoreCase(final String nombre) {
        return ciudadRepository.findByNombreIgnoreCase(nombre).flatMap(this::mapToDomain);
    }

    @Override
    public Flux<Ciudad> findAll() {
        return findAll(candidate -> true);
    }

    @Override
    public Flux<Ciudad> findAll(final Specification<Ciudad> specification) {
        return ciudadRepository.findAll()
                .flatMap(this::mapToDomain)
                .filter(specification::isSatisfiedBy);
    }

    @Override
    public Mono<Void> deleteById(final UUID id) {
        return ciudadRepository.deleteById(id);
    }

    private Mono<Ciudad> mapToDomain(final CiudadEntity entity) {
        return loadDepartamento(entity.getDepartamentoId()).map(departamento -> mapper.toDomain(entity, departamento));
    }

    private Mono<Departamento> loadDepartamento(final UUID departamentoId) {
        return departamentoRepository.findById(departamentoId)
                .flatMap(depEntity -> paisRepository.findById(depEntity.getPaisId()).map(paisMapper::toDomain)
                        .map(pais -> departamentoMapper.toDomain(depEntity, pais)));
    }
}
