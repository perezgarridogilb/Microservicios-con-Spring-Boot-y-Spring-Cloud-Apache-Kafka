package com.example.crudrapido.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.crudrapido.dto.request.SpecialtyRequestDTO;
import com.example.crudrapido.dto.response.SpecialtyResponseDTO;
import com.example.crudrapido.exception.ResourceNotFoundException;
import com.example.crudrapido.mapper.SpecialtyMapper;
import com.example.crudrapido.model.Specialty;
import com.example.crudrapido.model.Status;
import com.example.crudrapido.repository.SpecialtyRepository;
import com.example.crudrapido.service.SpecialtyService;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class SpecialtyServiceImpl implements SpecialtyService {

    private final SpecialtyRepository specialtyRepository;
    private final SpecialtyMapper specialtyMapper;

    @Override
    @Transactional
    public SpecialtyResponseDTO crearSpecialty(SpecialtyRequestDTO requestDto) {
        Specialty especialidad = specialtyMapper.toEntity(requestDto);
        especialidad = specialtyRepository.save(especialidad);

        log.info("Especialidad creada. id={}", especialidad.getId());
        return specialtyMapper.toResponse(especialidad);
    }

    @Override
    @Transactional
    public SpecialtyResponseDTO actualizarSpecialty(Long id, SpecialtyRequestDTO requestDto) {
        Specialty especialidad = specialtyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Especialidad no encontrada con id: " + id));

        specialtyMapper.updateEntity(especialidad, requestDto);
        Specialty updatEspecialidad = specialtyRepository.save(especialidad);

        log.info("Especialidad actualizada. id={}", especialidad.getId());
        return specialtyMapper.toResponse(updatEspecialidad);
    }

    @Override
    @Transactional
    public void eliminarSpecialty(Long id) {
        Specialty especialidad = specialtyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Especialidad no encontrada con id: " + id));

        specialtyRepository.delete(especialidad);

        log.info("Especialidad eliminada. id={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public SpecialtyResponseDTO obtenerSpecialtyPorId(Long id) {
        return specialtyRepository.findById(id)
                .map(specialtyMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Especialidad no encontrada con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SpecialtyResponseDTO> listarSpecialty(Pageable pageable) {
        return specialtyRepository.findAll(pageable)
                .map(specialtyMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SpecialtyResponseDTO> buscarPorName(String nombre, Pageable pageable) {
        return specialtyRepository.searchByName(nombre, pageable)
                .map(specialtyMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SpecialtyResponseDTO> listarPorStatus(String estadoStr, Pageable pageable) {
        Status status;
        try {
            status = Status.valueOf(estadoStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado inválido: " + estadoStr);
        }
        return specialtyRepository.findByEstado(status, pageable).map(specialtyMapper::toResponse);
    }

}
