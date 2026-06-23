package com.example.crudrapido.dto.request;

import com.example.crudrapido.model.Rol;
import com.example.crudrapido.model.Status;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmployeeRequestDTO {

    @NotNull(message = "La persona es obligatoria")
    private Long studentId;

    @NotNull(message = "El rol es obligatorio")
    private Rol rol;

    @NotNull(message = "El estado es obligatorio")
    private Status status;

}
