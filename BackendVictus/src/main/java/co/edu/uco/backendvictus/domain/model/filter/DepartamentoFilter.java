package co.edu.uco.backendvictus.domain.model.filter;

import java.util.UUID;

import co.edu.uco.backendvictus.crosscutting.helpers.NullHelper;
import co.edu.uco.backendvictus.crosscutting.helpers.StringSanitizer;

/**
 * Filter object for department queries.
 */
public record DepartamentoFilter(String nombre, UUID paisId, Boolean activo) {

    public DepartamentoFilter {
        nombre = StringSanitizer.sanitize(nombre);
        paisId = NullHelper.getOrDefault(paisId, null);
        activo = NullHelper.getOrDefault(activo, null);
    }

    public static DepartamentoFilter empty() {
        return new DepartamentoFilter(null, null, null);
    }
}
