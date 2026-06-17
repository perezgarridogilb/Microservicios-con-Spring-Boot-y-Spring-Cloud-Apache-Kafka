package com.example.crudrapido.mapper;

import org.mapstruct.Mapper;

import com.example.crudrapido.dto.request.EmployeeRequestDTO;
import com.example.crudrapido.dto.response.EmployeeResponseDTO;
import com.example.crudrapido.mapper.config.MapperConfiguration;
import com.example.crudrapido.model.Employee;

@Mapper(config = MapperConfiguration.class)
public interface EmployeeMapper {

    Employee toEntity(EmployeeRequestDTO dto);

    EmployeeResponseDTO toResponse(Employee employee);
}
