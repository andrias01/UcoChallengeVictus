package co.edu.uco.backendvictus.application.usecase.pais;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.backendvictus.application.dto.common.ChangeResponseDTO;
import co.edu.uco.backendvictus.application.dto.pais.PaisResponse;
import co.edu.uco.backendvictus.application.mapper.PaisApplicationMapper;
import co.edu.uco.backendvictus.crosscutting.exception.ApplicationException;
import co.edu.uco.backendvictus.domain.port.PaisRepository;
import reactor.core.publisher.Mono;

@Service
public class DeletePaisUseCase {

    private final PaisRepository repository;
    private final PaisApplicationMapper mapper;

    public DeletePaisUseCase(final PaisRepository repository, final PaisApplicationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Mono<ChangeResponseDTO<PaisResponse>> execute(final UUID id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ApplicationException("Pais no encontrado")))
                .flatMap(pais -> repository.deleteById(id)
                        .thenReturn(ChangeResponseDTO.of(mapper.toResponse(pais), null)));
    }
}
