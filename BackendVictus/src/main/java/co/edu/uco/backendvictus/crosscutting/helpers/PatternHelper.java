package co.edu.uco.backendvictus.crosscutting.helpers;

import java.util.regex.Pattern;

/**
 * Central place to keep reusable regular expressions.
 */
public final class PatternHelper {

    public static final Pattern EMAIL = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);

    private PatternHelper() {
        // Utility class
    }
}
