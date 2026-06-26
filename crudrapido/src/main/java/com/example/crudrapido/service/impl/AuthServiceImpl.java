package com.example.crudrapido.service.impl;

import com.example.crudrapido.security.JwtUtil;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.crudrapido.dto.request.AuthLoginRequestDTO;
import com.example.crudrapido.dto.request.AuthRegisterRequestDto;
import com.example.crudrapido.dto.response.AuthResponseDTO;
import com.example.crudrapido.dto.response.MessageResponseDTO;
import com.example.crudrapido.exception.ResourceNotFoundException;
import com.example.crudrapido.model.Employee;
import com.example.crudrapido.model.Patient;
import com.example.crudrapido.model.Rol;
import com.example.crudrapido.model.Student;
import com.example.crudrapido.model.User;
import com.example.crudrapido.repository.EmployeeRepository;
import com.example.crudrapido.repository.PatientRepository;
import com.example.crudrapido.repository.StudentRepository;
import com.example.crudrapido.repository.UserRepository;
import com.example.crudrapido.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PatientRepository patientRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public MessageResponseDTO registrar(AuthRegisterRequestDto request) {
        if (userRepository.existsByUser(request.getUser())) {
            throw new IllegalArgumentException("El usuario ya existe: " + request.getUser());
        }

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Estudiante no encontrado con id: " + request.getStudentId()));

        User user = User.builder()
                .user(request.getUser())
                .password(passwordEncoder.encode(request.getPassword()))
                .student(student)
                .build();

        userRepository.save(user);
        log.info("Usuario registrado exitosamente: {}", request.getUser());
        return new MessageResponseDTO("Usuario registrado exitosamente: " + request.getUser());
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponseDTO login(AuthLoginRequestDTO request) {
        User user = userRepository.findByUser(request.getUser())
                .orElseThrow(() -> new ResourceNotFoundException("User no encontrado: " + request.getUser()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Usuario o contraseña incorrectos");
        }

        Student student = user.getStudent();

        Rol rol;
        if (patientRepository.existsByStudent(student)) {
            rol = Rol.PACIENTE;
        } else {
            Employee employee = employeeRepository.findByStudent(student)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Empleado no encontrado"));
            rol = employee.getRol();
        }

        log.info("Login exitoso para usuario: {}", request.getUser());
        String token = jwtUtil.generateToken(request.getUser(), rol);

        return new AuthResponseDTO(token, "Bearer");
    }

}
