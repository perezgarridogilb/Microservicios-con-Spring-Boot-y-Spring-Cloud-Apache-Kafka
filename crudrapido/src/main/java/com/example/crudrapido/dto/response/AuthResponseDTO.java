package com.example.crudrapido.dto.response;

import java.io.ObjectInputFilter.Status;
import java.time.LocalDateTime;

import com.example.crudrapido.model.Student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    private String mensaje;
    private String token;
    private String tipoToken;
    // private EmpleadoResponseDto empleado;

    public AuthResponseDTO(String mensaje) {
        this.mensaje = mensaje;
    }

    public AuthResponseDTO( String token, String tipoToken) {
        this.mensaje = "Login exitoso";
        this.token = token;
        this.tipoToken = tipoToken;
    }

    
}
