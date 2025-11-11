package co.edu.uco.backendvictus.crosscutting.exception;

/**
 * Represents infrastructure layer errors.
 */
public class InfrastructureException extends BackendVictusException {

    private static final long serialVersionUID = 1L;

    public InfrastructureException(final String userMessage) {
        this(userMessage, userMessage, null);
    }

    public InfrastructureException(final String userMessage, final String technicalMessage) {
        this(userMessage, technicalMessage, null);
    }

    public InfrastructureException(final String userMessage, final String technicalMessage, final Exception exception) {
        super(userMessage, technicalMessage, exception, Layer.INFRASTRUCTURE);
    }
}
