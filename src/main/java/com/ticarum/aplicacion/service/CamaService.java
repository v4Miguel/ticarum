package com.ticarum.aplicacion.service;

import com.ticarum.aplicacion.entity.Cama;
import com.ticarum.aplicacion.entity.Dependencia;
import com.ticarum.aplicacion.entity.EstadoCama;

import java.util.List;

public interface CamaService {

    Cama obtenerCamaConDetalles(Long id);
    
    Cama crearCamaConValoresPorDefecto();
    
    Cama actualizarEstadoCama(Long idCama, EstadoCama estado);

	void darDeBajaCama(Long idCama);

	
}
