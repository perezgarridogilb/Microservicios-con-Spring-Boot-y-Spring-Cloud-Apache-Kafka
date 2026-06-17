package com.example.crudrapido.dto.request;

import com.example.crudrapido.model.Rol;
import com.example.crudrapido.model.Status;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PatientRequestDTO {

    @NotNull(message = "Student is required")
    private Long studentId;

    @NotNull(message = "Role is required")
    private Rol rol;

    @NotNull(message = "Status is required")
    private Status status;
}
