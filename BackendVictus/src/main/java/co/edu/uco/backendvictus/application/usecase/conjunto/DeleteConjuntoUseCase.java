package co.edu.uco.backendvictus.application.usecase.conjunto;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.backendvictus.application.dto.common.ChangeResponseDTO;
import co.edu.uco.backendvictus.application.dto.conjunto.ConjuntoResponse;
import co.edu.uco.backendvictus.application.mapper.ConjuntoApplicationMapper;
import co.edu.uco.backendvictus.crosscutting.exception.ApplicationException;
import co.edu.uco.backendvictus.domain.port.ConjuntoResidencialRepository;
import reactor.core.publisher.Mono;

@Service
public class DeleteConjuntoUseCase {

    private final ConjuntoResidencialRepository conjuntoRepository;
    private final ConjuntoApplicationMapper mapper;

    public DeleteConjuntoUseCase(final ConjuntoResidencialRepository conjuntoRepository,
            final ConjuntoApplicationMapper mapper) {
        this.conjuntoRepository = conjuntoRepository;
        this.mapper = mapper;
    }

    public Mono<ChangeResponseDTO<ConjuntoResponse>> execute(final UUID id) {
        return conjuntoRepository.findById(id)
                .switchIfEmpty(Mono.error(new ApplicationException("Conjunto residencial no encontrado")))
                .flatMap(conjunto -> conjuntoRepository.deleteById(id)
                        .thenReturn(ChangeResponseDTO.of(mapper.toResponse(conjunto), null)));
    }
}
