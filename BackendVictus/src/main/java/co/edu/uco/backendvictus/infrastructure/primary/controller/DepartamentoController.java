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

import co.edu.uco.backendvictus.application.dto.departamento.DepartamentoCreateRequest;
import co.edu.uco.backendvictus.application.dto.departamento.DepartamentoResponse;
import co.edu.uco.backendvictus.application.dto.departamento.DepartamentoUpdateCommand;
import co.edu.uco.backendvictus.application.dto.departamento.DepartamentoUpdateRequest;
import co.edu.uco.backendvictus.application.dto.common.ChangeResponseDTO;
import co.edu.uco.backendvictus.application.dto.common.ResponseDTO;
import co.edu.uco.backendvictus.application.usecase.departamento.CreateDepartamentoUseCase;
import co.edu.uco.backendvictus.application.usecase.departamento.DeleteDepartamentoUseCase;
import co.edu.uco.backendvictus.application.usecase.departamento.FindDepartamentoByIdUseCase;
import co.edu.uco.backendvictus.application.usecase.departamento.ListDepartamentoUseCase;
import co.edu.uco.backendvictus.application.usecase.departamento.UpdateDepartamentoUseCase;
import co.edu.uco.backendvictus.crosscutting.helpers.BooleanHelper;
import co.edu.uco.backendvictus.crosscutting.helpers.StringSanitizer;
import co.edu.uco.backendvictus.crosscutting.helpers.TextHelper;
import co.edu.uco.backendvictus.crosscutting.helpers.UUIDHelper;
import co.edu.uco.backendvictus.domain.model.filter.DepartamentoFilter;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/departamentos")
@Validated
public class DepartamentoController {

    private final CreateDepartamentoUseCase createDepartamentoUseCase;
    private final ListDepartamentoUseCase listDepartamentoUseCase;
    private final UpdateDepartamentoUseCase updateDepartamentoUseCase;
    private final DeleteDepartamentoUseCase deleteDepartamentoUseCase;
    private final FindDepartamentoByIdUseCase findDepartamentoByIdUseCase;

    public DepartamentoController(final CreateDepartamentoUseCase createDepartamentoUseCase,
            final ListDepartamentoUseCase listDepartamentoUseCase,
            final UpdateDepartamentoUseCase updateDepartamentoUseCase,
            final DeleteDepartamentoUseCase deleteDepartamentoUseCase,
            final FindDepartamentoByIdUseCase findDepartamentoByIdUseCase) {
        this.createDepartamentoUseCase = createDepartamentoUseCase;
        this.listDepartamentoUseCase = listDepartamentoUseCase;
        this.updateDepartamentoUseCase = updateDepartamentoUseCase;
        this.deleteDepartamentoUseCase = deleteDepartamentoUseCase;
        this.findDepartamentoByIdUseCase = findDepartamentoByIdUseCase;
    }

    @PostMapping("/crear")
    public Mono<ResponseEntity<ResponseDTO<DepartamentoResponse>>> crear(
            @Valid @RequestBody final DepartamentoCreateRequest request) {
        final DepartamentoCreateRequest sanitized = new DepartamentoCreateRequest(request.paisId(),
                StringSanitizer.sanitize(request.nombre()), request.activo());
        return createDepartamentoUseCase.execute(sanitized)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(ResponseDTO.of("Departamento creado correctamente", response)));
    }

    @GetMapping
    public Mono<ResponseEntity<ResponseDTO<List<DepartamentoResponse>>>> listar(
            @RequestParam(value = "nombre", required = false) final String nombre,
            @RequestParam(value = "paisId", required = false) final UUID paisId,
            @RequestParam(value = "activo", required = false) final Boolean activo) {
        final DepartamentoFilter filter = new DepartamentoFilter(nombre, paisId, activo);
        return listDepartamentoUseCase.execute(filter)
                .collectList()
                .map(list -> ResponseEntity.ok(ResponseDTO.of("Departamentos obtenidos correctamente", list)));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ResponseDTO<DepartamentoResponse>>> findById(@PathVariable("id") final UUID id) {
        return findDepartamentoByIdUseCase.execute(id)
                .map(response -> ResponseEntity.ok(ResponseDTO.of("Departamento encontrado", response)));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ResponseDTO<ChangeResponseDTO<DepartamentoResponse>>>> actualizar(
            @PathVariable("id") final UUID id,
            @Valid @RequestBody final DepartamentoUpdateRequest request) {
        final DepartamentoUpdateRequest sanitized = new DepartamentoUpdateRequest(request.paisId(),
                StringSanitizer.sanitize(request.nombre()), request.activo());
        final DepartamentoUpdateCommand command = new DepartamentoUpdateCommand(id, sanitized.paisId(),
                sanitized.nombre(), sanitized.activo());
        return updateDepartamentoUseCase.execute(command)
                .map(change -> ResponseEntity.ok(ResponseDTO.of(
                        String.format("Departamento actualizado: antes=%s, después=%s", change.before(),
                                change.after()),
                        change)));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<ResponseDTO<ChangeResponseDTO<DepartamentoResponse>>>> eliminar(
            @PathVariable("id") final UUID id) {
        return deleteDepartamentoUseCase.execute(id)
                .map(change -> ResponseEntity.ok(ResponseDTO.of(
                        String.format("Departamento eliminado. Estado previo=%s", change.before()), change)));
    }

    @GetMapping("/dummy")
    public Mono<ResponseEntity<ResponseDTO<DepartamentoResponse>>> dummy() {
        final DepartamentoResponse response = new DepartamentoResponse(UUIDHelper.getDefault(), UUIDHelper.getDefault(),
                TextHelper.EMPTY, BooleanHelper.DEFAULT);
        return Mono.just(ResponseEntity.ok(ResponseDTO.of("Dummy departamento", response)));
    }
}
