package com.example.crudrapido.dto.response;

import lombok.Data;

@Data
public class UserResponseDTO {

    private Long id;
    private String usuario;
    private StudentResponseDTO student;
}
