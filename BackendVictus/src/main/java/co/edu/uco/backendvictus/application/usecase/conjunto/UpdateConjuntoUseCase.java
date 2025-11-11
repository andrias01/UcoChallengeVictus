package co.edu.uco.backendvictus.application.usecase.conjunto;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.backendvictus.application.dto.common.ChangeResponseDTO;
import co.edu.uco.backendvictus.application.dto.conjunto.ConjuntoResponse;
import co.edu.uco.backendvictus.application.dto.conjunto.ConjuntoUpdateCommand;
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
public class UpdateConjuntoUseCase implements UseCase<ConjuntoUpdateCommand, ChangeResponseDTO<ConjuntoResponse>> {

    private final ConjuntoResidencialRepository conjuntoRepository;
    private final CiudadRepository ciudadRepository;
    private final AdministradorRepository administradorRepository;
    private final ConjuntoApplicationMapper mapper;

    public UpdateConjuntoUseCase(final ConjuntoResidencialRepository conjuntoRepository,
            final CiudadRepository ciudadRepository, final AdministradorRepository administradorRepository,
            final ConjuntoApplicationMapper mapper) {
        this.conjuntoRepository = conjuntoRepository;
        this.ciudadRepository = ciudadRepository;
        this.administradorRepository = administradorRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<ChangeResponseDTO<ConjuntoResponse>> execute(final ConjuntoUpdateCommand command) {
        final Mono<Ciudad> ciudadMono = ciudadRepository.findById(command.ciudadId())
                .switchIfEmpty(Mono.error(new ApplicationException("Ciudad no encontrada")));
        final Mono<Administrador> administradorMono = administradorRepository.findById(command.administradorId())
                .switchIfEmpty(Mono.error(new ApplicationException("Administrador no encontrado")));

        return conjuntoRepository.findById(command.id())
                .switchIfEmpty(Mono.error(new ApplicationException("Conjunto residencial no encontrado")))
                .flatMap(existente -> Mono.zip(ciudadMono, administradorMono)
                        .flatMap(tuple -> {
                            final ConjuntoResidencial actualizado = existente.update(command.nombre(),
                                    command.direccion(), tuple.getT1(), tuple.getT2(), command.activo());
                            return ensureNombreDisponible(actualizado.getNombre(), existente.getId())
                                    .then(Mono.defer(() -> {
                                        final ConjuntoResponse before = mapper.toResponse(existente);
                                        return conjuntoRepository.save(actualizado)
                                                .map(mapper::toResponse)
                                                .map(after -> ChangeResponseDTO.of(before, after));
                                    }));
                        }));
    }

    private Mono<Void> ensureNombreDisponible(final String nombre, final UUID excluirId) {
        return conjuntoRepository.findByNombreIgnoreCase(nombre)
                .filter(existente -> excluirId == null || !existente.getId().equals(excluirId))
                .flatMap(existente -> Mono.<Void>error(
                        new ApplicationException("Ya existe un conjunto residencial con el nombre especificado")))
                .switchIfEmpty(Mono.empty());
    }
}
