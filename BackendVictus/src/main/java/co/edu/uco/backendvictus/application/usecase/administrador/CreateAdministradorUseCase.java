package co.edu.uco.backendvictus.application.usecase.administrador;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.backendvictus.application.dto.administrador.AdministradorCreateRequest;
import co.edu.uco.backendvictus.application.dto.administrador.AdministradorResponse;
import co.edu.uco.backendvictus.application.mapper.AdministradorApplicationMapper;
import co.edu.uco.backendvictus.application.usecase.UseCase;
import co.edu.uco.backendvictus.crosscutting.exception.ApplicationException;
import co.edu.uco.backendvictus.crosscutting.helpers.TextHelper;
import co.edu.uco.backendvictus.domain.model.Administrador;
import co.edu.uco.backendvictus.domain.port.AdministradorRepository;
import reactor.core.publisher.Mono;

@Service
public class CreateAdministradorUseCase implements UseCase<AdministradorCreateRequest, AdministradorResponse> {

    private final AdministradorRepository administradorRepository;
    private final AdministradorApplicationMapper mapper;

    public CreateAdministradorUseCase(final AdministradorRepository administradorRepository,
            final AdministradorApplicationMapper mapper) {
        this.administradorRepository = administradorRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<AdministradorResponse> execute(final AdministradorCreateRequest request) {
        return Mono.defer(() -> {
            final Administrador administrador = mapper.toDomain(UUID.randomUUID(), request);
            return ensureEmailDisponible(administrador.getEmail(), null)
                    .then(ensureTelefonoDisponible(administrador.getTelefono(), null))
                    .then(administradorRepository.save(administrador))
                    .map(mapper::toResponse);
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
