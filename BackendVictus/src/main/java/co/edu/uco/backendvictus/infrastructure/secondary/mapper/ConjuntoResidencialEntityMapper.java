package co.edu.uco.backendvictus.infrastructure.secondary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.edu.uco.backendvictus.domain.model.Administrador;
import co.edu.uco.backendvictus.domain.model.Ciudad;
import co.edu.uco.backendvictus.domain.model.ConjuntoResidencial;
import co.edu.uco.backendvictus.infrastructure.secondary.entity.ConjuntoResidencialEntity;

@Mapper(componentModel = "spring")
public interface ConjuntoResidencialEntityMapper {

    @Mapping(target = "ciudadId", source = "ciudad.id")
    @Mapping(target = "administradorId", source = "administrador.id")
    ConjuntoResidencialEntity toEntity(ConjuntoResidencial conjuntoResidencial);

    default ConjuntoResidencial toDomain(final ConjuntoResidencialEntity entity, final Ciudad ciudad,
            final Administrador administrador) {
        if (entity == null || ciudad == null || administrador == null) {
            return null;
        }
        return ConjuntoResidencial.create(entity.getId(), entity.getNombre(), entity.getDireccion(), ciudad,
                administrador, entity.isActivo());
    }
}
