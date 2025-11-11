package co.edu.uco.backendvictus.application.dto.administrador;

import java.util.UUID;

public record AdministradorUpdateCommand(UUID id, String primerNombre, String segundoNombres, String primerApellido,
        String segundoApellido, String email, String telefono, boolean activo) {
}
