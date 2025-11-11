package co.edu.uco.backendvictus.application.usecase.ciudad;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.backendvictus.application.dto.ciudad.CiudadResponse;
import co.edu.uco.backendvictus.application.mapper.CiudadApplicationMapper;
import co.edu.uco.backendvictus.application.usecase.UseCase;
import co.edu.uco.backendvictus.crosscutting.exception.ApplicationException;
import co.edu.uco.backendvictus.domain.port.CiudadRepository;
import reactor.core.publisher.Mono;

@Service
public class FindCiudadByIdUseCase implements UseCase<UUID, CiudadResponse> {

    private final CiudadRepository ciudadRepository;
    private final CiudadApplicationMapper mapper;

    public FindCiudadByIdUseCase(final CiudadRepository ciudadRepository, final CiudadApplicationMapper mapper) {
        this.ciudadRepository = ciudadRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<CiudadResponse> execute(final UUID id) {
        return ciudadRepository.findById(id)
                .switchIfEmpty(Mono.error(new ApplicationException("Ciudad no encontrada")))
                .map(mapper::toResponse);
    }
}
