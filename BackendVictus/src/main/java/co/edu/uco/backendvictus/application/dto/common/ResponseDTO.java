package co.edu.uco.backendvictus.application.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ResponseDTO<T>(String message, T data) {

    public static <T> ResponseDTO<T> of(final String message, final T data) {
        return new ResponseDTO<>(message, data);
    }
}
