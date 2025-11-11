package co.edu.uco.backendvictus.application.usecase.departamento;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.backendvictus.application.dto.departamento.DepartamentoResponse;
import co.edu.uco.backendvictus.application.mapper.DepartamentoApplicationMapper;
import co.edu.uco.backendvictus.application.usecase.UseCase;
import co.edu.uco.backendvictus.crosscutting.exception.ApplicationException;
import co.edu.uco.backendvictus.domain.port.DepartamentoRepository;
import reactor.core.publisher.Mono;

@Service
public class FindDepartamentoByIdUseCase implements UseCase<UUID, DepartamentoResponse> {

    private final DepartamentoRepository departamentoRepository;
    private final DepartamentoApplicationMapper mapper;

    public FindDepartamentoByIdUseCase(final DepartamentoRepository departamentoRepository,
            final DepartamentoApplicationMapper mapper) {
        this.departamentoRepository = departamentoRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<DepartamentoResponse> execute(final UUID id) {
        return departamentoRepository.findById(id)
                .switchIfEmpty(Mono.error(new ApplicationException("Departamento no encontrado")))
                .map(mapper::toResponse);
    }
}
