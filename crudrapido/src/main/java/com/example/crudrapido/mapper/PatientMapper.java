package com.example.crudrapido.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.crudrapido.dto.request.PatientRequestDTO;
import com.example.crudrapido.dto.response.PatientResponseDTO;
import com.example.crudrapido.mapper.config.MapperConfiguration;
import com.example.crudrapido.model.Patient;

@Mapper(config = MapperConfiguration.class, uses = {StudentMapper.class})
public interface PatientMapper {

    @Mapping(source = "studentId", target = "student.id")
    Patient toEntity(PatientRequestDTO dto);

    PatientResponseDTO toResponse(Patient patient);

    @Mapping(source = "studentId", target = "student.id")
    void updateEntity(@MappingTarget Patient entity, PatientRequestDTO dto);
}
