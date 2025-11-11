package co.edu.uco.backendvictus.infrastructure.secondary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.edu.uco.backendvictus.crosscutting.helpers.ObjectHelper;
import co.edu.uco.backendvictus.crosscutting.helpers.TextHelper;
import co.edu.uco.backendvictus.domain.model.Departamento;
import co.edu.uco.backendvictus.domain.model.Pais;
import co.edu.uco.backendvictus.infrastructure.secondary.entity.DepartamentoEntity;

@Mapper(componentModel = "spring", imports = TextHelper.class)
public interface DepartamentoEntityMapper {

    @Mapping(target = "paisId", source = "pais.id")
    @Mapping(target = "nombre", expression = "java(TextHelper.applyTrim(departamento.getNombre()))")
    DepartamentoEntity toEntity(Departamento departamento);

    default Departamento toDomain(final DepartamentoEntity entity, final Pais pais) {
        if (ObjectHelper.isNull(entity) || ObjectHelper.isNull(pais)) {
            return null;
        }
        return Departamento.create(entity.getId(), entity.getNombre(), pais, entity.isActivo());
    }
}
