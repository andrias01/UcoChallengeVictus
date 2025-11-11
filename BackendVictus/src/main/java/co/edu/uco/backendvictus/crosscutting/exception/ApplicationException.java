package co.edu.uco.backendvictus.crosscutting.exception;

/**
 * Represents application layer errors.
 */
public class ApplicationException extends BackendVictusException {

    private static final long serialVersionUID = 1L;

    public ApplicationException(final String userMessage) {
        this(userMessage, userMessage, null);
    }

    public ApplicationException(final String userMessage, final String technicalMessage) {
        this(userMessage, technicalMessage, null);
    }

    public ApplicationException(final String userMessage, final String technicalMessage, final Exception exception) {
        super(userMessage, technicalMessage, exception, Layer.APPLICATION);
    }
}
