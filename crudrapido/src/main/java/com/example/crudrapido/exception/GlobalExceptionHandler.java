package com.example.crudrapido.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

/**
     * Mapeo para errores 404 (ModelNotFoundException / ResourceNotFound)
     * * EQUIVALENCIA DE ESTRUCTURAS:
     * - ResponseEntity<>             ->  Illuminate\Http\JsonResponse
     * - Map<String, Object>          ->  array (Asociativo / Mixto)
     * - String (Clave del Map)       ->  string (Llaves del array)
     * - Object (Valor del Map)       ->  mixed (Cualquier tipo de dato)
     * * Laravel equivalente:
     * return response()->json(array $body, int $status);
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String,Object>> handleResourceNotFoundException(ResourceNotFoundException ex){
        log.error("Error: {}", ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Mapeo para excepciones de negocio (BusinessException)
     * * EQUIVALENCIA DE ESTRUCTURAS:
     * - ResponseEntity<Map<String, Object>> es el contenedor estricto para asegurar
     * que el Cliente reciba un objeto JSON estructurado {'llave': valor} y no un texto plano.
     * * Laravel equivalente:
     * En PHP no se envuelve el array en un "Map" genérico; se pasa directamente 
     * el array asociativo al wrapper 'JsonResponse' mediante el helper 'response()->json()'.
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String,Object>> handleBusinessException(BusinessException ex){
        log.error("Error: {}", ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<Map<String,Object>> handleInvalidRequest(InvalidRequestException ex){
        log.error("Petición inválida: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleValidationErrors(MethodArgumentNotValidException ex){
        log.error("Errores de validación: {}", ex.getMessage());

        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                        .forEach(error -> validationErrors.put(error.getField(), error.getDefaultMessage()));

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status",HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation Error");
        body.put("message", validationErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> handleGeneralException(Exception ex){
        log.error("Error inesperado: ", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error interno. Contacte con soporte.");
    }

    /**
     * Helper constructor de la Respuesta Estructurada
     * * COMPARATIVA DE ESTRUCTURAS INTERNAS:
     * * 1. INSTANCIACIÓN:
     * - Java: Map<String, Object> body = new HashMap<>();
     * - Laravel: $body = []; // O array()
     * * 2. ASIGNACIÓN (Clave/Valor):
     * - Java: body.put("timestamp", LocalDateTime.now());
     * - Laravel: $body['timestamp'] = now();
     * * 3. RETORNO DE ENVOLTORIO (Wrapper):
     * - Java: ResponseEntity.status(status).body(body);
     * - Laravel: response()->json($body, $status);
     */
    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        // Estructura: Map de Java -> Equivalente a un Array Asociativo en PHP
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        
        // Estructura: ResponseEntity de Java -> Equivalente a JsonResponse en Laravel
        return ResponseEntity.status(status).body(body);
    }

}
