package co.edu.uco.backendvictus.crosscutting.helpers;

import java.util.UUID;

/**
 * UUID helper utilities for the backend.
 */
public final class UUIDHelper {

    private static final String DEFAULT_UUID_STRING = "00000000-0000-0000-0000-000000000000";

    private UUIDHelper() {
        // Utility class
    }

    public static UUID convertToUUID(final String uuidAsString) {
        return UUID.fromString(uuidAsString);
    }

    public static String convertToString(final UUID uuidAsUuid) {
        return uuidAsUuid.toString();
    }

    public static UUID getDefault(final UUID value, final UUID defaultValue) {
        return ObjectHelper.getDefault(value, defaultValue);
    }

    public static UUID getDefault() {
        return convertToUUID(DEFAULT_UUID_STRING);
    }

    public static boolean isDefault(final UUID value) {
        return getDefault(value, getDefault()).compareTo(getDefault()) == 0;
    }

    public static boolean isEqual(final UUID valueOne, final UUID valueTwo) {
        return getDefault(valueOne, getDefault()).compareTo(getDefault(valueTwo, getDefault())) == 0;
    }

    public static UUID generate() {
        return UUID.randomUUID();
    }
}
