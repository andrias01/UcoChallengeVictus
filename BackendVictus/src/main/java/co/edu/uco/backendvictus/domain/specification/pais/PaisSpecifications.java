package co.edu.uco.backendvictus.domain.specification.pais;

import java.util.Locale;

import co.edu.uco.backendvictus.crosscutting.helpers.TextHelper;
import co.edu.uco.backendvictus.domain.model.Pais;
import co.edu.uco.backendvictus.domain.model.filter.PaisFilter;
import co.edu.uco.backendvictus.domain.specification.Specification;

/**
 * Specifications for {@link Pais} filtering.
 */
public final class PaisSpecifications {

    private PaisSpecifications() {
        // Utility class
    }

    public static Specification<Pais> matchFilter(final PaisFilter filter) {
        Specification<Pais> specification = candidate -> true;
        if (filter == null) {
            return specification;
        }

        if (!TextHelper.isNull(filter.nombre()) && !TextHelper.isEmptyAfterTrim(filter.nombre())) {
            final String nombreFilter = filter.nombre().toLowerCase(Locale.ROOT);
            specification = specification.and(pais -> pais.getNombre().toLowerCase(Locale.ROOT).contains(nombreFilter));
        }

        if (filter.activo() != null) {
            specification = specification.and(pais -> pais.isActivo() == filter.activo());
        }

        return specification;
    }
}
