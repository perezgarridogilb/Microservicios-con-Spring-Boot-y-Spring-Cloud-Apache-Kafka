package com.latecnologiaavanza.service.impl;

import com.latecnologiaavanza.dto.request.AuthLoginRequestDto;
import com.latecnologiaavanza.dto.request.AuthRegisterRequestDto;
import com.latecnologiaavanza.dto.response.AuthResponseDto;
import com.latecnologiaavanza.dto.response.MessageResponseDto;
import com.latecnologiaavanza.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public MessageResponseDto registrar(AuthRegisterRequestDto request) {
        return null;
    }

    @Override
    public AuthResponseDto login(AuthLoginRequestDto request) {
        return null;
    }

}
