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
public class EmpleadoServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final StudentRepository studentRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    @Transactional
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO requestDto) {
        Student student = studentRepository.findById(requestDto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Persona no encontrada con id: " + requestDto.getPersonaId()));

        Employee employee = employeeMapper.toEntity(requestDto);
        employee.setStudent(student);
        employee = employeeRepository.save(employee);

        log.info("Empleado creado. id={}", employee.getId());
        return employeeMapper.toResponse(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> listarEmpleados() {
        return employeeRepository.findAll()
                .stream()
                .map(employeeMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponseDTO> listaPorStatus(String estado, Pageable pageable) {
        Page<Employee> page = employeeRepository.findByStatus(Enum.valueOf(Status.class, estado.toUpperCase()), pageable);
        return page.map(employeeMapper::toResponse);
    }

    @Override
    @Transactional
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO dto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id: " + id));


        if(!employee.getStudent().getId().equals(dto.getStudentId())){
            Student persona = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Persona no encontrada con id: " + dto.getPersonaId()));
            employee.setStudent(persona);
        }
       
        employeeMapper.updateEntity(employee, dto);
       
        Employee updateEmpleado = employeeRepository.save(employee);

        log.info("Empleado actualizado. id={}", employee.getId());
        return employeeMapper.toResponse(updateEmpleado);
    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        Employee empleado = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id: " + id));

        employeeRepository.delete(empleado);

        log.info("Empleado eliminado. id={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeeResponseDTO> findById(Long id) {
        return employeeRepository.findById(id)
                .map(empleadoMapper::toResponse);
    }

}
