package co.edu.uco.backendvictus.infrastructure.secondary.mapper;

import org.mapstruct.Mapper;

import co.edu.uco.backendvictus.domain.model.Pais;
import co.edu.uco.backendvictus.infrastructure.secondary.entity.PaisEntity;

@Mapper(componentModel = "spring")
public interface PaisEntityMapper {

    PaisEntity toEntity(Pais pais);

    default Pais toDomain(final PaisEntity entity) {
        if (entity == null) {
            return null;
        }
        return Pais.create(entity.getId(), entity.getNombre(), entity.isActivo());
    }
}
