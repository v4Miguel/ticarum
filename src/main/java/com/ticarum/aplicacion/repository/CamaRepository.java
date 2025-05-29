package com.ticarum.aplicacion.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticarum.aplicacion.entity.Cama;

public interface CamaRepository extends JpaRepository<Cama, Long> {
    List<Cama> findByHospitalId(Long hospitalId);
    List<Cama> findByHospitalIdAndDependenciaId(Long hospitalId, Long dependenciaId);
    Optional<Cama> findByIdAndHospitalId(Long idCama, Long idHospital);
    void deleteByIdAndHospitalId(Long idCama, Long idHospital);
}