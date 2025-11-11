package co.edu.uco.backendvictus.crosscutting.exception;

/**
 * Identifies the layer where an exception was originated.
 */
public enum Layer {
    GENERAL,
    CROSSCUTTING,
    DOMAIN,
    APPLICATION,
    INFRASTRUCTURE,
    DATA
}
