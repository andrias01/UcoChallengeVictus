package co.edu.uco.backendvictus.crosscutting.helpers;

import java.util.UUID;

import co.edu.uco.backendvictus.crosscutting.exception.DomainException;

/**
 * Provides cross-cutting validation logic shared across the different layers.
 */
public final class ValidationUtils {

    private ValidationUtils() {
        // Utility class
    }

    public static UUID validateUUID(final UUID id, final String fieldName) {
        if (ObjectHelper.isNull(id) || UUIDHelper.isDefault(id)) {
            throw new DomainException(fieldName + " no puede ser nulo", fieldName + " no puede ser nulo");
        }
        return id;
    }

    public static String validateRequiredText(final String value, final String fieldName, final int maxLength) {
        final String sanitized = TextHelper.sanitize(value);
        if (TextHelper.isNull(sanitized) || TextHelper.isEmptyAfterTrim(sanitized)) {
            throw new DomainException(fieldName + " es requerido", fieldName + " es requerido");
        }
        if (TextHelper.length(sanitized) > maxLength) {
            throw new DomainException(fieldName + " supera la longitud maxima de " + maxLength,
                    fieldName + " supera la longitud maxima de " + maxLength);
        }
        return sanitized;
    }

    public static String validateOptionalText(final String value, final String fieldName, final int maxLength) {
        final String sanitized = TextHelper.sanitize(value);
        if (!TextHelper.isNull(sanitized) && TextHelper.length(sanitized) > maxLength) {
            throw new DomainException(fieldName + " supera la longitud maxima de " + maxLength,
                    fieldName + " supera la longitud maxima de " + maxLength);
        }
        return sanitized;
    }

    public static String validateEmail(final String value) {
        final String sanitized = TextHelper.sanitize(value);
        if (TextHelper.isNull(sanitized) || TextHelper.isEmptyAfterTrim(sanitized)) {
            throw new DomainException("El correo electronico es requerido");
        }
        if (!PatternHelper.EMAIL.matcher(sanitized).matches()) {
            throw new DomainException("El correo electronico no es valido");
        }
        return sanitized;
    }
}
