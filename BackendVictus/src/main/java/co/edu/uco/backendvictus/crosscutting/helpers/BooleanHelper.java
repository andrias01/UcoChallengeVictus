package co.edu.uco.backendvictus.crosscutting.helpers;

/**
 * Helper utility for boolean operations and default handling.
 */
public final class BooleanHelper {

    public static final boolean DEFAULT = false;

    private BooleanHelper() {
        // Utility class
    }

    public static boolean getDefault(final Boolean value) {
        return NullHelper.getOrDefault(value, DEFAULT);
    }
}
