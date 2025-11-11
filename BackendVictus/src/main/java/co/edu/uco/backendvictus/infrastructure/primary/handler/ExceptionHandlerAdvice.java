package co.edu.uco.backendvictus.infrastructure.primary.handler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebInputException;

import co.edu.uco.backendvictus.crosscutting.exception.ApplicationException;
import co.edu.uco.backendvictus.crosscutting.exception.DomainException;
import co.edu.uco.backendvictus.crosscutting.exception.InfrastructureException;
import co.edu.uco.backendvictus.crosscutting.helpers.LoggerHelper;
import co.edu.uco.backendvictus.crosscutting.helpers.TextHelper;
import co.edu.uco.backendvictus.application.dto.common.ResponseDTO;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    private static final org.slf4j.Logger LOGGER = LoggerHelper.getLogger(ExceptionHandlerAdvice.class);

    @ExceptionHandler(DomainException.class)
    public Mono<ResponseEntity<ResponseDTO<ErrorResponse>>> handleDomainException(final DomainException exception,
            final ServerWebExchange exchange) {
        LOGGER.warn("Error de dominio: {}", exception.getTechnicalMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, exception.getUserMessage(), exchange, null);
    }

    @ExceptionHandler(ApplicationException.class)
    public Mono<ResponseEntity<ResponseDTO<ErrorResponse>>> handleApplicationException(final ApplicationException exception,
            final ServerWebExchange exchange) {
        LOGGER.warn("Error de aplicacion: {}", exception.getTechnicalMessage());
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, exception.getUserMessage(), exchange, null);
    }

    @ExceptionHandler(InfrastructureException.class)
    public Mono<ResponseEntity<ResponseDTO<ErrorResponse>>> handleInfrastructureException(
            final InfrastructureException exception,
            final ServerWebExchange exchange) {
        LOGGER.error("Error de infraestructura: {}", exception.getTechnicalMessage());
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getUserMessage(), exchange, null);
    }

    @ExceptionHandler(ServerWebInputException.class)
    public Mono<ResponseEntity<ResponseDTO<ErrorResponse>>> handleServerWebInputException(
            final ServerWebInputException exception, final ServerWebExchange exchange) {
        final String message = TextHelper.isNull(exception.getReason()) || TextHelper.isEmptyAfterTrim(exception.getReason())
                ? "El cuerpo de la solicitud no es válido"
                : TextHelper.applyTrim(exception.getReason());
        LOGGER.warn("Error de entrada: {}", message);
        return buildResponse(HttpStatus.BAD_REQUEST, message, exchange, null);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ResponseDTO<ErrorResponse>>> handleValidationException(
            final WebExchangeBindException exception, final ServerWebExchange exchange) {
        final String message = "Los datos proporcionados no son válidos";
        LOGGER.warn("Error de validacion: {}", exception.getAllErrors().size());
        final var details = exception.getAllErrors().stream()
                .map(error -> {
                    final String field = error instanceof FieldError fieldError ? fieldError.getField()
                            : error.getObjectName();
                    final String defaultMessage = TextHelper.applyTrim(error.getDefaultMessage());
                    return TextHelper.applyTrim(field + ": " + defaultMessage);
                })
                .toList();
        return buildResponse(HttpStatus.BAD_REQUEST, message, exchange, details);
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ResponseDTO<ErrorResponse>>> handleGenericException(final Exception exception,
            final ServerWebExchange exchange) {
        final String message = TextHelper.isNull(exception.getMessage()) || TextHelper.isEmptyAfterTrim(exception.getMessage())
                ? "Ocurrió un error inesperado"
                : TextHelper.applyTrim(exception.getMessage());
        LOGGER.error("Error inesperado: {}", message);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, message, exchange, null);
    }

    private Mono<ResponseEntity<ResponseDTO<ErrorResponse>>> buildResponse(final HttpStatus status, final String message,
            final ServerWebExchange exchange, final Object details) {
        final String path = exchange.getRequest().getURI().getPath();
        final ErrorResponse response = new ErrorResponse(LocalDateTime.now(), path, details);
        return Mono.just(ResponseEntity.status(status).body(ResponseDTO.of(message, response)));
    }
}
