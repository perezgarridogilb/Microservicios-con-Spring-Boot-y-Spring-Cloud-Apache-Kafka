package com.example.crudrapido.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "medical_specialty")
public class MedicalSpecialty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false,
        foreignKey = @ForeignKey(name = "FK_medical_specialty_employee"))
    private Employee employee; // 
    /**
     * para mappedBy que asu vez es employee_id
     * Este nombre de variable ("employee") es el que usa mappedBy = "employee" en la otra clase.
       A su vez, Hibernate usa esta configuración para crear físicamente la columna "employee_id"
    */
    @ManyToOne
    @JoinColumn(name = "specialty_id", nullable = false,
        foreignKey = @ForeignKey(name = "FK_medical_specialty_specialty"))
    private Specialty specialty;
}
