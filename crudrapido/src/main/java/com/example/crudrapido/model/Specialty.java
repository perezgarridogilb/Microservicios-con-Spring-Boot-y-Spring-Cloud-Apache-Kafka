package com.example.crudrapido.model;

import com.example.crudrapido.model.Status;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "specialties")
public class Specialty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status estado;
    /** hijo: MedicalSpecialty
     * Relación Uno a Muchos con Especialidades Médicas.
     * * [Característica en Spring Boot]: @OneToMany(mappedBy = "especialidad",
     * cascade=CascadeType.ALL, orphanRemoval = true)
     * -> Equivalente en Laravel (Eloquent): hasMany(MedicalSpecialty::class,
     * 'especialidad_id') junto con cascadeOnDelete() en la migración.
     * -> ¿Cómo funciona?:
     * - @OneToMany: Define que una entidad actual tiene muchos registros asociados
     * de 'MedicalSpecialty'.
     * - mappedBy = "especialidad": Cede la propiedad de la relación al campo
     * 'especialidad' en la clase hija (no genera columna FK aquí).
     * En Laravel, esto se define pasando explícitamente el string 'especialidad_id'
     * como segundo parámetro del hasMany.
     * - cascade=CascadeType.ALL: Cualquier operación de persistencia (guardar,
     * actualizar, eliminar) efectuada en esta entidad se replicará
     * automáticamente en sus elementos hijos. En Laravel se debe programar
     * explícitamente en el método booted() o controladores.
     * - orphanRemoval = true: Si un objeto hijo es removido de la colección (Set),
     * Hibernate lo eliminará físicamente de la base de datos.
     * En Laravel, esto se logra agregando ->cascadeOnDelete() en la restricción de
     * la llave foránea dentro de la migración.
     * * [Característica en Spring Boot]: private Set<MedicalSpecialty> empleados =
     * new HashSet<>();
     * -> Equivalente en Laravel (Eloquent): Retorna una instancia de
     * 'Illuminate\Database\Eloquent\Collection'.
     * -> ¿Cómo funciona?:
     * - Set: Se utiliza una colección de tipo Set para garantizar la unicidad de
     * los elementos y optimizar el rendimiento de JPA/Hibernate.
     * Laravel utiliza colecciones genéricas que cuentan con métodos como ->unique()
     * si se requiere el mismo comportamiento.
     * - = new HashSet<>(): Inicializa la estructura de datos en memoria para
     * prevenir excepciones de tipo NullPointerException (NFP).
     * En Laravel esto es innecesario, ya que Eloquent por defecto inyecta una
     * colección vacía si el registro no cuenta con relaciones.
     */
    @JsonManagedReference
    @OneToMany(mappedBy = "specialty", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MedicalSpecialty> empleados = new HashSet<>();
}
