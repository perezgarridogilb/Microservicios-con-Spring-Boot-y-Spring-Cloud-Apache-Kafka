package com.example.crudrapido.dto.response;
import com.example.crudrapido.model.Status;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class StudentResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
      private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
