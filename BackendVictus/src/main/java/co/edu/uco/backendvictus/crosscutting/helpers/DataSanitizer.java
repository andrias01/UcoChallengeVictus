package co.edu.uco.backendvictus.crosscutting.helpers;

/**
 * @deprecated use {@link StringSanitizer} instead. This class remains for backward compatibility.
 */
@Deprecated(forRemoval = true)
public final class DataSanitizer {

    private DataSanitizer() {
        // Utility class
    }

    public static String sanitizeText(final String rawValue) {
        return StringSanitizer.sanitize(rawValue);
    }
}
