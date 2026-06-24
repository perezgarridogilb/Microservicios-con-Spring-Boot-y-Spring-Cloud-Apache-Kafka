package com.example.crudrapido.controller;

import java.util.List;
import java.util.Optional;

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

import com.example.crudrapido.dto.request.EmployeeRequestDTO;
import com.example.crudrapido.dto.response.EmployeeResponseDTO;
import com.example.crudrapido.model.Status;
import com.example.crudrapido.dto.response.EmployeeResponseDTO;
import com.example.crudrapido.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/empleados")
@RequiredArgsConstructor
@Slf4j
public class EmpleadoController {

    private final EmployeeService empleadoService;

    @Operation(summary = "Crear un nuevo empleado")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Empleado Creado"),
            @ApiResponse(responseCode = "400", description = "Request Inválido"),
            @ApiResponse(responseCode = "404", description = "Persona no encontrada")
    })
    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> crearEmpleado(@Valid @RequestBody EmployeeRequestDTO requestDto){
        log.info("Solicitud para crear empleado recibida: {}", requestDto);
        EmployeeResponseDTO response = empleadoService.createEmployee(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar todos los empleados")
    @GetMapping
    public ResponseEntity<List<EmployeeResponseDTO>> listarTodosEmpleados(){
        log.info("Listando todos los empleados");
        List<EmployeeResponseDTO> empleados = empleadoService.listarEmployees();
        return ResponseEntity.ok(empleados);
    }

    @Operation(summary = "Obtener un empleado por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> obtenerEmpleadoPorId(@PathVariable Long id){
        log.info("Buscando empleado con id={}", id);
        Optional<EmployeeResponseDTO> empleado = empleadoService.findById(id);
        return empleado.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar empleados por estado (paginados)")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<Page<EmployeeResponseDTO>> listarPorEstado(
            @PathVariable Status estado,
            @ParameterObject Pageable pageable){
        log.info("Listando empleados con estado={}, page={} size={}", 
                estado, pageable.getPageNumber(), pageable.getPageSize());
        return ResponseEntity.ok(empleadoService.listarPorStatus(estado.name(), pageable));
    }

    @Operation(summary = "Actualizar un empleado")
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> actualizarEmpleado(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequestDTO requestDto){
        log.info("Solicitud para actualizar empleado con id={}: {}", id, requestDto);
        EmployeeResponseDTO response = empleadoService.updateEmployee(id, requestDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar un empleado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEmpleado(@PathVariable Long id){
        log.info("Solicitud para eliminar empleado con id={}", id);
        empleadoService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

}