package com.example.crudrapido.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SpecialtyRequestDTO {

    @NotBlank(message = "El nombre es requerido")
    private String name;
}
