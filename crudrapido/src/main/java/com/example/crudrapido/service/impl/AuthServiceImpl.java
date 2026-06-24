package com.example.crudrapido.service.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.crudrapido.dto.request.AuthLoginRequestDTO;
import com.example.crudrapido.dto.request.AuthRegisterRequestDto;
import com.example.crudrapido.dto.response.AuthResponseDTO;
import com.example.crudrapido.dto.response.MessageResponseDTO;
import com.example.crudrapido.exception.ResourceNotFoundException;
import com.example.crudrapido.model.Student;
import com.example.crudrapido.model.User;
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

    @Override
    @Transactional
    public MessageResponseDTO registrar(AuthRegisterRequestDto request) {
        if (userRepository.existsByUser(request.getUser())) {
            throw new IllegalArgumentException("El usuario ya existe: " + request.getUser());
        }

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id: " + request.getStudentId()));

        User user = User.builder()
                .user(request.getUser())
                .password(hashPassword(request.getPassword()))
                .student(student)
                .build();

        userRepository.save(user);
        log.info("Usuario registrado exitosamente: {}", request.getUser());
        return new MessageResponseDTO("Usuario registrado exitosamente");
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponseDTO login(AuthLoginRequestDTO request) {
        User user = userRepository.findByUser(request.getUser())
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));

        if (!verifyPassword(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        String token = generateToken(user);
        log.info("Login exitoso para usuario: {}", request.getUser());
        return new AuthResponseDTO(token, "Bearer");
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al hashear password", e);
        }
    }

    private boolean verifyPassword(String rawPassword, String hashedPassword) {
        return hashPassword(rawPassword).equals(hashedPassword);
    }

    private String generateToken(User user) {
        String raw = user.getId() + ":" + user.getUser() + ":" + System.currentTimeMillis();
        return Base64.getEncoder().encodeToString(raw.getBytes());
    }
}
