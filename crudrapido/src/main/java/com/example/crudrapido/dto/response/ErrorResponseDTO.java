package com.example.crudrapido.dto.response;

import com.example.crudrapido.model.Student;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponseDTO {
    private String mensaje;
    // private Long id;
    // private Student student;
}
