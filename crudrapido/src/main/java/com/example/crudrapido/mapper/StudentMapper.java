package com.example.crudrapido.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.example.crudrapido.dto.response.StudentResponseDTO;
import com.example.crudrapido.mapper.config.MapperConfiguration;
import com.example.crudrapido.model.Student;

@Mapper(config = MapperConfiguration.class)
public interface StudentMapper {
    Student toEntity(StudentResponseDTO dto);

    StudentResponseDTO toResponse(Student entity);

    void updateEntity(@MappingTarget Student entity, StudentResponseDTO dto);
}

/*
  EQUIVALENCIA EXACTA EN LARAVEL:
  
  1. 'toResponse' es la FUNCIÓN/MÉTODO:
     Equivale a la función "public function toArray($request)" de tu API Resource.
     Es la maquinaria que ejecuta la conversión.
     
  2. El 'StudentResponseDTO' que te regresa es el ARRAY ASOCIATIVO:
     Es el resultado final, el contenedor 'clave => valor'.
     
     En Java se ve como un Objeto:  dto.getName() -> "Gilberto"
     En Laravel se ve como un Array: $dto['name'] -> "Gilberto"
*/
