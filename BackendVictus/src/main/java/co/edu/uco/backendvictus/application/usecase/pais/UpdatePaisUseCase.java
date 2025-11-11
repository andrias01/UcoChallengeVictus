package co.edu.uco.backendvictus.application.usecase.pais;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.backendvictus.application.dto.common.ChangeResponseDTO;
import co.edu.uco.backendvictus.application.dto.pais.PaisResponse;
import co.edu.uco.backendvictus.application.dto.pais.PaisUpdateCommand;
import co.edu.uco.backendvictus.application.mapper.PaisApplicationMapper;
import co.edu.uco.backendvictus.application.usecase.UseCase;
import co.edu.uco.backendvictus.crosscutting.exception.ApplicationException;
import co.edu.uco.backendvictus.domain.model.Pais;
import co.edu.uco.backendvictus.domain.port.PaisRepository;
import reactor.core.publisher.Mono;

@Service
public class UpdatePaisUseCase implements UseCase<PaisUpdateCommand, ChangeResponseDTO<PaisResponse>> {

    private final PaisRepository repository;
    private final PaisApplicationMapper mapper;

    public UpdatePaisUseCase(final PaisRepository repository, final PaisApplicationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<ChangeResponseDTO<PaisResponse>> execute(final PaisUpdateCommand command) {
        return repository.findById(command.id())
                .switchIfEmpty(Mono.error(new ApplicationException("Pais no encontrado")))
                .flatMap(existing -> {
                    final Pais actualizado = mapper.toDomain(command);
                    return ensureNombreDisponible(actualizado.getNombre(), existing.getId())
                            .then(Mono.defer(() -> {
                                final PaisResponse before = mapper.toResponse(existing);
                                return repository.save(actualizado)
                                        .map(mapper::toResponse)
                                        .map(after -> ChangeResponseDTO.of(before, after));
                            }));
                });
    }

    private Mono<Void> ensureNombreDisponible(final String nombre, final UUID excluirId) {
        return repository.findByNombreIgnoreCase(nombre)
                .filter(existente -> excluirId == null || !existente.getId().equals(excluirId))
                .flatMap(existente -> Mono.<Void>error(
                        new ApplicationException("Ya existe un pais con el nombre especificado")))
                .switchIfEmpty(Mono.empty());
    }
}
