package com.example.crudrapido.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.crudrapido.dto.request.PatientRequestDTO;
import com.example.crudrapido.dto.response.PatientResponseDTO;
import com.example.crudrapido.exception.ResourceNotFoundException;
import com.example.crudrapido.mapper.PatientMapper;
import com.example.crudrapido.model.Patient;
import com.example.crudrapido.model.Status;
import com.example.crudrapido.model.Student;
import com.example.crudrapido.repository.PatientRepository;
import com.example.crudrapido.repository.StudentRepository;
import com.example.crudrapido.service.PatientService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class PatientServiceImpl implements PatientService {

    private final PatientRepository pacienteRepository;
    private final StudentRepository personaRepository;
    private final PatientMapper pacienteMapper;

    @Override
    @Transactional
    public PatientResponseDTO crearPatient(PatientRequestDTO requestDto) {
        Student persona = personaRepository.findById(requestDto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Persona no encontrada con id: " + requestDto.getStudentId()));

        Patient paciente = pacienteMapper.toEntity(requestDto);
        paciente.setStudent(persona);
        paciente = pacienteRepository.save(paciente);

        log.info("Paciente creado. id={}", paciente.getId());
        return pacienteMapper.toResponse(paciente);
    }

    @Override
    @Transactional(readOnly = true)
    public PatientResponseDTO obtenerPatientPorId(Long id) {
        log.info("Obteniendo paciente con id={}", id);

        return pacienteRepository.findById(id)
                .map(pacienteMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id: " + id));
    }

    @Override
    @Transactional
    public PatientResponseDTO actualizarPatient(Long id, PatientRequestDTO requestDto) {
        log.info("Actualizando paciente con id={}", id);

        Patient paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id: " + id));

        Student persona = personaRepository.findById(requestDto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Persona no encontrada con id: " + requestDto.getStudentId()));

        pacienteMapper.updateEntity(paciente, requestDto);
        paciente.setStudent(persona);

        Patient uppdatePaciente = pacienteRepository.save(paciente);
        return pacienteMapper.toResponse(uppdatePaciente);
    }

    @Override
    @Transactional
    public void eliminarPatient(Long id) {
        log.info("Eliminando paciente. id={}", id);

        if (!pacienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Paciente no encontrado con id: " + id);
        }
        pacienteRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PatientResponseDTO> listarPatients(Pageable pageable) {
        log.info("Listando pacientes paginados, page={} size={}", pageable.getPageNumber(), pageable.getPageSize());
        return pacienteRepository.findAll(pageable)
                .map(pacienteMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PatientResponseDTO> listarPatientsPorStatus(Status estado, Pageable pageable) {
        log.info("Listando pacientes por estado={} paginados", estado);
        return pacienteRepository.findByStatus(estado, pageable)
                .map(pacienteMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientResponseDTO> listarActivos() {
        log.info("Listando todos los pacientes activos");
        return pacienteRepository.findByStatus(Status.ACTIVO)
                .stream()
                .map(pacienteMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Long obtenerIdPatientPorUsername(String username) {
        log.info("Obteniendo ID del paciente para el usuario: {}", username);
        
        Patient paciente = pacienteRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado para el usuario: " + username));
        
        return paciente.getId();
    }

}