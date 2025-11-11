package co.edu.uco.backendvictus.crosscutting.helpers;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Centralized sanitization helper that neutralizes HTML, SQL metacharacters and excessive whitespace.
 */
public final class StringSanitizer {

    private static final Pattern SCRIPT_PATTERN = Pattern.compile("(?i)<script.*?>.*?</script>");
    private static final Pattern TAG_PATTERN = Pattern.compile("<[^>]+>");
    private static final Pattern CONTROL_CHARS = Pattern.compile("[\\p{Cntrl}&&[^\\r\\n\\t]]");
    private static final Pattern SQL_META_CHARS = Pattern.compile("['\";]+|(--)+");
    private static final Pattern MULTIPLE_SPACES = Pattern.compile("\\s+");

    private StringSanitizer() {
        // Utility class
    }

    public static String sanitize(final String rawValue) {
        if (NullHelper.isNull(rawValue)) {
            return null;
        }

        String value = Normalizer.normalize(rawValue, Normalizer.Form.NFKC);
        value = SCRIPT_PATTERN.matcher(value).replaceAll(TextHelper.EMPTY);
        value = TAG_PATTERN.matcher(value).replaceAll(TextHelper.EMPTY);
        value = CONTROL_CHARS.matcher(value).replaceAll(TextHelper.EMPTY);
        value = SQL_META_CHARS.matcher(value).replaceAll(TextHelper.EMPTY);
        value = MULTIPLE_SPACES.matcher(value).replaceAll(" ");
        value = value.trim();
        if (value.isEmpty()) {
            return null;
        }
        return value;
    }

    public static String sanitizeToLower(final String rawValue) {
        final String sanitized = sanitize(rawValue);
        return NullHelper.isNull(sanitized) ? null : sanitized.toLowerCase(Locale.ROOT);
    }
}
