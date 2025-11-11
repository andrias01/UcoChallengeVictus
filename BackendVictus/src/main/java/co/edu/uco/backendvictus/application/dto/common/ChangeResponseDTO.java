package co.edu.uco.backendvictus.application.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ChangeResponseDTO<T>(T before, T after) {

    public static <T> ChangeResponseDTO<T> of(final T before, final T after) {
        return new ChangeResponseDTO<>(before, after);
    }
}
