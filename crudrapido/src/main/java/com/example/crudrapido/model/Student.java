package com.example.crudrapido.model;

import java.time.LocalDateTime;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// @Data === getters, setters, toString, equals, hashCode automáticos (como los accessors de Eloquent)
// @Entity === class Student extends Model (en Laravel)
// @Table === protected $table = 'tbl_student'
@Data
@Entity
@Table(name = "tbl_student")
public class Student {
    // @Id + @GeneratedValue === $incrementing = true + primary key auto
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @NotBlank === 'required' en validation
    // private String firstName === protected $fillable = ['first_name']
    private String firstName;

    private String lastName;

    // @NotBlank === 'required'
    // @Email === 'email'
    // @Column(name = "email_address", unique = true) === $table->string('email')->unique()
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Column(name = "email_address", unique = true)
    private String email;

    private LocalDateTime createdAt;

private LocalDateTime updatedAt;

@Column(name = "date_event")
    private LocalDateTime dateEvent;

    private String operation;

// @PrePersist === static::creating() en Laravel (booted)
// @PreUpdate === static::updating() en Laravel (booted)
@PrePersist
protected void inicializarFechas() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
    audit("INSERT");
}

@PreUpdate
protected void actualizarFechaActualizacion() {
    updatedAt = LocalDateTime.now();
}

public void audit(String operation) {
        setOperation(operation);
        setDateEvent(LocalDateTime.now());
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setDateEvent(LocalDateTime dateEvent) {
        this.dateEvent = dateEvent;
    }
}
