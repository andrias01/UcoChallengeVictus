package co.edu.uco.backendvictus.domain.specification.ciudad;

import java.util.Locale;

import co.edu.uco.backendvictus.crosscutting.helpers.TextHelper;
import co.edu.uco.backendvictus.crosscutting.helpers.UUIDHelper;
import co.edu.uco.backendvictus.domain.model.Ciudad;
import co.edu.uco.backendvictus.domain.model.filter.CiudadFilter;
import co.edu.uco.backendvictus.domain.specification.Specification;

/**
 * Specifications for {@link Ciudad} filtering.
 */
public final class CiudadSpecifications {

    private CiudadSpecifications() {
        // Utility class
    }

    public static Specification<Ciudad> matchFilter(final CiudadFilter filter) {
        Specification<Ciudad> specification = candidate -> true;
        if (filter == null) {
            return specification;
        }

        if (!TextHelper.isNull(filter.nombre()) && !TextHelper.isEmptyAfterTrim(filter.nombre())) {
            final String nombreFilter = filter.nombre().toLowerCase(Locale.ROOT);
            specification = specification.and(ciudad -> ciudad.getNombre().toLowerCase(Locale.ROOT).contains(nombreFilter));
        }

        if (filter.departamentoId() != null && !UUIDHelper.isDefault(filter.departamentoId())) {
            specification = specification
                    .and(ciudad -> UUIDHelper.isEqual(ciudad.getDepartamento().getId(), filter.departamentoId()));
        }

        if (filter.activo() != null) {
            specification = specification.and(ciudad -> ciudad.isActivo() == filter.activo());
        }

        return specification;
    }
}
