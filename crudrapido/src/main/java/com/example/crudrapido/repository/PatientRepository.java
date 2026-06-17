package com.example.crudrapido.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.crudrapido.model.Patient;
import com.example.crudrapido.model.Status;
import com.example.crudrapido.model.Student;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByStatus(Status Status); 

Page<Patient> findByStatus(Status Status, Pageable pageable);

boolean existsByStudent(Student student);

@Query("""
    SELECT p FROM Patient p 
    JOIN User u ON u.student = p.student 
    WHERE u.user = :username
""")
Optional<Patient> findByUserUsername(@Param("username") String username);
}
