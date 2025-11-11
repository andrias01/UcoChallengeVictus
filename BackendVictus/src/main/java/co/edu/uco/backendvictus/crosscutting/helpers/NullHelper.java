package co.edu.uco.backendvictus.crosscutting.helpers;

/**
 * Null handling helper dedicated to expressively deal with nullable values across layers.
 */
public final class NullHelper {

    private NullHelper() {
        // Utility class
    }

    public static boolean isNull(final Object value) {
        return value == null;
    }

    public static <T> T getOrDefault(final T value, final T defaultValue) {
        return isNull(value) ? defaultValue : value;
    }
}
