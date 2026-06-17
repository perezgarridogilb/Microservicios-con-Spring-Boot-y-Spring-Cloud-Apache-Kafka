package com.example.crudrapido.dto.response;

import com.example.crudrapido.model.Status;

import com.example.crudrapido.model.Rol;

public class EmpleadoResponseDto {
    private Long id;
    private Rol rol;
    private StudentResponseDTO student;
    private Status status;
}
