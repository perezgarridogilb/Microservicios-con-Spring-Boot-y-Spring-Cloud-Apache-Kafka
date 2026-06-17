package com.example.crudrapido.dto.response;

import com.example.crudrapido.model.Status;

import com.example.crudrapido.model.Rol;

public class EmployeeResponseDTO {
    private Long id;
    private Rol rol;
    private Status status;
    private StudentResponseDTO persona;
}
