package com.example.crudrapido.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

// @Target === donde se puede usar esta anotación
// FIELD === solo en propiedades de clase (como 'use Illuminate\Validation\Rule' en Request)
@Target(ElementType.FIELD)
// @Retention(RUNTIME) === la anotación vive en runtime para que el validador la lea
@Retention(RetentionPolicy.RUNTIME)
// @Constraint === esto es una regla de validación personalizada
// validatedBy === el validador que ejecuta la lógica (como un Rule personalizado de Laravel)
@Constraint(validatedBy = UniqueEmailValidator.class)
public @interface UniqueEmail {
    // message === mensaje de error cuando falla (como 'email' => 'unique:users' -> mensaje por defecto)
    String message() default "El email ya existe";
    // groups === permite agrupar validaciones (como 'sometimes|required' en diferentes escenarios)
    Class<?>[] groups() default {};
    // payload === metadatos extra para el validador (no hay equivalente directo en Laravel)
    Class<? extends Payload>[] payload() default {};
}
