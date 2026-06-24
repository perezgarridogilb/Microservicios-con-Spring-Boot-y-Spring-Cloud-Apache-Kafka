package com.example.crudrapido.mapper;



import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.crudrapido.dto.request.SpecialtyRequestDTO;
import com.example.crudrapido.dto.response.SpecialtyResponseDTO;
import com.example.crudrapido.mapper.config.MapperConfiguration;
import com.example.crudrapido.model.Specialty;

@Mapper(config = MapperConfiguration.class)
public interface SpecialtyMapper {

    @Mapping(source = "status", target = "estado")
    Specialty toEntity(SpecialtyRequestDTO dto);

    @Mapping(source = "estado", target = "status")
    SpecialtyResponseDTO toResponse(Specialty entity);

    void updateEntity(@MappingTarget Specialty entity, SpecialtyRequestDTO dto);

}
