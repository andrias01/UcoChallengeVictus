package co.edu.uco.backendvictus.application.usecase.administrador;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.backendvictus.application.dto.administrador.AdministradorResponse;
import co.edu.uco.backendvictus.application.mapper.AdministradorApplicationMapper;
import co.edu.uco.backendvictus.application.usecase.UseCase;
import co.edu.uco.backendvictus.crosscutting.exception.ApplicationException;
import co.edu.uco.backendvictus.domain.port.AdministradorRepository;
import reactor.core.publisher.Mono;

@Service
public class FindAdministradorByIdUseCase implements UseCase<UUID, AdministradorResponse> {

    private final AdministradorRepository administradorRepository;
    private final AdministradorApplicationMapper mapper;

    public FindAdministradorByIdUseCase(final AdministradorRepository administradorRepository,
            final AdministradorApplicationMapper mapper) {
        this.administradorRepository = administradorRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<AdministradorResponse> execute(final UUID id) {
        return administradorRepository.findById(id)
                .switchIfEmpty(Mono.error(new ApplicationException("Administrador no encontrado")))
                .map(mapper::toResponse);
    }
}
