package co.edu.uco.backendvictus.application.mapper;

import java.util.List;
import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.edu.uco.backendvictus.application.dto.conjunto.ConjuntoCreateRequest;
import co.edu.uco.backendvictus.application.dto.conjunto.ConjuntoResponse;
import co.edu.uco.backendvictus.application.dto.conjunto.ConjuntoUpdateCommand;
import co.edu.uco.backendvictus.domain.model.Administrador;
import co.edu.uco.backendvictus.domain.model.Ciudad;
import co.edu.uco.backendvictus.domain.model.ConjuntoResidencial;

@Mapper(componentModel = "spring")
public abstract class ConjuntoApplicationMapper {

    public ConjuntoResidencial toDomain(final UUID id, final ConjuntoCreateRequest request, final Ciudad ciudad,
            final Administrador administrador) {
        return ConjuntoResidencial.create(id, request.nombre(), request.direccion(), ciudad, administrador,
                request.activo());
    }

    public ConjuntoResidencial toDomain(final ConjuntoUpdateCommand command, final Ciudad ciudad,
            final Administrador administrador) {
        return ConjuntoResidencial.create(command.id(), command.nombre(), command.direccion(), ciudad, administrador,
                command.activo());
    }

    @Mapping(target = "ciudadId", source = "ciudad.id")
    @Mapping(target = "administradorId", source = "administrador.id")
    public abstract ConjuntoResponse toResponse(ConjuntoResidencial conjuntoResidencial);

    public abstract List<ConjuntoResponse> toResponseList(List<ConjuntoResidencial> conjuntos);
}
