package co.edu.uco.backendvictus.application.usecase.departamento;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.backendvictus.application.dto.common.ChangeResponseDTO;
import co.edu.uco.backendvictus.application.dto.departamento.DepartamentoResponse;
import co.edu.uco.backendvictus.application.dto.departamento.DepartamentoUpdateCommand;
import co.edu.uco.backendvictus.application.mapper.DepartamentoApplicationMapper;
import co.edu.uco.backendvictus.application.usecase.UseCase;
import co.edu.uco.backendvictus.crosscutting.exception.ApplicationException;
import co.edu.uco.backendvictus.domain.model.Departamento;
import co.edu.uco.backendvictus.domain.model.Pais;
import co.edu.uco.backendvictus.domain.port.DepartamentoRepository;
import co.edu.uco.backendvictus.domain.port.PaisRepository;
import reactor.core.publisher.Mono;

@Service
public class UpdateDepartamentoUseCase
        implements UseCase<DepartamentoUpdateCommand, ChangeResponseDTO<DepartamentoResponse>> {

    private final DepartamentoRepository departamentoRepository;
    private final PaisRepository paisRepository;
    private final DepartamentoApplicationMapper mapper;

    public UpdateDepartamentoUseCase(final DepartamentoRepository departamentoRepository,
            final PaisRepository paisRepository, final DepartamentoApplicationMapper mapper) {
        this.departamentoRepository = departamentoRepository;
        this.paisRepository = paisRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<ChangeResponseDTO<DepartamentoResponse>> execute(final DepartamentoUpdateCommand command) {
        return departamentoRepository.findById(command.id())
                .switchIfEmpty(Mono.error(new ApplicationException("Departamento no encontrado")))
                .flatMap(existente -> paisRepository.findById(command.paisId())
                        .switchIfEmpty(Mono.error(new ApplicationException("Pais no encontrado")))
                        .flatMap(pais -> {
                            final Departamento actualizado = existente.update(command.nombre(), pais,
                                    command.activo());
                            return ensureNombreDisponible(actualizado.getNombre(), existente.getId())
                                    .then(Mono.defer(() -> {
                                        final DepartamentoResponse before = mapper.toResponse(existente);
                                        return departamentoRepository.save(actualizado)
                                                .map(mapper::toResponse)
                                                .map(after -> ChangeResponseDTO.of(before, after));
                                    }));
                        }));
    }

    private Mono<Void> ensureNombreDisponible(final String nombre, final UUID excluirId) {
        return departamentoRepository.findByNombreIgnoreCase(nombre)
                .filter(existente -> excluirId == null || !existente.getId().equals(excluirId))
                .flatMap(existente -> Mono.<Void>error(
                        new ApplicationException("Ya existe un departamento con el nombre especificado")))
                .switchIfEmpty(Mono.empty());
    }
}
