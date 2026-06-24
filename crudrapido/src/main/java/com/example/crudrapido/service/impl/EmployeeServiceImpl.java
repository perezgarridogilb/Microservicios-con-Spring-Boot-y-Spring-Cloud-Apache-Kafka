package com.example.crudrapido.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.crudrapido.dto.request.EmployeeRequestDTO;
import com.example.crudrapido.dto.response.EmployeeResponseDTO;
import com.example.crudrapido.exception.ResourceNotFoundException;
import com.example.crudrapido.mapper.EmployeeMapper;
import com.example.crudrapido.model.Employee;
import com.example.crudrapido.model.Status;
import com.example.crudrapido.model.Student;
import com.example.crudrapido.repository.EmployeeRepository;
import com.example.crudrapido.repository.StudentRepository;
import com.example.crudrapido.service.EmployeeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository empleadoRepository;
    private final StudentRepository personaRepository;
    private final EmployeeMapper empleadoMapper;

    @Override
    @Transactional
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO requestDto) {
        Student persona = personaRepository.findById(requestDto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Persona no encontrada con id: " + requestDto.getStudentId()));

        Employee empleado = empleadoMapper.toEntity(requestDto);
        empleado.setStudent(persona);
        empleado = empleadoRepository.save(empleado);

        log.info("Empleado creado. id={}", empleado.getId());
        return empleadoMapper.toResponse(empleado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> listarEmployees() {
        return empleadoRepository.findAll()
                .stream()
                .map(empleadoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponseDTO> listarPorStatus(String estado, Pageable pageable) {
        Page<Employee> page = empleadoRepository.findByStatus(Enum.valueOf(Status.class, estado.toUpperCase()), pageable);
        return page.map(empleadoMapper::toResponse);
    }

    @Override
    @Transactional
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO dto) {
        Employee empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id: " + id));


        if(!empleado.getStudent().getId().equals(dto.getStudentId())){
            Student persona = personaRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Persona no encontrada con id: " + dto.getStudentId()));
            empleado.setStudent(persona);
        }
       
        empleadoMapper.updateEntity(empleado, dto);
       
        Employee updateEmpleado = empleadoRepository.save(empleado);

        log.info("Empleado actualizado. id={}", empleado.getId());
        return empleadoMapper.toResponse(updateEmpleado);
    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        Employee empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id: " + id));

        empleadoRepository.delete(empleado);

        log.info("Empleado eliminado. id={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeeResponseDTO> findById(Long id) {
        return empleadoRepository.findById(id)
                .map(empleadoMapper::toResponse);
    }

}