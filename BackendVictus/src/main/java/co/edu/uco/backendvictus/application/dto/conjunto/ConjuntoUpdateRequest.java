package co.edu.uco.backendvictus.application.dto.conjunto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ConjuntoUpdateRequest(
        @NotNull(message = "La ciudad es obligatoria") UUID ciudadId,
        @NotNull(message = "El administrador es obligatorio") UUID administradorId,
        @NotBlank(message = "El nombre del conjunto es obligatorio")
        @Size(max = 150, message = "El nombre del conjunto no puede superar 150 caracteres") String nombre,
        @NotBlank(message = "La direccion es obligatoria")
        @Size(max = 180, message = "La direccion no puede superar 180 caracteres") String direccion,
        boolean activo) {
}
