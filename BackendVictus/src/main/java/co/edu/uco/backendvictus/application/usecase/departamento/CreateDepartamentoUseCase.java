package co.edu.uco.backendvictus.application.usecase.departamento;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.backendvictus.application.dto.departamento.DepartamentoCreateRequest;
import co.edu.uco.backendvictus.application.dto.departamento.DepartamentoResponse;
import co.edu.uco.backendvictus.application.mapper.DepartamentoApplicationMapper;
import co.edu.uco.backendvictus.application.usecase.UseCase;
import co.edu.uco.backendvictus.crosscutting.exception.ApplicationException;
import co.edu.uco.backendvictus.domain.model.Departamento;
import co.edu.uco.backendvictus.domain.model.Pais;
import co.edu.uco.backendvictus.domain.port.DepartamentoRepository;
import co.edu.uco.backendvictus.domain.port.PaisRepository;
import reactor.core.publisher.Mono;

@Service
public class CreateDepartamentoUseCase implements UseCase<DepartamentoCreateRequest, DepartamentoResponse> {

    private final DepartamentoRepository departamentoRepository;
    private final PaisRepository paisRepository;
    private final DepartamentoApplicationMapper mapper;

    public CreateDepartamentoUseCase(final DepartamentoRepository departamentoRepository,
            final PaisRepository paisRepository, final DepartamentoApplicationMapper mapper) {
        this.departamentoRepository = departamentoRepository;
        this.paisRepository = paisRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<DepartamentoResponse> execute(final DepartamentoCreateRequest request) {
        return paisRepository.findById(request.paisId())
                .switchIfEmpty(Mono.error(new ApplicationException("Pais no encontrado")))
                .flatMap(pais -> Mono.defer(() -> {
                    final Departamento departamento = mapper.toDomain(UUID.randomUUID(), request, pais);
                    return ensureNombreDisponible(departamento.getNombre(), null)
                            .then(departamentoRepository.save(departamento));
                }))
                .map(mapper::toResponse);
    }

    private Mono<Void> ensureNombreDisponible(final String nombre, final UUID excluirId) {
        return departamentoRepository.findByNombreIgnoreCase(nombre)
                .filter(existente -> excluirId == null || !existente.getId().equals(excluirId))
                .flatMap(existente -> Mono.<Void>error(
                        new ApplicationException("Ya existe un departamento con el nombre especificado")))
                .switchIfEmpty(Mono.empty());
    }
}
