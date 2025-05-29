package com.ticarum.aplicacion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticarum.aplicacion.entity.Dependencia;
import com.ticarum.aplicacion.entity.Hospital;

public interface DependenciaRepository extends JpaRepository<Dependencia, Long> {
    List<Dependencia> findByHospitalId(Long hospitalId);
}
