package com.example.crudrapido.controller;

import java.time.LocalDateTime;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.crudrapido.dto.request.AtentionRequestDto;
import com.example.crudrapido.dto.response.AtentionResponseDTO;
import com.example.crudrapido.service.AtentionService;
import com.example.crudrapido.service.StudentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/atenciones")
@RequiredArgsConstructor
@Slf4j
public class AtentionController {

    private final AtentionService atentionService;
    private final StudentService patientService;

    @Operation(summary = "Crear una nueva atención médica")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Atención Creado"),
            @ApiResponse(responseCode = "400", description = "Request Válido"),
            @ApiResponse(responseCode = "404", description = "Paciente o empleado no encontado")
    })

    /**
     * EQUIVALENCIA DE FIRMA Y TIPADO (Spring Boot vs Laravel)
     * * @PostMapping                                      -> Route::post('/atenciones', ...)
     * ResponseEntity<AtentionResponseDTO>               -> : JsonResponse (Tipo de retorno estricto)
     * @Valid @RequestBody AtencionRequestDto requestDto  -> AtencionRequest $request (Form Request + Validación)
     * * COMPARATIVA DE ESTRUCTURAS:
     * En Laravel, el Form Request intercepta la petición, valida y si falla, corta el flujo.
     * En Spring, '@Valid' hace exactamente lo mismo: si los campos del DTO no cumplen las
     * reglas (como @NotNull o @Size), arroja una excepción antes de ejecutar el código de abajo.
     */
    @PostMapping
    public ResponseEntity<AtentionResponseDTO> crearAtencion(@Valid @RequestBody AtentionRequestDto requestDto) {
        

        /**
         * CAPA DE NEGOCIO Y PROCESAMIENTO
         * * [Spring] AtentionResponseDTO response = atencionService.crearAtencion(requestDto);
         * [Laravel] $atencion = $this->atencionService->crearAtencion($request->validated());
         * * Nota de Arquitectura: En Spring el Service ya procesa, mapea con MapStruct/modelMapper
         * y retorna el DTO limpio listo para JSON. En Laravel, el Service suele retornar el 
         * Modelo Eloquent puro y el controlador lo pasa por un API Resource.
         */
        AtentionResponseDTO response = atentionService.crearAtention(requestDto);

        log.info("Atención médica creada exitosamente con ID asignado: {}", response.getId());

        /**
         * RETORNO DEL ENVOLTORIO HTTP (WRAPPER)
         * * [Spring] ResponseEntity.status(HttpStatus.CREATED).body(response)
         * [Laravel] response()->json(new AtentionResource($atencion), 201)
         * * Explicación del Wrapper:
         * ResponseEntity es el contenedor genérico de Spring que maneja los metadatos HTTP.
         * Al pasarle <AtentionResponseDTO>, el serializador de Jackson transforma ese DTO 
         * automáticamente en un objeto JSON en el Body, mapeando los tipos de Java 
         * (Long -> number, String -> string, LocalDateTime -> ISO String).
         */
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

        @Operation(summary = "Listar todas las atenciones (paginadas)")
    @GetMapping
    public ResponseEntity<Page<AtentionResponseDTO>> listarTodas(@ParameterObject Pageable pageable) {
        log.info("Listando todas las atenciones, page={} size={}", pageable.getPageNumber(), pageable.getPageSize());
        return ResponseEntity.ok(atentionService.listarTodas(pageable));
    }


    @Operation(summary = "Listar atenciones por rango de fechas (paginadas)")
/**
     * EQUIVALENCIA DE FIRMA, VERBO Y QUERY PARAMS (Spring Boot vs Laravel)
     * * @GetMapping("/fecha")                                      -> Route::get('/atenciones/fecha', ...)
     * ResponseEntity<Page<AtentionResponseDTO>>                     -> : JsonResponse (Envoltorio con metadatos de paginación)
     * @RequestParam @DateTimeFormat(...) LocalDateTime fechaInicio -> $request->input('fechaInicio') (Cast automático a objeto de fecha)
     * @ParameterObject Pageable pageable                           -> $request->input('page') y $request->input('size') unificados
     * * * COMPARATIVA DE PARÁMETROS:
     * En Laravel, extraes los query params sueltos del contenedor `$request`. Si quieres validar el formato de fecha,
     * lo haces en las reglas de validación (e.g., 'date_format:Y-m-d\TH:i:s').
     * En Spring, '@RequestParam' mapea la variable de la URL directamente al argumento del método, y '@DateTimeFormat'
     * actúa como el validador/parseador interceptando el String de la URL para convertirlo nativamente en un 'LocalDateTime'.
     */
    @GetMapping("/fecha")
    public ResponseEntity<Page<AtentionResponseDTO>> listarPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
            @ParameterObject Pageable pageable) {
        // Viene de la Clase PageableHandlerMethodArgumentResolver
        // sambeos que son tres page, size y sort

        log.info("Buscando atenciones entre {} y {}, page={} size={}",
                fechaInicio, fechaFin, pageable.getPageNumber(), pageable.getPageSize());

        /**
         * CAPA DE NEGOCIO Y PAGINACIÓN ESTRUCTURADA
         * * [Spring] Page<AtentionResponseDTO> resultado = atencionService.listarPorRangoFechas(fechaInicio, fechaFin, pageable);
         * [Laravel] $atenciones = $this->atencionService->listarPorRangoFechas($fechaInicio, $fechaFin, $perPage);
         * * * Nota de Arquitectura sobre Paginación:
         * En Spring, pasas la interfaz 'Pageable' (que ya encapsula el número de página, el tamaño 'size' y opcionalmente el ordenamiento)
         * directo al Repository de JPA. Este ejecuta automáticamente dos consultas tras bambalinas: el SELECT con LIMIT/OFFSET y un SELECT COUNT(*)
         * para calcular el total. Todo se empaqueta en un objeto de tipo 'Page'.
         * En Laravel, esto equivale a usar el método `->paginate($perPage)`, el cual retorna un objeto 'LengthAwarePaginator'.
         */
        log.info("Buscando atenciones entre {} y {}, page={} size={}",
                fechaInicio, fechaFin, pageable.getPageNumber(), pageable.getPageSize());
        return ResponseEntity.ok(atentionService.listarPorRangoFechas(fechaInicio, fechaFin, pageable));
    }

        @Operation(summary = "Obtener una atención por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<AtentionResponseDTO> obtenerAtencionPorId(@PathVariable Long id) {
        log.info("Buscando atención con id={}", id);
        return ResponseEntity.ok(atentionService.obtenerAtentionPorId(id));
    }

    @Operation(summary = "Actualizar una atención existente")
    @PutMapping("/{id}")
    public ResponseEntity<AtentionResponseDTO> actualizarAtencion(
            @PathVariable Long id,
            @Valid @RequestBody AtentionRequestDto requestDto) {
        log.info("Solicitud para actualizar atención con id={}: {}", id, requestDto);
        AtentionResponseDTO response = atentionService.actualizarAtention(id, requestDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar una atención")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAtencion(@PathVariable Long id) {
        log.info("Solicitud para eliminar atención con id={}", id);
        atentionService.eliminarAtention(id);
        return ResponseEntity.noContent().build();
    }
}
