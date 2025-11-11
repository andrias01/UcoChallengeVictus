package co.edu.uco.backendvictus.domain.specification.departamento;

import java.util.Locale;

import co.edu.uco.backendvictus.crosscutting.helpers.TextHelper;
import co.edu.uco.backendvictus.crosscutting.helpers.UUIDHelper;
import co.edu.uco.backendvictus.domain.model.Departamento;
import co.edu.uco.backendvictus.domain.model.filter.DepartamentoFilter;
import co.edu.uco.backendvictus.domain.specification.Specification;

/**
 * Specifications for {@link Departamento} filtering.
 */
public final class DepartamentoSpecifications {

    private DepartamentoSpecifications() {
        // Utility class
    }

    public static Specification<Departamento> matchFilter(final DepartamentoFilter filter) {
        Specification<Departamento> specification = candidate -> true;
        if (filter == null) {
            return specification;
        }

        if (!TextHelper.isNull(filter.nombre()) && !TextHelper.isEmptyAfterTrim(filter.nombre())) {
            final String nombreFilter = filter.nombre().toLowerCase(Locale.ROOT);
            specification = specification
                    .and(departamento -> departamento.getNombre().toLowerCase(Locale.ROOT).contains(nombreFilter));
        }

        if (filter.paisId() != null && !UUIDHelper.isDefault(filter.paisId())) {
            specification = specification.and(departamento -> UUIDHelper.isEqual(departamento.getPais().getId(),
                    filter.paisId()));
        }

        if (filter.activo() != null) {
            specification = specification.and(departamento -> departamento.isActivo() == filter.activo());
        }

        return specification;
    }
}
