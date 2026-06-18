package com.example.crudrapido.dto.request;

import java.time.LocalDateTime;
import com.example.crudrapido.model.Status;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AtentionRequestDto {
    @NotNull(message = "La fecha de atención es requerida")
    private LocalDateTime fecha;

    @NotBlank(message = "El motivo de la atención es requerido")
    private String atention;

@NotNull
private Long patientId;

    @NotNull(message = "El empleado es obligatorio")
    private Long employeeId;

    @NotNull(message = "El estado de la atención es requerido")
    private Status status;
}
