package com.example.crudrapido.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.crudrapido.model.Employee;
import com.example.crudrapido.model.Status;
import com.example.crudrapido.model.Student;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

        Page<Employee> findByStatus(Status status, Pageable pageable);


    Optional<Employee> findByStudent(Student student);
}
