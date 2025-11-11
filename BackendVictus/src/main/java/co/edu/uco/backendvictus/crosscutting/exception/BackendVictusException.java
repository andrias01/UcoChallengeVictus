package co.edu.uco.backendvictus.crosscutting.exception;

import co.edu.uco.backendvictus.crosscutting.helpers.ObjectHelper;
import co.edu.uco.backendvictus.crosscutting.helpers.TextHelper;

/**
 * Base runtime exception that captures both user-facing and technical details of an error.
 */
public class BackendVictusException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String userMessage;
    private final String technicalMessage;
    private final Layer layer;

    protected BackendVictusException(final String userMessage, final String technicalMessage,
            final Exception rootException, final Layer layer) {
        super(TextHelper.applyTrim(technicalMessage), ObjectHelper.getDefault(rootException, new Exception()));
        this.userMessage = TextHelper.applyTrim(userMessage);
        this.technicalMessage = TextHelper.applyTrim(technicalMessage);
        this.layer = ObjectHelper.getDefault(layer, Layer.GENERAL);
    }

    public String getUserMessage() {
        return userMessage;
    }

    public String getTechnicalMessage() {
        return technicalMessage;
    }

    public Layer getLayer() {
        return layer;
    }
}
