package co.edu.uco.backendvictus.application.usecase.conjunto;

import org.springframework.stereotype.Service;

import co.edu.uco.backendvictus.application.dto.conjunto.ConjuntoResponse;
import co.edu.uco.backendvictus.application.mapper.ConjuntoApplicationMapper;
import co.edu.uco.backendvictus.domain.model.filter.ConjuntoResidencialFilter;
import co.edu.uco.backendvictus.domain.port.ConjuntoResidencialRepository;
import reactor.core.publisher.Flux;

@Service
public class ListConjuntoUseCase {

    private final ConjuntoResidencialRepository conjuntoRepository;
    private final ConjuntoApplicationMapper mapper;

    public ListConjuntoUseCase(final ConjuntoResidencialRepository conjuntoRepository,
            final ConjuntoApplicationMapper mapper) {
        this.conjuntoRepository = conjuntoRepository;
        this.mapper = mapper;
    }

    public Flux<ConjuntoResponse> execute() {
        return execute(ConjuntoResidencialFilter.empty());
    }

    public Flux<ConjuntoResponse> execute(final ConjuntoResidencialFilter filter) {
        return conjuntoRepository.findAll(filter).map(mapper::toResponse);
    }
}
