package co.edu.uco.backendvictus.domain.model.filter;

import java.util.UUID;

import co.edu.uco.backendvictus.crosscutting.helpers.NullHelper;
import co.edu.uco.backendvictus.crosscutting.helpers.StringSanitizer;

/**
 * Filter object for city queries.
 */
public record CiudadFilter(String nombre, UUID departamentoId, Boolean activo) {

    public CiudadFilter {
        nombre = StringSanitizer.sanitize(nombre);
        departamentoId = NullHelper.getOrDefault(departamentoId, null);
        activo = NullHelper.getOrDefault(activo, null);
    }

    public static CiudadFilter empty() {
        return new CiudadFilter(null, null, null);
    }
}
