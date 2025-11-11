package co.edu.uco.backendvictus.domain.model.filter;

import co.edu.uco.backendvictus.crosscutting.helpers.NullHelper;
import co.edu.uco.backendvictus.crosscutting.helpers.StringSanitizer;

/**
 * Filter object for country queries.
 */
public record PaisFilter(String nombre, Boolean activo) {

    public PaisFilter {
        nombre = StringSanitizer.sanitize(nombre);
        activo = NullHelper.getOrDefault(activo, null);
    }

    public static PaisFilter empty() {
        return new PaisFilter(null, null);
    }
}
