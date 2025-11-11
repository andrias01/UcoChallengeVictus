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

import co.edu.uco.backendvictus.application.dto.ciudad.CiudadCreateRequest;
import co.edu.uco.backendvictus.application.dto.ciudad.CiudadResponse;
import co.edu.uco.backendvictus.application.dto.ciudad.CiudadUpdateCommand;
import co.edu.uco.backendvictus.application.dto.ciudad.CiudadUpdateRequest;
import co.edu.uco.backendvictus.application.dto.common.ChangeResponseDTO;
import co.edu.uco.backendvictus.application.dto.common.ResponseDTO;
import co.edu.uco.backendvictus.application.usecase.ciudad.CreateCiudadUseCase;
import co.edu.uco.backendvictus.application.usecase.ciudad.DeleteCiudadUseCase;
import co.edu.uco.backendvictus.application.usecase.ciudad.FindCiudadByIdUseCase;
import co.edu.uco.backendvictus.application.usecase.ciudad.ListCiudadUseCase;
import co.edu.uco.backendvictus.application.usecase.ciudad.UpdateCiudadUseCase;
import co.edu.uco.backendvictus.crosscutting.helpers.BooleanHelper;
import co.edu.uco.backendvictus.crosscutting.helpers.StringSanitizer;
import co.edu.uco.backendvictus.crosscutting.helpers.TextHelper;
import co.edu.uco.backendvictus.crosscutting.helpers.UUIDHelper;
import co.edu.uco.backendvictus.domain.model.filter.CiudadFilter;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/ciudades")
@Validated
public class CiudadController {

    private final CreateCiudadUseCase createCiudadUseCase;
    private final ListCiudadUseCase listCiudadUseCase;
    private final UpdateCiudadUseCase updateCiudadUseCase;
    private final DeleteCiudadUseCase deleteCiudadUseCase;
    private final FindCiudadByIdUseCase findCiudadByIdUseCase;

    public CiudadController(final CreateCiudadUseCase createCiudadUseCase, final ListCiudadUseCase listCiudadUseCase,
            final UpdateCiudadUseCase updateCiudadUseCase, final DeleteCiudadUseCase deleteCiudadUseCase,
            final FindCiudadByIdUseCase findCiudadByIdUseCase) {
        this.createCiudadUseCase = createCiudadUseCase;
        this.listCiudadUseCase = listCiudadUseCase;
        this.updateCiudadUseCase = updateCiudadUseCase;
        this.deleteCiudadUseCase = deleteCiudadUseCase;
        this.findCiudadByIdUseCase = findCiudadByIdUseCase;
    }

    @PostMapping("/crear")
    public Mono<ResponseEntity<ResponseDTO<CiudadResponse>>> crear(
            @Valid @RequestBody final CiudadCreateRequest request) {
        final CiudadCreateRequest sanitized = new CiudadCreateRequest(request.departamentoId(),
                StringSanitizer.sanitize(request.nombre()), request.activo());
        return createCiudadUseCase.execute(sanitized)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(ResponseDTO.of("Ciudad creada correctamente", response)));
    }

    @GetMapping
    public Mono<ResponseEntity<ResponseDTO<List<CiudadResponse>>>> listar(
            @RequestParam(value = "nombre", required = false) final String nombre,
            @RequestParam(value = "departamentoId", required = false) final UUID departamentoId,
            @RequestParam(value = "activo", required = false) final Boolean activo) {
        final CiudadFilter filter = new CiudadFilter(nombre, departamentoId, activo);
        return listCiudadUseCase.execute(filter)
                .collectList()
                .map(list -> ResponseEntity.ok(ResponseDTO.of("Ciudades obtenidas correctamente", list)));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ResponseDTO<CiudadResponse>>> findById(@PathVariable("id") final UUID id) {
        return findCiudadByIdUseCase.execute(id)
                .map(response -> ResponseEntity.ok(ResponseDTO.of("Ciudad encontrada", response)));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ResponseDTO<ChangeResponseDTO<CiudadResponse>>>> actualizar(
            @PathVariable("id") final UUID id,
            @Valid @RequestBody final CiudadUpdateRequest request) {
        final CiudadUpdateRequest sanitized = new CiudadUpdateRequest(request.departamentoId(),
                StringSanitizer.sanitize(request.nombre()), request.activo());
        final CiudadUpdateCommand command = new CiudadUpdateCommand(id, sanitized.departamentoId(),
                sanitized.nombre(), sanitized.activo());
        return updateCiudadUseCase.execute(command)
                .map(change -> ResponseEntity.ok(ResponseDTO.of(
                        String.format("Ciudad actualizada: antes=%s, después=%s", change.before(), change.after()),
                        change)));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<ResponseDTO<ChangeResponseDTO<CiudadResponse>>>> eliminar(
            @PathVariable("id") final UUID id) {
        return deleteCiudadUseCase.execute(id)
                .map(change -> ResponseEntity.ok(ResponseDTO.of(
                        String.format("Ciudad eliminada. Estado previo=%s", change.before()), change)));
    }

    @GetMapping("/dummy")
    public Mono<ResponseEntity<ResponseDTO<CiudadResponse>>> dummy() {
        final CiudadResponse response = new CiudadResponse(UUIDHelper.getDefault(), UUIDHelper.getDefault(),
                TextHelper.EMPTY, BooleanHelper.DEFAULT);
        return Mono.just(ResponseEntity.ok(ResponseDTO.of("Dummy ciudad", response)));
    }
}
