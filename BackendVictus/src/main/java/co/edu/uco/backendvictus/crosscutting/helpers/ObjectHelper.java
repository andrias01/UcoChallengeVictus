package co.edu.uco.backendvictus.crosscutting.helpers;

/**
 * Utility methods to deal with null-safe object handling across the application.
 */
public final class ObjectHelper {

    private ObjectHelper() {
        // Utility class
    }

    public static <T> boolean isNull(final T object) {
        return object == null;
    }

    public static <T> T getDefault(final T object, final T defaultValue) {
        return isNull(object) ? defaultValue : object;
    }
}
