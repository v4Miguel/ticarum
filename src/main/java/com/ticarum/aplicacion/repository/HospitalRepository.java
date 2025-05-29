package com.ticarum.aplicacion.repository;


import com.ticarum.aplicacion.entity.Hospital;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Annotation
@Repository

public interface HospitalRepository extends JpaRepository<Hospital, Long> {
}
