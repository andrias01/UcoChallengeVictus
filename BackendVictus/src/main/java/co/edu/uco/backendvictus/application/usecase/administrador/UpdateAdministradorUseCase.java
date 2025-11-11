package co.edu.uco.backendvictus.application.usecase.administrador;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.backendvictus.application.dto.administrador.AdministradorResponse;
import co.edu.uco.backendvictus.application.dto.administrador.AdministradorUpdateCommand;
import co.edu.uco.backendvictus.application.dto.common.ChangeResponseDTO;
import co.edu.uco.backendvictus.application.mapper.AdministradorApplicationMapper;
import co.edu.uco.backendvictus.application.usecase.UseCase;
import co.edu.uco.backendvictus.crosscutting.exception.ApplicationException;
import co.edu.uco.backendvictus.crosscutting.helpers.TextHelper;
import co.edu.uco.backendvictus.domain.model.Administrador;
import co.edu.uco.backendvictus.domain.port.AdministradorRepository;
import reactor.core.publisher.Mono;

@Service
public class UpdateAdministradorUseCase
        implements UseCase<AdministradorUpdateCommand, ChangeResponseDTO<AdministradorResponse>> {

    private final AdministradorRepository administradorRepository;
    private final AdministradorApplicationMapper mapper;

    public UpdateAdministradorUseCase(final AdministradorRepository administradorRepository,
            final AdministradorApplicationMapper mapper) {
        this.administradorRepository = administradorRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<ChangeResponseDTO<AdministradorResponse>> execute(final AdministradorUpdateCommand command) {
        return administradorRepository.findById(command.id())
                .switchIfEmpty(Mono.error(new ApplicationException("Administrador no encontrado")))
                .flatMap(existente -> {
                    final Administrador actualizado = existente.update(command.primerNombre(),
                            command.segundoNombres(), command.primerApellido(), command.segundoApellido(),
                            command.email(), command.telefono(), command.activo());
                    return ensureEmailDisponible(actualizado.getEmail(), existente.getId())
                            .then(ensureTelefonoDisponible(actualizado.getTelefono(), existente.getId()))
                            .then(Mono.defer(() -> {
                                final AdministradorResponse before = mapper.toResponse(existente);
                                return administradorRepository.save(actualizado)
                                        .map(mapper::toResponse)
                                        .map(after -> ChangeResponseDTO.of(before, after));
                            }));
                });
    }

    private Mono<Void> ensureEmailDisponible(final String email, final UUID excluirId) {
        return administradorRepository.findByEmailIgnoreCase(email)
                .filter(existente -> excluirId == null || !existente.getId().equals(excluirId))
                .flatMap(existente -> Mono.<Void>error(
                        new ApplicationException("Ya existe un administrador con el correo especificado")))
                .switchIfEmpty(Mono.empty());
    }

    private Mono<Void> ensureTelefonoDisponible(final String telefono, final UUID excluirId) {
        if (TextHelper.isNull(telefono) || TextHelper.isEmptyAfterTrim(telefono)) {
            return Mono.empty();
        }
        return administradorRepository.findByTelefono(TextHelper.applyTrim(telefono))
                .filter(existente -> excluirId == null || !existente.getId().equals(excluirId))
                .flatMap(existente -> Mono.<Void>error(
                        new ApplicationException("Ya existe un administrador con el telefono especificado")))
                .switchIfEmpty(Mono.empty());
    }
}
