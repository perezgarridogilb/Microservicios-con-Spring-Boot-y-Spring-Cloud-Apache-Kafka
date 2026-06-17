package com.example.crudrapido.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRequestDto {

    @NotBlank(message = "El User es obligatorio")
    private String User;

    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasena;

    @NotNull(message = "La persona es obligatoria")
    private Long studentId;

}
