package com.example.crudrapido.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import com.example.crudrapido.dto.request.SpecialtyRequestDTO;
import com.example.crudrapido.dto.response.SpecialtyResponseDTO;
import com.example.crudrapido.service.SpecialtyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/especialidades")
@RequiredArgsConstructor
@Slf4j
public class SpecialtyController {

    private final SpecialtyService especialidadService;

    @Operation(summary = "Crear una nueva especialidad")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Especialidad Creada"),
            @ApiResponse(responseCode = "400", description = "Request Inválido")
    })
    @PostMapping
    public ResponseEntity<SpecialtyResponseDTO> crearEspecialidad(@Valid @RequestBody SpecialtyRequestDTO requestDto){
        log.info("Solicitud para crear especialidad recibida: {}", requestDto);
        SpecialtyResponseDTO response = especialidadService.crearSpecialty(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar todas las especialidades (paginadas)")
    @GetMapping
    public ResponseEntity<Page<SpecialtyResponseDTO>> listarEspecialidades(@ParameterObject Pageable pageable){
        log.info("Listando todas las especialidades, page={} size={}", pageable.getPageNumber(), pageable.getPageSize());
        return ResponseEntity.ok(especialidadService.listarSpecialty(pageable));
    }

    @Operation(summary = "Buscar especialidades por nombre (paginadas)")
    @GetMapping("/nombre")
    public ResponseEntity<Page<SpecialtyResponseDTO>> buscarPorNombre(
            @RequestParam String nombre,
            @ParameterObject Pageable pageable){
        log.info("Buscando especialidades con nombre={}, page={} size={}", 
                nombre, pageable.getPageNumber(), pageable.getPageSize());
        return ResponseEntity.ok(especialidadService.buscarPorName(nombre, pageable));
    }

    @Operation(summary = "Listar especialidades por estado (paginadas)")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<Page<SpecialtyResponseDTO>> listarPorStatus(
            @PathVariable String estado,
            @ParameterObject Pageable pageable){
        log.info("Listando especialidades con estado={}, page={} size={}", 
                estado, pageable.getPageNumber(), pageable.getPageSize());
        return ResponseEntity.ok(especialidadService.listarPorStatus(estado, pageable));
    }

    @Operation(summary = "Obtener una especialidad por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<SpecialtyResponseDTO> obtenerEspecialidadPorId(@PathVariable Long id){
        log.info("Buscando especialidad con id={}", id);
        return ResponseEntity.ok(especialidadService.obtenerSpecialtyPorId(id));
    }

    @Operation(summary = "Actualizar una especialidad")
    @PutMapping("/{id}")
    public ResponseEntity<SpecialtyResponseDTO> actualizarEspecialidad(
            @PathVariable Long id,
            @Valid @RequestBody SpecialtyRequestDTO requestDto){
        log.info("Solicitud para actualizar especialidad con id={}: {}", id, requestDto);
        SpecialtyResponseDTO response = especialidadService.actualizarSpecialty(id, requestDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar una especialidad")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEspecialidad(@PathVariable Long id){
        log.info("Solicitud para eliminar especialidad con id={}", id);
        especialidadService.eliminarSpecialty(id);
        return ResponseEntity.noContent().build();
    }

}