package com.example.crudrapido.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.example.crudrapido.model.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class Atention {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La fecha de atención es requerida")
    private LocalDateTime date;

    @NotBlank(message = "El motivo de la atención es requerido")
    private String atention;

    @NotNull(message = "El estado de la atención es requerido")
    @Enumerated(EnumType.STRING)
    private Status status;


    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false,
    foreignKey = @ForeignKey(name = "FK_atention_employee"))
    private Employee employee;

    // @ManyToOne === belongsTo(Student::class)
    // @JoinColumn(name = "student_id") === $table->foreignId('student_id')->constrained()
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false,
    foreignKey = @ForeignKey(name = "FK_atention_patient"))
    private Patient patient;
}
