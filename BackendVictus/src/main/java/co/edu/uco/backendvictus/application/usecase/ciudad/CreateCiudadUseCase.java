package co.edu.uco.backendvictus.application.usecase.ciudad;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.backendvictus.application.dto.ciudad.CiudadCreateRequest;
import co.edu.uco.backendvictus.application.dto.ciudad.CiudadResponse;
import co.edu.uco.backendvictus.application.mapper.CiudadApplicationMapper;
import co.edu.uco.backendvictus.application.usecase.UseCase;
import co.edu.uco.backendvictus.crosscutting.exception.ApplicationException;
import co.edu.uco.backendvictus.domain.model.Ciudad;
import co.edu.uco.backendvictus.domain.model.Departamento;
import co.edu.uco.backendvictus.domain.port.CiudadRepository;
import co.edu.uco.backendvictus.domain.port.DepartamentoRepository;
import reactor.core.publisher.Mono;

@Service
public class CreateCiudadUseCase implements UseCase<CiudadCreateRequest, CiudadResponse> {

    private final CiudadRepository ciudadRepository;
    private final DepartamentoRepository departamentoRepository;
    private final CiudadApplicationMapper mapper;

    public CreateCiudadUseCase(final CiudadRepository ciudadRepository,
            final DepartamentoRepository departamentoRepository, final CiudadApplicationMapper mapper) {
        this.ciudadRepository = ciudadRepository;
        this.departamentoRepository = departamentoRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<CiudadResponse> execute(final CiudadCreateRequest request) {
        return departamentoRepository.findById(request.departamentoId())
                .switchIfEmpty(Mono.error(new ApplicationException("Departamento no encontrado")))
                .flatMap(departamento -> Mono.defer(() -> {
                    final Ciudad ciudad = mapper.toDomain(UUID.randomUUID(), request, departamento);
                    return ensureNombreDisponible(ciudad.getNombre(), null)
                            .then(ciudadRepository.save(ciudad));
                }))
                .map(mapper::toResponse);
    }

    private Mono<Void> ensureNombreDisponible(final String nombre, final UUID excluirId) {
        return ciudadRepository.findByNombreIgnoreCase(nombre)
                .filter(existente -> excluirId == null || !existente.getId().equals(excluirId))
                .flatMap(existente -> Mono.<Void>error(
                        new ApplicationException("Ya existe una ciudad con el nombre especificado")))
                .switchIfEmpty(Mono.empty());
    }
}
