package com.example.crudrapido.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.crudrapido.dto.request.MedicalSpecialtyRequestDTO;
import com.example.crudrapido.dto.response.MedicalSpecialtyResponseDTO;
import com.example.crudrapido.mapper.config.MapperConfiguration;
import com.example.crudrapido.model.MedicalSpecialty;

@Mapper(config = MapperConfiguration.class, uses = {EmployeeMapper.class, SpecialtyMapper.class})
public interface MedicalSpecialtyMapper {

    @Mapping(source = "employeeId", target = "employee.id")
    @Mapping(source = "specialtyId", target = "specialty.id")
    MedicalSpecialty toEntity(MedicalSpecialtyRequestDTO dto);

    MedicalSpecialtyResponseDTO toResponse(MedicalSpecialty entity);

    @Mapping(source = "employeeId", target = "employee.id")
    @Mapping(source = "specialtyId", target = "specialty.id")
    void updateEntity(@MappingTarget MedicalSpecialty entity, MedicalSpecialtyRequestDTO dto);
}
