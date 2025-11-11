package co.edu.uco.backendvictus.application.dto.departamento;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DepartamentoUpdateRequest(
        @NotNull(message = "El pais es obligatorio") UUID paisId,
        @NotBlank(message = "El nombre del departamento es obligatorio")
        @Size(max = 120, message = "El nombre del departamento no puede superar 120 caracteres") String nombre,
        boolean activo) {
}
