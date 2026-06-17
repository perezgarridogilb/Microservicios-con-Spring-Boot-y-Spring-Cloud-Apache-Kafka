package com.example.crudrapido.dto.response;

import com.example.crudrapido.model.Status;

import lombok.Data;

@Data
public class SpecialtyResponseDTO {
    private Long id;
    private String name;
    private Status status;
}
