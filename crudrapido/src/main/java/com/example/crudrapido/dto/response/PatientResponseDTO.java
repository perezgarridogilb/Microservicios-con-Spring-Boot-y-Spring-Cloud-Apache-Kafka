package com.example.crudrapido.dto.response;

import com.example.crudrapido.model.Rol;
import com.example.crudrapido.model.Status;

import lombok.Data;

@Data
public class PatientResponseDTO {
    private Long id;
    private Rol rol;
    private Status status;
    private StudentResponseDTO student;
}
