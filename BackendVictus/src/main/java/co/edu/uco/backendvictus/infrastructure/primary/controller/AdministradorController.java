package co.edu.uco.backendvictus.infrastructure.primary.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uco.backendvictus.application.dto.administrador.AdministradorCreateRequest;
import co.edu.uco.backendvictus.application.dto.administrador.AdministradorResponse;
import co.edu.uco.backendvictus.application.dto.administrador.AdministradorUpdateCommand;
import co.edu.uco.backendvictus.application.dto.administrador.AdministradorUpdateRequest;
import co.edu.uco.backendvictus.application.dto.common.ChangeResponseDTO;
import co.edu.uco.backendvictus.application.dto.common.ResponseDTO;
import co.edu.uco.backendvictus.application.usecase.administrador.CreateAdministradorUseCase;
import co.edu.uco.backendvictus.application.usecase.administrador.DeleteAdministradorUseCase;
import co.edu.uco.backendvictus.application.usecase.administrador.FindAdministradorByIdUseCase;
import co.edu.uco.backendvictus.application.usecase.administrador.ListAdministradorUseCase;
import co.edu.uco.backendvictus.application.usecase.administrador.UpdateAdministradorUseCase;
import co.edu.uco.backendvictus.crosscutting.helpers.BooleanHelper;
import co.edu.uco.backendvictus.crosscutting.helpers.StringSanitizer;
import co.edu.uco.backendvictus.crosscutting.helpers.TextHelper;
import co.edu.uco.backendvictus.crosscutting.helpers.UUIDHelper;
import co.edu.uco.backendvictus.domain.model.filter.AdministradorFilter;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/administradores")
@Validated
public class AdministradorController {

    private final CreateAdministradorUseCase createAdministradorUseCase;
    private final ListAdministradorUseCase listAdministradorUseCase;
    private final UpdateAdministradorUseCase updateAdministradorUseCase;
    private final DeleteAdministradorUseCase deleteAdministradorUseCase;
    private final FindAdministradorByIdUseCase findAdministradorByIdUseCase;

    public AdministradorController(final CreateAdministradorUseCase createAdministradorUseCase,
            final ListAdministradorUseCase listAdministradorUseCase,
            final UpdateAdministradorUseCase updateAdministradorUseCase,
            final DeleteAdministradorUseCase deleteAdministradorUseCase,
            final FindAdministradorByIdUseCase findAdministradorByIdUseCase) {
        this.createAdministradorUseCase = createAdministradorUseCase;
        this.listAdministradorUseCase = listAdministradorUseCase;
        this.updateAdministradorUseCase = updateAdministradorUseCase;
        this.deleteAdministradorUseCase = deleteAdministradorUseCase;
        this.findAdministradorByIdUseCase = findAdministradorByIdUseCase;
    }

    @PostMapping("/crear")
    public Mono<ResponseEntity<ResponseDTO<AdministradorResponse>>> crear(
            @Valid @RequestBody final AdministradorCreateRequest request) {
        final AdministradorCreateRequest sanitized = new AdministradorCreateRequest(
                StringSanitizer.sanitize(request.primerNombre()), StringSanitizer.sanitize(request.segundoNombres()),
                StringSanitizer.sanitize(request.primerApellido()), StringSanitizer.sanitize(request.segundoApellido()),
                StringSanitizer.sanitize(request.email()), StringSanitizer.sanitize(request.telefono()), request.activo());
        return createAdministradorUseCase.execute(sanitized)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(ResponseDTO.of("Administrador creado correctamente", response)));
    }

    @GetMapping
    public Mono<ResponseEntity<ResponseDTO<List<AdministradorResponse>>>> listar(
            @RequestParam(value = "nombre", required = false) final String nombre,
            @RequestParam(value = "email", required = false) final String email,
            @RequestParam(value = "activo", required = false) final Boolean activo) {
        final AdministradorFilter filter = new AdministradorFilter(nombre, email, activo);
        return listAdministradorUseCase.execute(filter)
                .collectList()
                .map(list -> ResponseEntity.ok(ResponseDTO.of("Administradores obtenidos correctamente", list)));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ResponseDTO<AdministradorResponse>>> findById(@PathVariable("id") final UUID id) {
        return findAdministradorByIdUseCase.execute(id)
                .map(response -> ResponseEntity.ok(ResponseDTO.of("Administrador encontrado", response)));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ResponseDTO<ChangeResponseDTO<AdministradorResponse>>>> actualizar(
            @PathVariable("id") final UUID id,
            @Valid @RequestBody final AdministradorUpdateRequest request) {
        final AdministradorUpdateRequest sanitized = new AdministradorUpdateRequest(
                StringSanitizer.sanitize(request.primerNombre()), StringSanitizer.sanitize(request.segundoNombres()),
                StringSanitizer.sanitize(request.primerApellido()), StringSanitizer.sanitize(request.segundoApellido()),
                StringSanitizer.sanitize(request.email()), StringSanitizer.sanitize(request.telefono()),
                request.activo());
        final AdministradorUpdateCommand command = new AdministradorUpdateCommand(id, sanitized.primerNombre(),
                sanitized.segundoNombres(), sanitized.primerApellido(), sanitized.segundoApellido(),
                sanitized.email(), sanitized.telefono(), sanitized.activo());
        return updateAdministradorUseCase.execute(command)
                .map(change -> ResponseEntity.ok(ResponseDTO.of(
                        String.format("Administrador actualizado: antes=%s, después=%s", change.before(),
                                change.after()),
                        change)));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<ResponseDTO<ChangeResponseDTO<AdministradorResponse>>>> eliminar(
            @PathVariable("id") final UUID id) {
        return deleteAdministradorUseCase.execute(id)
                .map(change -> ResponseEntity.ok(ResponseDTO.of(
                        String.format("Administrador eliminado. Estado previo=%s", change.before()), change)));
    }

    @GetMapping("/dummy")
    public Mono<ResponseEntity<ResponseDTO<AdministradorResponse>>> dummy() {
        final AdministradorResponse response = new AdministradorResponse(UUIDHelper.getDefault(), TextHelper.EMPTY,
                TextHelper.EMPTY, TextHelper.EMPTY, TextHelper.EMPTY, TextHelper.EMPTY, TextHelper.EMPTY,
                BooleanHelper.DEFAULT);
        return Mono.just(ResponseEntity.ok(ResponseDTO.of("Dummy administrador", response)));
    }
}
