package com.example.crudrapido.dto.request;

import com.example.crudrapido.model.Status;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SpecialtyRequestDTO {

    @NotBlank(message = "El nombre es requerido")
    private String name;

@NotNull(message = "El status no puede ser nulo")
    private Status status;
}
