package com.example.crudrapido.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.crudrapido.model.Specialty;
import com.example.crudrapido.model.Status;

@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, Long> {

    Page<Specialty> findByEstado(Status estado, Pageable pageable);

    @Query("""
            SELECT s FROM Specialty s
            WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))
            """)
    Page<Specialty> searchByName(@Param("name") String name, Pageable pageable);

}
