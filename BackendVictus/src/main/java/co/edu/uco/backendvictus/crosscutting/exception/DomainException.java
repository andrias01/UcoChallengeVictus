package co.edu.uco.backendvictus.crosscutting.exception;

/**
 * Represents domain validation errors.
 */
public class DomainException extends BackendVictusException {

    private static final long serialVersionUID = 1L;

    public DomainException(final String userMessage) {
        this(userMessage, userMessage, null);
    }

    public DomainException(final String userMessage, final String technicalMessage) {
        this(userMessage, technicalMessage, null);
    }

    public DomainException(final String userMessage, final String technicalMessage, final Exception exception) {
        super(userMessage, technicalMessage, exception, Layer.DOMAIN);
    }
}
