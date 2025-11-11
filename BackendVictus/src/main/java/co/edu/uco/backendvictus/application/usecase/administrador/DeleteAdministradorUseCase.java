package co.edu.uco.backendvictus.application.usecase.administrador;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.backendvictus.application.dto.administrador.AdministradorResponse;
import co.edu.uco.backendvictus.application.dto.common.ChangeResponseDTO;
import co.edu.uco.backendvictus.application.mapper.AdministradorApplicationMapper;
import co.edu.uco.backendvictus.crosscutting.exception.ApplicationException;
import co.edu.uco.backendvictus.domain.port.AdministradorRepository;
import reactor.core.publisher.Mono;

@Service
public class DeleteAdministradorUseCase {

    private final AdministradorRepository administradorRepository;
    private final AdministradorApplicationMapper mapper;

    public DeleteAdministradorUseCase(final AdministradorRepository administradorRepository,
            final AdministradorApplicationMapper mapper) {
        this.administradorRepository = administradorRepository;
        this.mapper = mapper;
    }

    public Mono<ChangeResponseDTO<AdministradorResponse>> execute(final UUID id) {
        return administradorRepository.findById(id)
                .switchIfEmpty(Mono.error(new ApplicationException("Administrador no encontrado")))
                .flatMap(admin -> administradorRepository.deleteById(id)
                        .thenReturn(ChangeResponseDTO.of(mapper.toResponse(admin), null)));
    }
}
