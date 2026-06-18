package com.latecnologiaavanza.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.latecnologiaavanza.dto.request.PacienteRequestDto;
import com.latecnologiaavanza.dto.response.PacienteResponseDto;
import com.latecnologiaavanza.exception.ResourceNotFoundException;
import com.latecnologiaavanza.mapper.PacienteMapper;
import com.latecnologiaavanza.model.Estado;
import com.latecnologiaavanza.model.Paciente;
import com.latecnologiaavanza.model.Persona;
import com.latecnologiaavanza.repository.PacienteRepository;
import com.latecnologiaavanza.repository.PersonaRepository;
import com.latecnologiaavanza.service.PacienteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;
    private final PersonaRepository personaRepository;
    private final PacienteMapper pacienteMapper;

    @Override
    @Transactional
    public PacienteResponseDto crearPaciente(PacienteRequestDto requestDto) {
        Persona persona = personaRepository.findById(requestDto.getPersonaId())
                .orElseThrow(() -> new ResourceNotFoundException("Persona no encontrada con id: " + requestDto.getPersonaId()));

        Paciente paciente = pacienteMapper.toEntity(requestDto);
        paciente.setPersona(persona);
        paciente = pacienteRepository.save(paciente);

        log.info("Paciente creado. id={}", paciente.getId());
        return pacienteMapper.toResponse(paciente);
    }

    @Override
    @Transactional(readOnly = true)
    public PacienteResponseDto obtenerPacientePorId(Long id) {
        log.info("Obteniendo paciente con id={}", id);

        return pacienteRepository.findById(id)
                .map(pacienteMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id: " + id));
    }

    @Override
    @Transactional
    public PacienteResponseDto actualizarPaciente(Long id, PacienteRequestDto requestDto) {
        log.info("Actualizando paciente con id={}", id);

        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id: " + id));

        Persona persona = personaRepository.findById(requestDto.getPersonaId())
                .orElseThrow(() -> new ResourceNotFoundException("Persona no encontrada con id: " + requestDto.getPersonaId()));

        pacienteMapper.updateEntity(paciente, requestDto);
        paciente.setPersona(persona);

        Paciente uppdatePaciente = pacienteRepository.save(paciente);
        return pacienteMapper.toResponse(uppdatePaciente);
    }

    @Override
    @Transactional
    public void eliminarPaciente(Long id) {
        log.info("Eliminando paciente. id={}", id);

        if (!pacienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Paciente no encontrado con id: " + id);
        }
        pacienteRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PacienteResponseDto> listarPacientes(Pageable pageable) {
        log.info("Listando pacientes paginados, page={} size={}", pageable.getPageNumber(), pageable.getPageSize());
        return pacienteRepository.findAll(pageable)
                .map(pacienteMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PacienteResponseDto> listarPacientesPorEstado(Estado estado, Pageable pageable) {
        log.info("Listando pacientes por estado={} paginados", estado);
        return pacienteRepository.findByEstado(estado, pageable)
                .map(pacienteMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PacienteResponseDto> listarActivos() {
        log.info("Listando todos los pacientes activos");
        return pacienteRepository.findByEstado(Estado.ACTIVO)
                .stream()
                .map(pacienteMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Long obtenerIdPacientePorUsername(String username) {
        log.info("Obteniendo ID del paciente para el usuario: {}", username);
        
        Paciente paciente = pacienteRepository.findByUsuarioUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado para el usuario: " + username));
        
        return paciente.getId();
    }

}
