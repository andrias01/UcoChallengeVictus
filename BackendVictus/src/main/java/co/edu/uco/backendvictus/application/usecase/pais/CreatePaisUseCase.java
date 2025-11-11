package co.edu.uco.backendvictus.application.usecase.pais;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.backendvictus.application.dto.pais.PaisCreateRequest;
import co.edu.uco.backendvictus.application.dto.pais.PaisResponse;
import co.edu.uco.backendvictus.application.mapper.PaisApplicationMapper;
import co.edu.uco.backendvictus.application.usecase.UseCase;
import co.edu.uco.backendvictus.crosscutting.exception.ApplicationException;
import co.edu.uco.backendvictus.domain.model.Pais;
import co.edu.uco.backendvictus.domain.port.PaisRepository;
import reactor.core.publisher.Mono;

@Service
public class CreatePaisUseCase implements UseCase<PaisCreateRequest, PaisResponse> {

    private final PaisRepository repository;
    private final PaisApplicationMapper mapper;

    public CreatePaisUseCase(final PaisRepository repository, final PaisApplicationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<PaisResponse> execute(final PaisCreateRequest request) {
        return Mono.defer(() -> {
            final Pais pais = mapper.toDomain(UUID.randomUUID(), request);
            return ensureNombreDisponible(pais.getNombre(), null)
                    .then(repository.save(pais).map(mapper::toResponse));
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
