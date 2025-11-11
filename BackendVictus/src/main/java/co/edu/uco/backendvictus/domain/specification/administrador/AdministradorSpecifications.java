package co.edu.uco.backendvictus.domain.specification.administrador;

import java.util.Locale;

import co.edu.uco.backendvictus.crosscutting.helpers.TextHelper;
import co.edu.uco.backendvictus.domain.model.Administrador;
import co.edu.uco.backendvictus.domain.model.filter.AdministradorFilter;
import co.edu.uco.backendvictus.domain.specification.Specification;

/**
 * Specifications for {@link Administrador} filtering.
 */
public final class AdministradorSpecifications {

    private AdministradorSpecifications() {
        // Utility class
    }

    public static Specification<Administrador> matchFilter(final AdministradorFilter filter) {
        Specification<Administrador> specification = candidate -> true;
        if (filter == null) {
            return specification;
        }

        if (!TextHelper.isNull(filter.nombre()) && !TextHelper.isEmptyAfterTrim(filter.nombre())) {
            final String nombreFilter = filter.nombre().toLowerCase(Locale.ROOT);
            specification = specification.and(
                    administrador -> administrador.getNombreCompleto().toLowerCase(Locale.ROOT).contains(nombreFilter));
        }

        if (!TextHelper.isNull(filter.email()) && !TextHelper.isEmptyAfterTrim(filter.email())) {
            final String emailFilter = filter.email().toLowerCase(Locale.ROOT);
            specification = specification
                    .and(administrador -> administrador.getEmail().toLowerCase(Locale.ROOT).contains(emailFilter));
        }

        if (filter.activo() != null) {
            specification = specification.and(administrador -> administrador.isActivo() == filter.activo());
        }

        return specification;
    }
}
