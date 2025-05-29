package com.ticarum.aplicacion.service;

import com.ticarum.aplicacion.entity.Cama;
import com.ticarum.aplicacion.entity.Dependencia;

import java.util.List;

public interface HospitalService {

    List<Cama> obtenerCamasPorHospital(Long id);

    List<Dependencia> obtenerDependenciasPorHospital(Long hospitalId);

    List<Cama> obtenerCamasPorDependencia(Long hospitalId, Long dependenciaId);

    
    Cama asignarCamaAHospitalYDependencia(Long hospitalId, Long camaId, Long dependenciaId);
    
    void eliminarCamaDeHospital(Long hospitalId, Long camaId);
    
}
