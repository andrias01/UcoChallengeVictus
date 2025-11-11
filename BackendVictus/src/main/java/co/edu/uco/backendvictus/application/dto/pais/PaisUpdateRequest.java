package co.edu.uco.backendvictus.application.dto.pais;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PaisUpdateRequest(
        @NotBlank(message = "El nombre del pais es obligatorio")
        @Size(max = 120, message = "El nombre del pais no puede superar 120 caracteres") String nombre,
        boolean activo) {
}
