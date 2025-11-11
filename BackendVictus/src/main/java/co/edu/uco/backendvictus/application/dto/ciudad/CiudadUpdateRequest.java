package co.edu.uco.backendvictus.application.dto.ciudad;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CiudadUpdateRequest(
        @NotNull(message = "El departamento es obligatorio") UUID departamentoId,
        @NotBlank(message = "El nombre de la ciudad es obligatorio")
        @Size(max = 120, message = "El nombre de la ciudad no puede superar 120 caracteres") String nombre,
        boolean activo) {
}
