package co.edu.uco.backendvictus.crosscutting.helpers;

import java.util.UUID;

/**
 * Facade helper that exposes validation utilities with expressive method names for the application layer.
 */
public final class ValidationHelper {

    private ValidationHelper() {
        // Utility class
    }

    public static UUID requireValidId(final UUID id, final String fieldName) {
        return ValidationUtils.validateUUID(id, fieldName);
    }

    public static String requireText(final String value, final String fieldName, final int maxLength) {
        return ValidationUtils.validateRequiredText(value, fieldName, maxLength);
    }

    public static String optionalText(final String value, final String fieldName, final int maxLength) {
        return ValidationUtils.validateOptionalText(value, fieldName, maxLength);
    }

    public static String requireEmail(final String value) {
        return ValidationUtils.validateEmail(value);
    }
}
