package co.edu.uco.backendvictus.infrastructure.secondary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.edu.uco.backendvictus.domain.model.Ciudad;
import co.edu.uco.backendvictus.domain.model.Departamento;
import co.edu.uco.backendvictus.infrastructure.secondary.entity.CiudadEntity;

@Mapper(componentModel = "spring")
public interface CiudadEntityMapper {

    @Mapping(target = "departamentoId", source = "departamento.id")
    CiudadEntity toEntity(Ciudad ciudad);

    default Ciudad toDomain(final CiudadEntity entity, final Departamento departamento) {
        if (entity == null || departamento == null) {
            return null;
        }
        return Ciudad.create(entity.getId(), entity.getNombre(), departamento, entity.isActivo());
    }
}
