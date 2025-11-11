package co.edu.uco.backendvictus.domain.specification.conjunto;

import java.util.Locale;

import co.edu.uco.backendvictus.crosscutting.helpers.TextHelper;
import co.edu.uco.backendvictus.crosscutting.helpers.UUIDHelper;
import co.edu.uco.backendvictus.domain.model.ConjuntoResidencial;
import co.edu.uco.backendvictus.domain.model.filter.ConjuntoResidencialFilter;
import co.edu.uco.backendvictus.domain.specification.Specification;

/**
 * Specifications for {@link ConjuntoResidencial} filtering.
 */
public final class ConjuntoResidencialSpecifications {

    private ConjuntoResidencialSpecifications() {
        // Utility class
    }

    public static Specification<ConjuntoResidencial> matchFilter(final ConjuntoResidencialFilter filter) {
        Specification<ConjuntoResidencial> specification = candidate -> true;
        if (filter == null) {
            return specification;
        }

        if (!TextHelper.isNull(filter.nombre()) && !TextHelper.isEmptyAfterTrim(filter.nombre())) {
            final String nombreFilter = filter.nombre().toLowerCase(Locale.ROOT);
            specification = specification
                    .and(conjunto -> conjunto.getNombre().toLowerCase(Locale.ROOT).contains(nombreFilter));
        }

        if (filter.ciudadId() != null && !UUIDHelper.isDefault(filter.ciudadId())) {
            specification = specification
                    .and(conjunto -> UUIDHelper.isEqual(conjunto.getCiudad().getId(), filter.ciudadId()));
        }

        if (filter.administradorId() != null && !UUIDHelper.isDefault(filter.administradorId())) {
            specification = specification.and(
                    conjunto -> UUIDHelper.isEqual(conjunto.getAdministrador().getId(), filter.administradorId()));
        }

        if (filter.activo() != null) {
            specification = specification.and(conjunto -> conjunto.isActivo() == filter.activo());
        }

        return specification;
    }
}
