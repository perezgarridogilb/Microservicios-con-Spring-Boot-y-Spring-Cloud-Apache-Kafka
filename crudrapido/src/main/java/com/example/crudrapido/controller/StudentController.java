package com.example.crudrapido.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.crudrapido.dto.request.StudentRequestDTO;
import com.example.crudrapido.model.Student;
import com.example.crudrapido.service.StudentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "api/v1/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> getAll(){
        return studentService.getStudents();
    }
@GetMapping("/{id}")
    public Optional<Student> getById(@PathVariable("id") Long id){
        return studentService.getStudent(id);
    }

    @PostMapping
    public ResponseEntity<?> saveUpdate(@Valid @RequestBody StudentRequestDTO request, BindingResult result){
        if (result.hasFieldErrors()) {
            return validation(result);
        }
        return ResponseEntity.ok(studentService.saveOrUpdate(request));
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err ->
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id ) {
        studentService.delete(id);
    }
}
