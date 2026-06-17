package com.example.crudrapido.validation;

import org.springframework.stereotype.Component;

import com.example.crudrapido.repository.StudentRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    private final StudentRepository studentRepository;

    public UniqueEmailValidator(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return !studentRepository.existsByEmail(email);
    }
}
