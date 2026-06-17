package com.example.crudrapido.dto.request;

import com.example.crudrapido.model.Status;

import com.example.crudrapido.validation.UniqueEmail;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

// @Data === getters, setters, equals, hashCode, toString automáticos
// Equivalente a definir manualmente: getFirstName(), setFirstName(), getLastName()...
// así no escribimos boilerplate
@Data
public class StudentRequestDTO {
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @UniqueEmail
    private String email;

    @NotNull(message = "El estado es requerido")
    @Enumerated(EnumType.STRING)
    private Status status;

}
