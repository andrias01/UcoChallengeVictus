package co.edu.uco.backendvictus.infrastructure.secondary.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import co.edu.uco.backendvictus.domain.model.Administrador;
import co.edu.uco.backendvictus.domain.port.AdministradorRepository;
import co.edu.uco.backendvictus.domain.specification.Specification;
import co.edu.uco.backendvictus.infrastructure.secondary.entity.AdministradorEntity;
import co.edu.uco.backendvictus.infrastructure.secondary.mapper.AdministradorEntityMapper;
import co.edu.uco.backendvictus.infrastructure.secondary.repository.AdministradorReactiveRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class AdministradorRepositoryAdapter implements AdministradorRepository {

    private final AdministradorReactiveRepository administradorRepository;
    private final AdministradorEntityMapper mapper;

    public AdministradorRepositoryAdapter(final AdministradorReactiveRepository administradorRepository,
            final AdministradorEntityMapper mapper) {
        this.administradorRepository = administradorRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Administrador> save(final Administrador administrador) {
        return administradorRepository.existsById(administrador.getId())
                .flatMap(exists -> {
                    final AdministradorEntity entity = mapper.toEntity(administrador).markNew(!exists);
                    return administradorRepository.save(entity).map(mapper::toDomain);
                });
    }

    @Override
    public Mono<Administrador> findById(final UUID id) {
        return administradorRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Mono<Administrador> findByEmailIgnoreCase(final String email) {
        return administradorRepository.findByEmailIgnoreCase(email).map(mapper::toDomain);
    }

    @Override
    public Mono<Administrador> findByTelefono(final String telefono) {
        return administradorRepository.findByTelefono(telefono).map(mapper::toDomain);
    }

    @Override
    public Flux<Administrador> findAll() {
        return findAll(candidate -> true);
    }

    @Override
    public Flux<Administrador> findAll(final Specification<Administrador> specification) {
        return administradorRepository.findAll()
                .map(mapper::toDomain)
                .filter(specification::isSatisfiedBy);
    }

    @Override
    public Mono<Void> deleteById(final UUID id) {
        return administradorRepository.deleteById(id);
    }
}
