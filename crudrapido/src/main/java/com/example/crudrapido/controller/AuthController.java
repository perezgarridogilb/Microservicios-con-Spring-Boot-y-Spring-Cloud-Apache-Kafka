package com.example.crudrapido.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.crudrapido.dto.request.AuthLoginRequestDTO;
import com.example.crudrapido.dto.request.AuthRegisterRequestDto;
import com.example.crudrapido.dto.response.AuthResponseDTO;
import com.example.crudrapido.dto.response.MessageResponseDTO;
import com.example.crudrapido.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Registrar un nuevo usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Request Inválido")
    })
    @PostMapping("/registrar")
    public ResponseEntity<MessageResponseDTO> registrar(@Valid @RequestBody AuthRegisterRequestDto requestDto){
        log.info("Solicitud para registrar nuevo usuario recibida: {}", requestDto.getUser());
        try {
            MessageResponseDTO response = authService.registrar(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch (IllegalArgumentException ex){
            log.warn("Error al registrar usuario: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponseDTO(ex.getMessage()));
        }
    }

    @Operation(summary = "Iniciar sesión")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sesión iniciada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Request Inválido"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthLoginRequestDTO requestDto){
        log.info("Solicitud para iniciar sesión recibida para usuario: {}", requestDto.getUser());

        try {
            AuthResponseDTO response = authService.login(requestDto);
            return ResponseEntity.ok(response);
        }catch (IllegalArgumentException ex){
            log.warn("Error en el login: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponseDTO(ex.getMessage()));
        }
    }

}