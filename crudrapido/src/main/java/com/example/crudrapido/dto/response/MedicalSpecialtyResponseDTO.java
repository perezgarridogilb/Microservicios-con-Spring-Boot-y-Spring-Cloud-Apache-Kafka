package com.example.crudrapido.dto.response;

import lombok.Data;

@Data
public class MedicalSpecialtyResponseDTO {
    private Long id;
    private EmployeeResponseDTO employee;
    private SpecialtyResponseDTO specialty;
}
