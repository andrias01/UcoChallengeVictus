package co.edu.uco.backendvictus.application.usecase.pais;

import org.springframework.stereotype.Service;

import co.edu.uco.backendvictus.application.dto.pais.PaisResponse;
import co.edu.uco.backendvictus.application.mapper.PaisApplicationMapper;
import co.edu.uco.backendvictus.domain.model.filter.PaisFilter;
import co.edu.uco.backendvictus.domain.port.PaisRepository;
import reactor.core.publisher.Flux;

@Service
public class ListPaisUseCase {

    private final PaisRepository repository;
    private final PaisApplicationMapper mapper;

    public ListPaisUseCase(final PaisRepository repository, final PaisApplicationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Flux<PaisResponse> execute() {
        return execute(PaisFilter.empty());
    }

    public Flux<PaisResponse> execute(final PaisFilter filter) {
        return repository.findAll(filter).map(mapper::toResponse);
    }
}
