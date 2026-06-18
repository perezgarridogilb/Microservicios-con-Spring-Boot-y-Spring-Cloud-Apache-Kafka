package com.example.crudrapido.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.crudrapido.dto.request.EmployeeRequestDTO;
import com.example.crudrapido.dto.response.EmployeeResponseDTO;

public interface EmployeeService {

    EmployeeResponseDTO createEmployee(EmployeeRequestDTO requestDto);

     List<EmployeeResponseDTO> listarEmpleados();

    List<EmployeeResponseDTO> listAll();

    Page<EmployeeResponseDTO> listaPorStatus(String status, Pageable pageable);

    EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO dto);

    void deleteEmployee(Long id);

    Optional<EmployeeResponseDTO> findById(Long id);
}
