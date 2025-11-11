package co.edu.uco.backendvictus.application.dto.administrador;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AdministradorCreateRequest(
        @NotBlank(message = "El primer nombre es obligatorio")
        @Size(max = 60, message = "El primer nombre no puede superar 60 caracteres") String primerNombre,
        @Size(max = 100, message = "Los segundos nombres no pueden superar 100 caracteres") String segundoNombres,
        @NotBlank(message = "El primer apellido es obligatorio")
        @Size(max = 60, message = "El primer apellido no puede superar 60 caracteres") String primerApellido,
        @Size(max = 60, message = "El segundo apellido no puede superar 60 caracteres") String segundoApellido,
        @NotBlank(message = "El correo electronico es obligatorio")
        @Email(message = "El correo electronico no es valido")
        @Size(max = 120, message = "El correo electronico no puede superar 120 caracteres") String email,
        @NotBlank(message = "El telefono es obligatorio")
        @Size(max = 20, message = "El telefono no puede superar 20 caracteres") String telefono,
        boolean activo) {
}
