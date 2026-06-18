package com.example.crudrapido.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.crudrapido.dto.request.PatientRequestDTO;
import com.example.crudrapido.dto.response.PatientResponseDTO;
import com.example.crudrapido.model.Status;


public interface PatientService {

    PatientResponseDTO crearPatient(PatientRequestDTO requestDto);

    PatientResponseDTO obtenerPatientPorId(Long id);

    PatientResponseDTO actualizarPatient(Long id, PatientRequestDTO requestDto);

    void eliminarPatient(Long id);

    Page<PatientResponseDTO> listarPatients(Pageable pageable);

    Page<PatientResponseDTO> listarPatientsPorStatus(Status status, Pageable pageable);

    List<PatientResponseDTO> listarActivos();

    Long obtenerIdPatientPorUsername(String username);

}
