package com.example.crudrapido.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.crudrapido.dto.request.AtentionRequestDto;
import com.example.crudrapido.dto.response.AtentionResponseDTO;
import com.example.crudrapido.model.Status;

public interface AtentionService {

    AtentionResponseDTO crearAtention(AtentionRequestDto requestDto);

    AtentionResponseDTO obtenerAtentionPorId(Long id);

    Page<AtentionResponseDTO> listarTodas(Pageable pageable);

    Page<AtentionResponseDTO> listarPorEmployee(Long empleadoId, Pageable pageable);

    Page<AtentionResponseDTO> listarPorPatient(Long patientId, Pageable pageable);

    Page<AtentionResponseDTO> listarPorStatus(Status status, Pageable pageable);

    Page<AtentionResponseDTO> listarPorRangoFechas(LocalDateTime fechaInicio
        , LocalDateTime fechaFin, Pageable pageable);

    Page<AtentionResponseDTO> buscarPorMotivo(String motivo, Pageable pageable);

    AtentionResponseDTO actualizarAtention(Long id, AtentionRequestDto requestDto);

    void eliminarAtention(Long id);

    Page<AtentionResponseDTO> listarAtentionsDelPacienteAutenticado(String username, Pageable pageable);
}
