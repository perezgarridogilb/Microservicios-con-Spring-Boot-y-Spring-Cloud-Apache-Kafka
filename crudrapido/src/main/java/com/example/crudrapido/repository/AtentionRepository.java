package com.example.crudrapido.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.crudrapido.model.Atention;
import com.example.crudrapido.model.Patient;
import com.example.crudrapido.model.Status;

@Repository
public interface AtentionRepository extends JpaRepository<Atention, Long> {

    Page<Atention> findByPatient(Patient patient, Pageable pageable);

    Page<Atention> findByStatus(Status status, Pageable pageable);

    Page<Atention> findByDateBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT a FROM Atention a WHERE LOWER(a.atention) LIKE LOWER(CONCAT('%', :motivo, '%'))")
    Page<Atention> searchByAtention(@Param("atention") String motivo, Pageable pageable);
}
