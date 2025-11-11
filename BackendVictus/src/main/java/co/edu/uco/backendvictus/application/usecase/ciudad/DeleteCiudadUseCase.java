package co.edu.uco.backendvictus.application.usecase.ciudad;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.backendvictus.application.dto.ciudad.CiudadResponse;
import co.edu.uco.backendvictus.application.dto.common.ChangeResponseDTO;
import co.edu.uco.backendvictus.application.mapper.CiudadApplicationMapper;
import co.edu.uco.backendvictus.crosscutting.exception.ApplicationException;
import co.edu.uco.backendvictus.domain.port.CiudadRepository;
import reactor.core.publisher.Mono;

@Service
public class DeleteCiudadUseCase {

    private final CiudadRepository ciudadRepository;
    private final CiudadApplicationMapper mapper;

    public DeleteCiudadUseCase(final CiudadRepository ciudadRepository, final CiudadApplicationMapper mapper) {
        this.ciudadRepository = ciudadRepository;
        this.mapper = mapper;
    }

    public Mono<ChangeResponseDTO<CiudadResponse>> execute(final UUID id) {
        return ciudadRepository.findById(id)
                .switchIfEmpty(Mono.error(new ApplicationException("Ciudad no encontrada")))
                .flatMap(ciudad -> ciudadRepository.deleteById(id)
                        .thenReturn(ChangeResponseDTO.of(mapper.toResponse(ciudad), null)));
    }
}
