package co.edu.uco.backendvictus.application.usecase.administrador;

import org.springframework.stereotype.Service;

import co.edu.uco.backendvictus.application.dto.administrador.AdministradorResponse;
import co.edu.uco.backendvictus.application.mapper.AdministradorApplicationMapper;
import co.edu.uco.backendvictus.domain.model.filter.AdministradorFilter;
import co.edu.uco.backendvictus.domain.port.AdministradorRepository;
import reactor.core.publisher.Flux;

@Service
public class ListAdministradorUseCase {

    private final AdministradorRepository administradorRepository;
    private final AdministradorApplicationMapper mapper;

    public ListAdministradorUseCase(final AdministradorRepository administradorRepository,
            final AdministradorApplicationMapper mapper) {
        this.administradorRepository = administradorRepository;
        this.mapper = mapper;
    }

    public Flux<AdministradorResponse> execute() {
        return execute(AdministradorFilter.empty());
    }

    public Flux<AdministradorResponse> execute(final AdministradorFilter filter) {
        return administradorRepository.findAll(filter).map(mapper::toResponse);
    }
}
