package co.edu.uco.backendvictus.application.dto.departamento;

import java.util.UUID;

public record DepartamentoUpdateCommand(UUID id, UUID paisId, String nombre, boolean activo) {
}
