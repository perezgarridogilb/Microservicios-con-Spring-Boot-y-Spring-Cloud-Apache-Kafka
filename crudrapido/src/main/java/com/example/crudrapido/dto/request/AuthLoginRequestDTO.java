package com.example.crudrapido.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthLoginRequestDTO {

    @NotBlank(message = "El user es obligatorio")
    private String user;

    @NotBlank(message = "La password es obligatoria")
    private String password;
}
