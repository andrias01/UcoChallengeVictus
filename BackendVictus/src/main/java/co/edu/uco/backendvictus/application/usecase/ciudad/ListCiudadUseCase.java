package co.edu.uco.backendvictus.application.usecase.ciudad;

import org.springframework.stereotype.Service;

import co.edu.uco.backendvictus.application.dto.ciudad.CiudadResponse;
import co.edu.uco.backendvictus.application.mapper.CiudadApplicationMapper;
import co.edu.uco.backendvictus.domain.model.filter.CiudadFilter;
import co.edu.uco.backendvictus.domain.port.CiudadRepository;
import reactor.core.publisher.Flux;

@Service
public class ListCiudadUseCase {

    private final CiudadRepository ciudadRepository;
    private final CiudadApplicationMapper mapper;

    public ListCiudadUseCase(final CiudadRepository ciudadRepository, final CiudadApplicationMapper mapper) {
        this.ciudadRepository = ciudadRepository;
        this.mapper = mapper;
    }

    public Flux<CiudadResponse> execute() {
        return execute(CiudadFilter.empty());
    }

    public Flux<CiudadResponse> execute(final CiudadFilter filter) {
        return ciudadRepository.findAll(filter).map(mapper::toResponse);
    }
}
