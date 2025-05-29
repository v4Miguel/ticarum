package com.ticarum.aplicacion.service;

import com.ticarum.aplicacion.entity.Cama;
import com.ticarum.aplicacion.entity.Dependencia;
import com.ticarum.aplicacion.entity.Hospital;
import com.ticarum.aplicacion.exception.ResourceNotFoundException;
import com.ticarum.aplicacion.repository.CamaRepository;
import com.ticarum.aplicacion.repository.DependenciaRepository;
import com.ticarum.aplicacion.repository.HospitalRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HospitalServiceImp implements HospitalService {

    @Autowired private CamaRepository camaRepository;
    @Autowired private HospitalRepository hospitalRepository;
    @Autowired private DependenciaRepository dependenciaRepository;

    @Override
    public List<Cama> obtenerCamasPorHospital(Long hospitalId) {
        verificarExistenciaHospital(hospitalId);
        return camaRepository.findByHospitalId(hospitalId);
    }

    @Override
    public List<Dependencia> obtenerDependenciasPorHospital(Long hospitalId) {
        verificarExistenciaHospital(hospitalId);
        return dependenciaRepository.findByHospitalId(hospitalId);
    }

    @Override
    public List<Cama> obtenerCamasPorDependencia(Long hospitalId, Long dependenciaId) {
        verificarExistenciaHospital(hospitalId);
        verificarExistenciaDependencia(dependenciaId);
        return camaRepository.findByHospitalIdAndDependenciaId(hospitalId, dependenciaId);
    }

    @Override
    public Cama asignarCamaAHospitalYDependencia(Long hospitalId, Long camaId, Long dependenciaId) {
        Cama cama = camaRepository.findById(camaId)
                .orElseThrow(() -> new ResourceNotFoundException("Cama no encontrada"));
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital no encontrado"));
        Dependencia dependencia = dependenciaRepository.findById(dependenciaId)
                .orElseThrow(() -> new ResourceNotFoundException("Dependencia no encontrada"));

        if (!dependencia.getHospital().getId().equals(hospital.getId())) {
            throw new IllegalStateException("La dependencia no pertenece al hospital");
        }

        cama.setHospital(hospital);
        cama.setDependencia(dependencia);
        return camaRepository.save(cama);
    }

    @Override
    @Transactional
    public void eliminarCamaDeHospital(Long idHospital, Long idCama) {
        Optional<Cama> cama = camaRepository.findByIdAndHospitalId(idCama, idHospital);
        if (cama.isPresent()) {
            camaRepository.deleteByIdAndHospitalId(idCama, idHospital);
        } else {
            throw new ResourceNotFoundException("Cama no encontrada en el hospital especificado");
        }
    }

    private void verificarExistenciaHospital(Long id) {
        if (!hospitalRepository.existsById(id)) {
            throw new ResourceNotFoundException("Hospital no encontrado");
        }
    }

    private void verificarExistenciaDependencia(Long id) {
        if (!dependenciaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Dependencia no encontrada");
        }
    }
}




