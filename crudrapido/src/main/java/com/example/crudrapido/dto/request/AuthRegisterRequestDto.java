package com.example.crudrapido.dto.request;

import com.example.crudrapido.model.Rol;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthRegisterRequestDto {

    @NotBlank(message = "El usuario es obligatorio")
    private String user;

    @NotBlank(message = "La clave es obligatoria")
    private String password;

    @NotNull(message = "El rol es obligatorio")
    private Rol rol;

    @NotNull(message = "El estudiante es obligatoria")
    private Long studentId;
}
