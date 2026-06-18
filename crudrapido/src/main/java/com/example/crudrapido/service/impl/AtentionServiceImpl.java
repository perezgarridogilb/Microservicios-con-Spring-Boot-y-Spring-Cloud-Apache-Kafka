package com.example.crudrapido.service.impl;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.crudrapido.dto.request.AtentionRequestDto;
import com.example.crudrapido.dto.response.AtentionResponseDTO;
import com.example.crudrapido.exception.BusinessException;
import com.example.crudrapido.exception.InvalidRequestException;
import com.example.crudrapido.exception.ResourceNotFoundException;
import com.example.crudrapido.mapper.AtentionMapper;
import com.example.crudrapido.model.Atention;
import com.example.crudrapido.model.Employee;
import com.example.crudrapido.model.Patient;
import com.example.crudrapido.model.Status;
import com.example.crudrapido.repository.AtentionRepository;
import com.example.crudrapido.repository.EmployeeRepository;
import com.example.crudrapido.repository.PatientRepository;
import com.example.crudrapido.service.AtentionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class AtentionServiceImpl implements AtentionService {

    private final AtentionRepository atentionRepository;
    private final PatientRepository patientRepository;
    private final EmployeeRepository employeeRepository;
    private final AtentionMapper atentionMapper;

    @Override
    @Transactional
    public AtentionResponseDTO crearAtention(AtentionRequestDto requestDto) {
        if (requestDto.getFecha() == null) {
            throw new InvalidRequestException("La fecha de la atención es obligatoria");
        }

        if (requestDto.getFecha().isBefore(LocalDateTime.now().minusMinutes(1))) {
            throw new InvalidRequestException("La fecha de la atención no puede ser en el pasado");
        }

        Patient patient = patientRepository.findById(requestDto.getPatientId())
            .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id: " + requestDto.getPatientId()));

        Employee employee = employeeRepository.findById(requestDto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee no encontrado con id: " + requestDto.getEmployeeId()));
        Atention atention = atentionMapper.toEntity(requestDto);

        atention.setPatient(patient);
        atention.setEmployee(employee);

        atention = atentionRepository.save(atention);

        log.info("Atención creada id={}", atention.getId());

        return atentionMapper.toResponse(atention);
    }

    @Override
    @Transactional(readOnly = true)
    public AtentionResponseDTO obtenerAtentionPorId(Long id) {
        return atentionRepository.findById(id)
            .map(atentionMapper::toResponse)
            .orElseThrow(() -> new ResourceNotFoundException("Atención no encontrada con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AtentionResponseDTO> listarTodas(Pageable pageable) {
        return atentionRepository.findAll(pageable).map(atentionMapper::toResponse);
    }

    @Override
    public Page<AtentionResponseDTO> listarPorEmployee(Long empleadoId, Pageable pageable) {
        throw new UnsupportedOperationException("Unimplemented method 'listarPorEmployee'");
    }

    @Override
    public Page<AtentionResponseDTO> listarPorPatient(Long patientId, Pageable pageable) {
                Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient no encontrado con id: " + patientId));

        return atentionRepository.findByPatient(patient,pageable)
                .map(atentionMapper::toResponse);
    }

    @Override
    public Page<AtentionResponseDTO> listarPorStatus(Status status, Pageable pageable) {
                return atentionRepository.findByStatus(status,pageable)
                .map(atentionMapper::toResponse);
    }

    @Override
    public Page<AtentionResponseDTO> listarPorRangoFechas(LocalDateTime fechaInicio ,
LocalDateTime fechaFin, Pageable pageable) {
        if(fechaInicio == null || fechaFin == null){
            throw new InvalidRequestException("Debe proporcionar fecha de inicio y fin para el filtro.");
        }

        if(fechaFin.isBefore(fechaInicio)){
            throw new InvalidRequestException("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }

        return atentionRepository.findByDateBetween(fechaInicio,fechaFin,pageable)
                .map(atentionMapper::toResponse);
    }

    @Override
    public Page<AtentionResponseDTO> buscarPorMotivo(String motivo, Pageable pageable) {
        if(motivo == null || motivo.trim().isEmpty()){
            throw new InvalidRequestException("El motivo para búsqueda no puede estar vacío.");
        }

        return atentionRepository.searchByAtention(motivo,pageable)
                .map(atentionMapper::toResponse);    
            }

    @Override
    @Transactional
    public AtentionResponseDTO actualizarAtention(Long id, AtentionRequestDto requestDto) {
              Atention atention = atentionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Atención no encontrada con id: " + id));

        if(atention.getStatus() == Status.FINALIZADO){
            throw new BusinessException("No se puede actualizar una atención que ya está finalizada.");
        }

        if(requestDto.getFecha() == null){
            throw new InvalidRequestException("La fecha de la atención es obligatoria");
        }

        atention.setDate(requestDto.getFecha());
        atention.setAtention(requestDto.getAtention());
        atention.setStatus(requestDto.getStatus());

        if(requestDto.getPatientId() != null && !requestDto.getEmployeeId().equals(atention.getPatient().getId())){
            atention.setPatient(patientRepository.findById(requestDto.getPatientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id: " + requestDto.getPatientId())));
        }

        if(requestDto.getEmployeeId() != null && requestDto.getEmployeeId().equals(atention.getEmployee().getId())){
            atention.setEmployee(employeeRepository.findById(requestDto.getEmployeeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id: " + requestDto.getEmployeeId())));
        }
        atention = atentionRepository.save(atention);

        log.info("Atención actualizada. id={}", atention.getId());

        return atentionMapper.toResponse(atention);
    }

       @Override
    @Transactional
    public void eliminarAtention(Long id) {
        if(!atentionRepository.existsById(id)){
            throw new ResourceNotFoundException("Atención no encontrada con ID:" + id);
        }
        atentionRepository.deleteById(id);

        log.info("Atención eliminada. id={}", id);
    }

        @Override
    @Transactional
    public Page<AtentionResponseDTO> listarAtentionsDelPacienteAutenticado(String username, Pageable pageable) {
        Patient patient = patientRepository.findByUserUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Patient no encontrado para el usuario autenticado"));

        return atentionRepository.findByPatient(patient, pageable)
                .map(atentionMapper::toResponse);    }
}
