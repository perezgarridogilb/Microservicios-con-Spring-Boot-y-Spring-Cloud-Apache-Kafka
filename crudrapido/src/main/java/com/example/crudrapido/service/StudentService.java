package com.example.crudrapido.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.crudrapido.model.Student;
import com.example.crudrapido.repository.StudentRepository;

import jakarta.validation.ValidationException;

@Service
public class StudentService {
    @Autowired
    StudentRepository studentRepository;

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudent(Long id) {
        return studentRepository.findById(id);
    }

    public Student saveOrUpdate(Student student) {
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new ValidationException("El email " + student.getEmail() + " ya existe");
        }
        return studentRepository.save(student);
    }

    public void delete(Long id) {
        studentRepository.deleteById(id);
    }
}
