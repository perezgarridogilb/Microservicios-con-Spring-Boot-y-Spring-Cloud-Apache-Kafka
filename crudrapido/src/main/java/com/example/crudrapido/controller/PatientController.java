package com.example.crudrapido.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import com.example.crudrapido.dto.request.PatientRequestDTO;
import com.example.crudrapido.dto.response.PatientResponseDTO;
import com.example.crudrapido.model.Status;
import com.example.crudrapido.service.PatientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
@Slf4j
public class PatientController {

    private final PatientService pacienteService;

    @Operation(summary = "Crear un nuevo paciente")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Paciente Creado"),
            @ApiResponse(responseCode = "400", description = "Request Inválido"),
            @ApiResponse(responseCode = "404", description = "Persona no encontrada")
    })
    @PostMapping
    public ResponseEntity<PatientResponseDTO> crearPaciente(@Valid @RequestBody PatientRequestDTO requestDto){
        log.info("Solicitud para crear paciente recibida: {}", requestDto);
        PatientResponseDTO response = pacienteService.crearPatient(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar todos los pacientes (paginados)")
    @GetMapping
    public ResponseEntity<Page<PatientResponseDTO>> listarPacientes(@ParameterObject Pageable pageable){
        log.info("Listando todos los pacientes, page={} size={}", pageable.getPageNumber(), pageable.getPageSize());
        return ResponseEntity.ok(pacienteService.listarPatients(pageable));
    }

    @Operation(summary = "Obtener un paciente por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> obtenerPacientePorId(@PathVariable Long id){
        log.info("Buscando paciente con id={}", id);
        return ResponseEntity.ok(pacienteService.obtenerPatientPorId(id));
    }

    @Operation(summary = "Listar pacientes activos")
    @GetMapping("/activos")
    public ResponseEntity<List<PatientResponseDTO>> listarActivos(){
        log.info("Listando pacientes activos");
        List<PatientResponseDTO> pacientes = pacienteService.listarActivos();
        return ResponseEntity.ok(pacientes);
    }

    @Operation(summary = "Listar pacientes por estado (paginados)")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<Page<PatientResponseDTO>> listarPacientesPorEstado(
            @PathVariable Status estado,
            @ParameterObject Pageable pageable){
        log.info("Listando pacientes con estado={}, page={} size={}", 
                estado, pageable.getPageNumber(), pageable.getPageSize());
        return ResponseEntity.ok(pacienteService.listarPatientsPorStatus(estado, pageable));
    }

    @Operation(summary = "Actualizar un paciente")
    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> actualizarPaciente(
            @PathVariable Long id,
            @Valid @RequestBody PatientRequestDTO requestDto){
        log.info("Solicitud para actualizar paciente con id={}: {}", id, requestDto);
        PatientResponseDTO response = pacienteService.actualizarPatient(id, requestDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar un paciente")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPaciente(@PathVariable Long id){
        log.info("Solicitud para eliminar paciente con id={}", id);
        pacienteService.eliminarPatient(id);
        return ResponseEntity.noContent().build();
    }

}