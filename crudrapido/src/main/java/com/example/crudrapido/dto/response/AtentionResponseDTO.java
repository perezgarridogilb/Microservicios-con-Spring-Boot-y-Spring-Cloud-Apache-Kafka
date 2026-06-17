package com.example.crudrapido.dto.response;

import java.time.LocalDateTime;

import com.example.crudrapido.model.Status;

import lombok.Data;

@Data
/*
public AtentionResponseDTO toResponse(Atention entity) {
    if (entity == null) {
        return null;
    }

    AtentionResponseDTO dto = new AtentionResponseDTO();
    dto.setId(entity.getId());
    dto.setDate(entity.getDate());
    dto.setAtention(entity.getAtention());
    dto.setStatus(entity.getStatus());

    // ¡AQUÍ ES DONDE SE USA EL 'USES'!
    // Equivale a: 'student' => new StudentResource($this->student)
    dto.setStudent(studentMapper.toResponse(entity.getStudent()));
    
    // Equivale a: 'employee' => new EmployeeResource($this->employee)
    dto.setEmployee(employeeMapper.toResponse(entity.getEmployee()));

    return dto;
}
*/
public class AtentionResponseDTO {
    private Long id;
    private LocalDateTime date;
    private String atention;
    private Status status;
    private StudentResponseDTO student;
    private EmployeeResponseDTO employee;
}
