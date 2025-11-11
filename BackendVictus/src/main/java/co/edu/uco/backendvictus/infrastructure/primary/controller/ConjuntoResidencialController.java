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

import co.edu.uco.backendvictus.application.dto.conjunto.ConjuntoCreateRequest;
import co.edu.uco.backendvictus.application.dto.conjunto.ConjuntoResponse;
import co.edu.uco.backendvictus.application.dto.conjunto.ConjuntoUpdateCommand;
import co.edu.uco.backendvictus.application.dto.conjunto.ConjuntoUpdateRequest;
import co.edu.uco.backendvictus.application.dto.common.ChangeResponseDTO;
import co.edu.uco.backendvictus.application.dto.common.ResponseDTO;
import co.edu.uco.backendvictus.application.usecase.conjunto.CreateConjuntoUseCase;
import co.edu.uco.backendvictus.application.usecase.conjunto.DeleteConjuntoUseCase;
import co.edu.uco.backendvictus.application.usecase.conjunto.FindConjuntoByIdUseCase;
import co.edu.uco.backendvictus.application.usecase.conjunto.ListConjuntoUseCase;
import co.edu.uco.backendvictus.application.usecase.conjunto.UpdateConjuntoUseCase;
import co.edu.uco.backendvictus.crosscutting.helpers.BooleanHelper;
import co.edu.uco.backendvictus.crosscutting.helpers.StringSanitizer;
import co.edu.uco.backendvictus.crosscutting.helpers.TextHelper;
import co.edu.uco.backendvictus.crosscutting.helpers.UUIDHelper;
import co.edu.uco.backendvictus.domain.model.filter.ConjuntoResidencialFilter;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/conjuntos")
@Validated
public class ConjuntoResidencialController {

    private final CreateConjuntoUseCase createConjuntoUseCase;
    private final ListConjuntoUseCase listConjuntoUseCase;
    private final UpdateConjuntoUseCase updateConjuntoUseCase;
    private final DeleteConjuntoUseCase deleteConjuntoUseCase;
    private final FindConjuntoByIdUseCase findConjuntoByIdUseCase;

    public ConjuntoResidencialController(final CreateConjuntoUseCase createConjuntoUseCase,
            final ListConjuntoUseCase listConjuntoUseCase, final UpdateConjuntoUseCase updateConjuntoUseCase,
            final DeleteConjuntoUseCase deleteConjuntoUseCase, final FindConjuntoByIdUseCase findConjuntoByIdUseCase) {
        this.createConjuntoUseCase = createConjuntoUseCase;
        this.listConjuntoUseCase = listConjuntoUseCase;
        this.updateConjuntoUseCase = updateConjuntoUseCase;
        this.deleteConjuntoUseCase = deleteConjuntoUseCase;
        this.findConjuntoByIdUseCase = findConjuntoByIdUseCase;
    }

    @PostMapping("/crear")
    public Mono<ResponseEntity<ResponseDTO<ConjuntoResponse>>> crear(
            @Valid @RequestBody final ConjuntoCreateRequest request) {
        final ConjuntoCreateRequest sanitized = new ConjuntoCreateRequest(request.ciudadId(), request.administradorId(),
                StringSanitizer.sanitize(request.nombre()), StringSanitizer.sanitize(request.direccion()),
                request.activo());
        return createConjuntoUseCase.execute(sanitized)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(ResponseDTO.of("Conjunto residencial creado correctamente", response)));
    }

    @GetMapping
    public Mono<ResponseEntity<ResponseDTO<List<ConjuntoResponse>>>> listar(
            @RequestParam(value = "nombre", required = false) final String nombre,
            @RequestParam(value = "ciudadId", required = false) final UUID ciudadId,
            @RequestParam(value = "administradorId", required = false) final UUID administradorId,
            @RequestParam(value = "activo", required = false) final Boolean activo) {
        final ConjuntoResidencialFilter filter = new ConjuntoResidencialFilter(nombre, ciudadId, administradorId,
                activo);
        return listConjuntoUseCase.execute(filter)
                .collectList()
                .map(list -> ResponseEntity.ok(ResponseDTO.of("Conjuntos residenciales obtenidos correctamente", list)));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ResponseDTO<ConjuntoResponse>>> findById(@PathVariable("id") final UUID id) {
        return findConjuntoByIdUseCase.execute(id)
                .map(response -> ResponseEntity.ok(ResponseDTO.of("Conjunto residencial encontrado", response)));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ResponseDTO<ChangeResponseDTO<ConjuntoResponse>>>> actualizar(
            @PathVariable("id") final UUID id,
            @Valid @RequestBody final ConjuntoUpdateRequest request) {
        final ConjuntoUpdateRequest sanitized = new ConjuntoUpdateRequest(request.ciudadId(), request.administradorId(),
                StringSanitizer.sanitize(request.nombre()), StringSanitizer.sanitize(request.direccion()),
                request.activo());
        final ConjuntoUpdateCommand command = new ConjuntoUpdateCommand(id, sanitized.ciudadId(),
                sanitized.administradorId(), sanitized.nombre(), sanitized.direccion(), sanitized.activo());
        return updateConjuntoUseCase.execute(command)
                .map(change -> ResponseEntity.ok(ResponseDTO.of(
                        String.format("Conjunto residencial actualizado: antes=%s, después=%s", change.before(),
                                change.after()),
                        change)));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<ResponseDTO<ChangeResponseDTO<ConjuntoResponse>>>> eliminar(
            @PathVariable("id") final UUID id) {
        return deleteConjuntoUseCase.execute(id)
                .map(change -> ResponseEntity.ok(ResponseDTO.of(
                        String.format("Conjunto residencial eliminado. Estado previo=%s", change.before()),
                        change)));
    }

    @GetMapping("/dummy")
    public Mono<ResponseEntity<ResponseDTO<ConjuntoResponse>>> dummy() {
        final ConjuntoResponse response = new ConjuntoResponse(UUIDHelper.getDefault(), UUIDHelper.getDefault(),
                UUIDHelper.getDefault(), TextHelper.EMPTY, TextHelper.EMPTY, BooleanHelper.DEFAULT);
        return Mono.just(ResponseEntity.ok(ResponseDTO.of("Dummy conjunto residencial", response)));
    }
}
