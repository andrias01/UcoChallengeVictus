package co.edu.uco.backendvictus.application.mapper;

import java.util.List;
import java.util.UUID;

import org.mapstruct.Mapper;

import co.edu.uco.backendvictus.application.dto.pais.PaisCreateRequest;
import co.edu.uco.backendvictus.application.dto.pais.PaisResponse;
import co.edu.uco.backendvictus.application.dto.pais.PaisUpdateCommand;
import co.edu.uco.backendvictus.domain.model.Pais;

@Mapper(componentModel = "spring")
public abstract class PaisApplicationMapper {

    public Pais toDomain(final UUID id, final PaisCreateRequest request) {
        return Pais.create(id, request.nombre(), request.activo());
    }

    public Pais toDomain(final PaisUpdateCommand command) {
        return Pais.create(command.id(), command.nombre(), command.activo());
    }

    public abstract PaisResponse toResponse(Pais pais);

    public abstract List<PaisResponse> toResponseList(List<Pais> paises);
}
