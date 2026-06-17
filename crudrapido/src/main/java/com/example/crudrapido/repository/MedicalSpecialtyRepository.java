package com.example.crudrapido.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.crudrapido.model.Employee;
import com.example.crudrapido.model.MedicalSpecialty;
import com.example.crudrapido.model.Specialty;

@Repository
public interface MedicalSpecialtyRepository extends JpaRepository<MedicalSpecialty, Long> {
    Page<MedicalSpecialty> findByEmployee(Employee employee, Pageable pageable);
    Page<MedicalSpecialty> findBySpecialty(Specialty specialty, Pageable pageable);

}
