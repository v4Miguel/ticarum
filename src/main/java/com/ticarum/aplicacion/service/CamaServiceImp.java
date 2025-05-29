package com.ticarum.aplicacion.service;

import com.ticarum.aplicacion.entity.Cama;
import com.ticarum.aplicacion.entity.Dependencia;
import com.ticarum.aplicacion.entity.EstadoCama;
import com.ticarum.aplicacion.entity.Hospital;
import com.ticarum.aplicacion.repository.CamaRepository;
import com.ticarum.aplicacion.repository.DependenciaRepository;
import com.ticarum.aplicacion.repository.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ticarum.aplicacion.exception.EntityNotFoundException;

import java.util.EnumSet;
import java.util.List;

@Service
public class CamaServiceImp implements CamaService {

    private final CamaRepository camaRepository;

    @Autowired
    public CamaServiceImp(
        CamaRepository camaRepository) {
        this.camaRepository = camaRepository;
    }

    @Override
    public Cama obtenerCamaConDetalles(Long idCama) {
        return camaRepository.findById(idCama)
                .orElseThrow(() -> new EntityNotFoundException("Cama no encontrada con id " + idCama));
    }
    
    public Cama crearCamaConValoresPorDefecto() {
        Cama cama = new Cama();
        cama.setEstado(EstadoCama.LIBRE);
        //hospital y dependecia vacias
        return camaRepository.save(cama);
    }
    
    public Cama actualizarEstadoCama(Long idCama, EstadoCama nuevoEstado) {
        Cama cama = camaRepository.findById(idCama)
                .orElseThrow(() -> new RuntimeException("Cama no encontrada"));
        
        
        if (cama.getHospital() != null) {
            throw new RuntimeException("No se puede actualizar el estado de una cama asignada a un hospital");
        }
        
        EstadoCama estadoActual = cama.getEstado();

        if (!esTransicionValida(estadoActual, nuevoEstado)) {
            throw new RuntimeException("Transición de estado no válida: " + estadoActual + " → " + nuevoEstado);
        }

        cama.setEstado(nuevoEstado);
        return camaRepository.save(cama);
    }

    private boolean esTransicionValida(EstadoCama actual, EstadoCama nuevo) {
        return switch (actual) {
            case LIBRE -> EnumSet.of(EstadoCama.OCUPADA, EstadoCama.AVERIADA, EstadoCama.BAJA).contains(nuevo);
            case OCUPADA -> EnumSet.of(EstadoCama.LIBRE, EstadoCama.AVERIADA).contains(nuevo);
            case AVERIADA -> EnumSet.of(EstadoCama.EN_REPARACION).contains(nuevo);
            case EN_REPARACION -> EnumSet.of(EstadoCama.LIBRE).contains(nuevo);
            case BAJA -> false;
        };
    }
    
    public void darDeBajaCama(Long idCama) {
        Cama cama = camaRepository.findById(idCama)
                     .orElseThrow(() -> new EntityNotFoundException("Cama no encontrada"));

        if (cama.getEstado() == EstadoCama.BAJA) {
            throw new IllegalStateException("La cama ya está dada de baja");
        }

        // Aquí puedes añadir reglas extra si hace falta antes de cambiar el estado

        cama.setEstado(EstadoCama.BAJA);
        camaRepository.save(cama);
    }





    
}
