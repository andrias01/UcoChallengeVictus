package co.edu.uco.backendvictus.application.dto.conjunto;

import java.util.UUID;

public record ConjuntoUpdateCommand(UUID id, UUID ciudadId, UUID administradorId, String nombre, String direccion,
        boolean activo) {
}
