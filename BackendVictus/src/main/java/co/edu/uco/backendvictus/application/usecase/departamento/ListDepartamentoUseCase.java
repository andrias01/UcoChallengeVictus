package co.edu.uco.backendvictus.application.usecase.departamento;

import org.springframework.stereotype.Service;

import co.edu.uco.backendvictus.application.dto.departamento.DepartamentoResponse;
import co.edu.uco.backendvictus.application.mapper.DepartamentoApplicationMapper;
import co.edu.uco.backendvictus.domain.model.filter.DepartamentoFilter;
import co.edu.uco.backendvictus.domain.port.DepartamentoRepository;
import reactor.core.publisher.Flux;

@Service
public class ListDepartamentoUseCase {

    private final DepartamentoRepository departamentoRepository;
    private final DepartamentoApplicationMapper mapper;

    public ListDepartamentoUseCase(final DepartamentoRepository departamentoRepository,
            final DepartamentoApplicationMapper mapper) {
        this.departamentoRepository = departamentoRepository;
        this.mapper = mapper;
    }

    public Flux<DepartamentoResponse> execute() {
        return execute(DepartamentoFilter.empty());
    }

    public Flux<DepartamentoResponse> execute(final DepartamentoFilter filter) {
        return departamentoRepository.findAll(filter).map(mapper::toResponse);
    }
}
