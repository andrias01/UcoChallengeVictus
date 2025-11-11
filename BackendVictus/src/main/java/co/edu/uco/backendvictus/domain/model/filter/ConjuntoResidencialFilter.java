package co.edu.uco.backendvictus.domain.model.filter;

import java.util.UUID;

import co.edu.uco.backendvictus.crosscutting.helpers.NullHelper;
import co.edu.uco.backendvictus.crosscutting.helpers.StringSanitizer;

/**
 * Filter object for residential complex queries.
 */
public record ConjuntoResidencialFilter(String nombre, UUID ciudadId, UUID administradorId, Boolean activo) {

    public ConjuntoResidencialFilter {
        nombre = StringSanitizer.sanitize(nombre);
        ciudadId = NullHelper.getOrDefault(ciudadId, null);
        administradorId = NullHelper.getOrDefault(administradorId, null);
        activo = NullHelper.getOrDefault(activo, null);
    }

    public static ConjuntoResidencialFilter empty() {
        return new ConjuntoResidencialFilter(null, null, null, null);
    }
}
