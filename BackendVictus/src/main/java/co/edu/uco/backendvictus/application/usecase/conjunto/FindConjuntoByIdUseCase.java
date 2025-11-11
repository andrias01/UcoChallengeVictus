package co.edu.uco.backendvictus.application.usecase.conjunto;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.backendvictus.application.dto.conjunto.ConjuntoResponse;
import co.edu.uco.backendvictus.application.mapper.ConjuntoApplicationMapper;
import co.edu.uco.backendvictus.application.usecase.UseCase;
import co.edu.uco.backendvictus.crosscutting.exception.ApplicationException;
import co.edu.uco.backendvictus.domain.port.ConjuntoResidencialRepository;
import reactor.core.publisher.Mono;

@Service
public class FindConjuntoByIdUseCase implements UseCase<UUID, ConjuntoResponse> {

    private final ConjuntoResidencialRepository conjuntoRepository;
    private final ConjuntoApplicationMapper mapper;

    public FindConjuntoByIdUseCase(final ConjuntoResidencialRepository conjuntoRepository,
            final ConjuntoApplicationMapper mapper) {
        this.conjuntoRepository = conjuntoRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<ConjuntoResponse> execute(final UUID id) {
        return conjuntoRepository.findById(id)
                .switchIfEmpty(Mono.error(new ApplicationException("Conjunto residencial no encontrado")))
                .map(mapper::toResponse);
    }
}
