package com.example.crudrapido.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import com.example.crudrapido.model.Atention;

@Repository
public interface AtentionRepository extends JpaRepository<Atention, Long> {
     
/**
     * [Característica en Spring Boot]: @Query con JPQL, Page<Atention> y Pageable
     * -> Equivalente en Laravel (Eloquent): Query Scope utilizando DB::raw() completo.
     * -> ¿Cómo funciona?: Evita que Eloquent altere o escape las funciones de la base de datos.
     * * Ejemplo del en el Modelo 'Atention.php' de Laravel:
     * --------------------------------------------------------------------------------------
* public function scopeSearchByMotivoExplicit($query, $motivo, $page = 3, $perPage = 10) {
     * // Se calcula el OFFSET manualmente: (3 - 1) * 10 = 20
     * $calculatedOffset = ($page - 1) * $perPage;
     * * return $query->where(
     * \DB::raw("LOWER(motivo)"), 
     * "LIKE", 
     * \DB::raw("LOWER(CONCAT('%', '{$motivo}', '%'))")
     * )
     * ->limit($perPage)          // <-- LIMIT EXPLÍCITO (Toma 10 registros)
     * ->offset($calculatedOffset); // <-- OFFSET EXPLÍCITO (Se salta los primeros 20)
     * }
     * --------------------------------------------------------------------------------------
     */
        @Query("SELECT a FROM Atention a WHERE LOWER(a.atention) LIKE LOWER(CONCAT('%', :motivo, '%'))")
    Page<Atention> searchByMotivo(@Param("motivo") String motivo, Pageable pageable);
}
