package co.edu.uco.backendvictus.infrastructure.secondary.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import co.edu.uco.backendvictus.domain.model.Administrador;
import co.edu.uco.backendvictus.domain.model.Ciudad;
import co.edu.uco.backendvictus.domain.model.ConjuntoResidencial;
import co.edu.uco.backendvictus.domain.port.AdministradorRepository;
import co.edu.uco.backendvictus.domain.port.CiudadRepository;
import co.edu.uco.backendvictus.domain.port.ConjuntoResidencialRepository;
import co.edu.uco.backendvictus.domain.specification.Specification;
import co.edu.uco.backendvictus.infrastructure.secondary.entity.ConjuntoResidencialEntity;
import co.edu.uco.backendvictus.infrastructure.secondary.repository.ConjuntoResidencialReactiveRepository;
import co.edu.uco.backendvictus.infrastructure.secondary.mapper.ConjuntoResidencialEntityMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class ConjuntoResidencialRepositoryAdapter implements ConjuntoResidencialRepository {

    private final ConjuntoResidencialReactiveRepository conjuntoRepository;
    private final CiudadRepository ciudadRepository;
    private final AdministradorRepository administradorRepository;
    private final ConjuntoResidencialEntityMapper mapper;

    public ConjuntoResidencialRepositoryAdapter(final ConjuntoResidencialReactiveRepository conjuntoRepository,
            final CiudadRepository ciudadRepository, final AdministradorRepository administradorRepository,
            final ConjuntoResidencialEntityMapper mapper) {
        this.conjuntoRepository = conjuntoRepository;
        this.ciudadRepository = ciudadRepository;
        this.administradorRepository = administradorRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<ConjuntoResidencial> save(final ConjuntoResidencial conjuntoResidencial) {
        return conjuntoRepository.existsById(conjuntoResidencial.getId())
                .flatMap(exists -> {
                    final ConjuntoResidencialEntity entity = mapper.toEntity(conjuntoResidencial).markNew(!exists);
                    return conjuntoRepository.save(entity).flatMap(this::mapToDomain);
                });
    }

    @Override
    public Mono<ConjuntoResidencial> findById(final UUID id) {
        return conjuntoRepository.findById(id).flatMap(this::mapToDomain);
    }

    @Override
    public Mono<ConjuntoResidencial> findByNombreIgnoreCase(final String nombre) {
        return conjuntoRepository.findByNombreIgnoreCase(nombre).flatMap(this::mapToDomain);
    }

    @Override
    public Flux<ConjuntoResidencial> findAll() {
        return findAll(candidate -> true);
    }

    @Override
    public Flux<ConjuntoResidencial> findAll(final Specification<ConjuntoResidencial> specification) {
        return conjuntoRepository.findAll()
                .flatMap(this::mapToDomain)
                .filter(specification::isSatisfiedBy);
    }

    @Override
    public Mono<Void> deleteById(final UUID id) {
        return conjuntoRepository.deleteById(id);
    }

    private Mono<ConjuntoResidencial> mapToDomain(final ConjuntoResidencialEntity entity) {
        final Mono<Ciudad> ciudadMono = ciudadRepository.findById(entity.getCiudadId());
        final Mono<Administrador> administradorMono = administradorRepository.findById(entity.getAdministradorId());
        return Mono.zip(ciudadMono, administradorMono)
                .map(tuple -> mapper.toDomain(entity, tuple.getT1(), tuple.getT2()));
    }
}
