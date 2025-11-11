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

import co.edu.uco.backendvictus.application.dto.pais.PaisCreateRequest;
import co.edu.uco.backendvictus.application.dto.pais.PaisResponse;
import co.edu.uco.backendvictus.application.dto.pais.PaisUpdateCommand;
import co.edu.uco.backendvictus.application.dto.pais.PaisUpdateRequest;
import co.edu.uco.backendvictus.application.dto.common.ChangeResponseDTO;
import co.edu.uco.backendvictus.application.dto.common.ResponseDTO;
import co.edu.uco.backendvictus.application.usecase.pais.CreatePaisUseCase;
import co.edu.uco.backendvictus.application.usecase.pais.DeletePaisUseCase;
import co.edu.uco.backendvictus.application.usecase.pais.FindPaisByIdUseCase;
import co.edu.uco.backendvictus.application.usecase.pais.ListPaisUseCase;
import co.edu.uco.backendvictus.application.usecase.pais.UpdatePaisUseCase;
import co.edu.uco.backendvictus.crosscutting.helpers.BooleanHelper;
import co.edu.uco.backendvictus.crosscutting.helpers.StringSanitizer;
import co.edu.uco.backendvictus.crosscutting.helpers.TextHelper;
import co.edu.uco.backendvictus.crosscutting.helpers.UUIDHelper;
import co.edu.uco.backendvictus.domain.model.filter.PaisFilter;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/paises")
@Validated
public class PaisController {

    private final CreatePaisUseCase createPaisUseCase;
    private final ListPaisUseCase listPaisUseCase;
    private final UpdatePaisUseCase updatePaisUseCase;
    private final DeletePaisUseCase deletePaisUseCase;
    private final FindPaisByIdUseCase findPaisByIdUseCase;

    public PaisController(final CreatePaisUseCase createPaisUseCase, final ListPaisUseCase listPaisUseCase,
            final UpdatePaisUseCase updatePaisUseCase, final DeletePaisUseCase deletePaisUseCase,
            final FindPaisByIdUseCase findPaisByIdUseCase) {
        this.createPaisUseCase = createPaisUseCase;
        this.listPaisUseCase = listPaisUseCase;
        this.updatePaisUseCase = updatePaisUseCase;
        this.deletePaisUseCase = deletePaisUseCase;
        this.findPaisByIdUseCase = findPaisByIdUseCase;
    }

    @PostMapping("/crear")
    public Mono<ResponseEntity<ResponseDTO<PaisResponse>>> crear(@Valid @RequestBody final PaisCreateRequest request) {
        final PaisCreateRequest sanitized = new PaisCreateRequest(StringSanitizer.sanitize(request.nombre()),
                request.activo());
        return createPaisUseCase.execute(sanitized)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(ResponseDTO.of("País creado correctamente", response)));
    }

    @GetMapping
    public Mono<ResponseEntity<ResponseDTO<List<PaisResponse>>>> listar(
            @RequestParam(value = "nombre", required = false) final String nombre,
            @RequestParam(value = "activo", required = false) final Boolean activo) {
        final PaisFilter filter = new PaisFilter(nombre, activo);
        return listPaisUseCase.execute(filter)
                .collectList()
                .map(list -> ResponseEntity.ok(ResponseDTO.of("Países obtenidos correctamente", list)));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ResponseDTO<PaisResponse>>> findById(@PathVariable("id") final UUID id) {
        return findPaisByIdUseCase.execute(id)
                .map(response -> ResponseEntity.ok(ResponseDTO.of("País encontrado", response)));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ResponseDTO<ChangeResponseDTO<PaisResponse>>>> actualizar(
            @PathVariable("id") final UUID id,
            @Valid @RequestBody final PaisUpdateRequest request) {
        final PaisUpdateRequest sanitized = new PaisUpdateRequest(StringSanitizer.sanitize(request.nombre()),
                request.activo());
        final PaisUpdateCommand command = new PaisUpdateCommand(id, sanitized.nombre(), sanitized.activo());
        return updatePaisUseCase.execute(command)
                .map(change -> ResponseEntity.ok(ResponseDTO.of(
                        String.format("País actualizado: antes=%s, después=%s", change.before(), change.after()),
                        change)));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<ResponseDTO<ChangeResponseDTO<PaisResponse>>>> eliminar(
            @PathVariable("id") final UUID id) {
        return deletePaisUseCase.execute(id)
                .map(change -> ResponseEntity.ok(ResponseDTO.of(
                        String.format("País eliminado. Estado previo=%s", change.before()), change)));
    }

    @GetMapping("/dummy")
    public Mono<ResponseEntity<ResponseDTO<PaisResponse>>> dummy() {
        final PaisResponse response = new PaisResponse(UUIDHelper.getDefault(), TextHelper.EMPTY, BooleanHelper.DEFAULT);
        return Mono.just(ResponseEntity.ok(ResponseDTO.of("Dummy país", response)));
    }
}
