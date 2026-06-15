package com.example.crudrapido.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Student saveUpdate(@Valid @RequestBody Student student){ // @Valid en Spring = $request->validate()
        return studentService.saveOrUpdate(student);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id ) {
        studentService.delete(id);
    }
}
