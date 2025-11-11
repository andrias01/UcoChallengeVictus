package co.edu.uco.backendvictus.application.usecase.conjunto;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.backendvictus.application.dto.conjunto.ConjuntoCreateRequest;
import co.edu.uco.backendvictus.application.dto.conjunto.ConjuntoResponse;
import co.edu.uco.backendvictus.application.mapper.ConjuntoApplicationMapper;
import co.edu.uco.backendvictus.application.usecase.UseCase;
import co.edu.uco.backendvictus.crosscutting.exception.ApplicationException;
import co.edu.uco.backendvictus.domain.model.Administrador;
import co.edu.uco.backendvictus.domain.model.Ciudad;
import co.edu.uco.backendvictus.domain.model.ConjuntoResidencial;
import co.edu.uco.backendvictus.domain.port.AdministradorRepository;
import co.edu.uco.backendvictus.domain.port.CiudadRepository;
import co.edu.uco.backendvictus.domain.port.ConjuntoResidencialRepository;
import reactor.core.publisher.Mono;

@Service
public class CreateConjuntoUseCase implements UseCase<ConjuntoCreateRequest, ConjuntoResponse> {

    private final ConjuntoResidencialRepository conjuntoRepository;
    private final CiudadRepository ciudadRepository;
    private final AdministradorRepository administradorRepository;
    private final ConjuntoApplicationMapper mapper;

    public CreateConjuntoUseCase(final ConjuntoResidencialRepository conjuntoRepository,
            final CiudadRepository ciudadRepository, final AdministradorRepository administradorRepository,
            final ConjuntoApplicationMapper mapper) {
        this.conjuntoRepository = conjuntoRepository;
        this.ciudadRepository = ciudadRepository;
        this.administradorRepository = administradorRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<ConjuntoResponse> execute(final ConjuntoCreateRequest request) {
        final Mono<Ciudad> ciudadMono = ciudadRepository.findById(request.ciudadId())
                .switchIfEmpty(Mono.error(new ApplicationException("Ciudad no encontrada")));
        final Mono<Administrador> administradorMono = administradorRepository.findById(request.administradorId())
                .switchIfEmpty(Mono.error(new ApplicationException("Administrador no encontrado")));

        return Mono.zip(ciudadMono, administradorMono)
                .flatMap(tuple -> Mono.defer(() -> {
                    final ConjuntoResidencial conjuntoResidencial = mapper.toDomain(UUID.randomUUID(), request,
                            tuple.getT1(), tuple.getT2());
                    return ensureNombreDisponible(conjuntoResidencial.getNombre(), null)
                            .then(conjuntoRepository.save(conjuntoResidencial));
                }))
                .map(mapper::toResponse);
    }

    private Mono<Void> ensureNombreDisponible(final String nombre, final UUID excluirId) {
        return conjuntoRepository.findByNombreIgnoreCase(nombre)
                .filter(existente -> excluirId == null || !existente.getId().equals(excluirId))
                .flatMap(existente -> Mono.<Void>error(
                        new ApplicationException("Ya existe un conjunto residencial con el nombre especificado")))
                .switchIfEmpty(Mono.empty());
    }
}
