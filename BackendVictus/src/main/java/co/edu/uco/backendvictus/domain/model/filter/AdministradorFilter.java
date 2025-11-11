package co.edu.uco.backendvictus.domain.model.filter;

import co.edu.uco.backendvictus.crosscutting.helpers.NullHelper;
import co.edu.uco.backendvictus.crosscutting.helpers.StringSanitizer;

/**
 * Filter object for administrator queries.
 */
public record AdministradorFilter(String nombre, String email, Boolean activo) {

    public AdministradorFilter {
        nombre = StringSanitizer.sanitize(nombre);
        email = StringSanitizer.sanitizeToLower(email);
        activo = NullHelper.getOrDefault(activo, null);
    }

    public static AdministradorFilter empty() {
        return new AdministradorFilter(null, null, null);
    }
}
