package co.edu.uco.backendvictus.infrastructure.secondary.mapper;

import org.mapstruct.Mapper;

import co.edu.uco.backendvictus.domain.model.Administrador;
import co.edu.uco.backendvictus.infrastructure.secondary.entity.AdministradorEntity;

@Mapper(componentModel = "spring")
public interface AdministradorEntityMapper {

    AdministradorEntity toEntity(Administrador administrador);

    default Administrador toDomain(final AdministradorEntity entity) {
        if (entity == null) {
            return null;
        }
        return Administrador.create(entity.getId(), entity.getPrimerNombre(), entity.getSegundoNombres(),
                entity.getPrimerApellido(), entity.getSegundoApellido(), entity.getEmail(), entity.getTelefono(),
                entity.isActivo());
    }
}
