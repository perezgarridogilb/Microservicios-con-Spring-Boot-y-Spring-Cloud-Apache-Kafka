package com.example.crudrapido.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.crudrapido.dto.request.SpecialtyRequestDTO;
import com.example.crudrapido.dto.response.SpecialtyResponseDTO;

public interface SpecialtyService {

    SpecialtyResponseDTO crearSpecialty(SpecialtyRequestDTO requestDto);

    SpecialtyResponseDTO actualizarSpecialty(Long id, SpecialtyRequestDTO requestDto);

    void eliminarSpecialty(Long id);

    SpecialtyResponseDTO obtenerSpecialtyPorId(Long id);

    Page<SpecialtyResponseDTO> listarSpecialty(Pageable pageable);

    Page<SpecialtyResponseDTO> buscarPorName(String name, Pageable pageable);

    Page<SpecialtyResponseDTO> listarPorStatus(String staatus, Pageable pageable);

}
