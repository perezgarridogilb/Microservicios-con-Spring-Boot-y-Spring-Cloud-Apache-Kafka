package com.example.crudrapido.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.crudrapido.dto.request.EmployeeRequestDTO;
import com.example.crudrapido.dto.response.EmployeeResponseDTO;
import com.example.crudrapido.mapper.config.MapperConfiguration;
import com.example.crudrapido.model.Employee;

@Mapper(config = MapperConfiguration.class)
public interface EmployeeMapper {
    @Mapping(source = "studentId", target = "student.id")
    Employee toEntity(EmployeeRequestDTO dto);

    EmployeeResponseDTO toResponse(Employee employee);

        @Mapping(source = "studentId", target = "student.id")
    void updateEntity(@MappingTarget Employee entity, EmployeeRequestDTO dto);
}
