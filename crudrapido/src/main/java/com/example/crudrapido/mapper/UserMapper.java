package com.example.crudrapido.mapper;



import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.crudrapido.dto.request.UserRequestDto;
import com.example.crudrapido.dto.response.UserResponseDTO;
import com.example.crudrapido.mapper.config.MapperConfiguration;
import com.example.crudrapido.model.User;

@Mapper(config = MapperConfiguration.class, uses = {StudentMapper.class})
public interface UserMapper {

    @Mapping(source = "studentId", target = "student.id")
    User toEntity(UserRequestDto dto);

    UserResponseDTO toResponse(User entity);

    @Mapping(source = "studentId", target = "student.id")
    void updateEntity(@MappingTarget User entity, UserRequestDto dto);

}
