package com.example.crudrapido.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MedicalSpecialtyRequestDTO {

    @NotNull(message = "El empleado es requerido")
    private Long employeeId;

    @NotNull(message = "La especialidad es requerida")
    private Long specialtyId;
}
