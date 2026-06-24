package com.example.crudrapido.service;

import com.example.crudrapido.dto.request.AuthLoginRequestDTO;
import com.example.crudrapido.dto.request.AuthRegisterRequestDto;
import com.example.crudrapido.dto.response.AuthResponseDTO;
import com.example.crudrapido.dto.response.MessageResponseDTO;

public interface AuthService {
    MessageResponseDTO registrar(AuthRegisterRequestDto request);
    AuthResponseDTO login(AuthLoginRequestDTO request);
}
