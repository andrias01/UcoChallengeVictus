package co.edu.uco.backendvictus.crosscutting.helpers;

/**
 * Numeric helper operations used across the project.
 */
public final class NumericHelper {

    public static final int ZERO = 0;

    private NumericHelper() {
        // Utility class
    }

    public static <T extends Number> boolean isGreater(final T numberOne, final T numberTwo) {
        return numberOne.doubleValue() > numberTwo.doubleValue();
    }

    public static <T extends Number> boolean isLess(final T numberOne, final T numberTwo) {
        return numberOne.doubleValue() < numberTwo.doubleValue();
    }

    public static <T extends Number> boolean isDifferent(final T numberOne, final T numberTwo) {
        return numberOne.doubleValue() != numberTwo.doubleValue();
    }

    public static <T extends Number> boolean isEqual(final T numberOne, final T numberTwo) {
        return numberOne.doubleValue() == numberTwo.doubleValue();
    }

    public static <T extends Number> boolean isGreaterOrEqual(final T numberOne, final T numberTwo) {
        return numberOne.doubleValue() >= numberTwo.doubleValue();
    }

    public static <T extends Number> boolean isLessOrEqual(final T numberOne, final T numberTwo) {
        return numberOne.doubleValue() <= numberTwo.doubleValue();
    }

    public static <T extends Number> boolean isBetween(final T number, final T initialLimit, final T finalLimit,
            final boolean includeInitialLimit, final boolean includeFinalLimit) {
        return (includeInitialLimit ? isGreaterOrEqual(number, initialLimit) : isGreater(number, initialLimit))
                && (includeFinalLimit ? isLessOrEqual(number, finalLimit) : isLess(number, finalLimit));
    }
}
