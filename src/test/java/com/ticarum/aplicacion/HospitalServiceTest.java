package com.ticarum.aplicacion;

import com.ticarum.aplicacion.entity.*;
import com.ticarum.aplicacion.exception.ResourceNotFoundException;
import com.ticarum.aplicacion.repository.CamaRepository;
import com.ticarum.aplicacion.repository.DependenciaRepository;
import com.ticarum.aplicacion.repository.HospitalRepository;
import com.ticarum.aplicacion.service.HospitalService;
import com.ticarum.aplicacion.service.HospitalServiceImp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HospitalServiceTest {

	@Mock
	private HospitalRepository hospitalRepository;

	@Mock
	private CamaRepository camaRepository;

	@Mock
	private DependenciaRepository dependenciaRepository;

	@InjectMocks
	private HospitalServiceImp hospitalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerCamasPorHospital() {
        Long idHospital = 1L;
        Cama cama = new Cama();
        cama.setId(1L);
        cama.setHospital(new Hospital());

        when(camaRepository.findByHospitalId(idHospital)).thenReturn(Collections.singletonList(cama));

        List<Cama> camas = hospitalService.obtenerCamasPorHospital(idHospital);
        assertEquals(1, camas.size());
    }

    @Test
    void testObtenerDependenciasPorHospital() {
        Long idHospital = 1L;
        Dependencia dep = new Dependencia();
        dep.setId(1L);

        when(dependenciaRepository.findByHospitalId(idHospital)).thenReturn(Collections.singletonList(dep));

        List<Dependencia> dependencias = hospitalService.obtenerDependenciasPorHospital(idHospital);
        assertEquals(1, dependencias.size());
    }

    @Test
    void testObtenerCamasPorDependencia() {
        Long idHospital = 1L;
        Long idDependencia = 2L;
        Cama cama = new Cama();
        cama.setId(1L);
        cama.setDependencia(new Dependencia());

        when(camaRepository.findByHospitalIdAndDependenciaId(idHospital, idDependencia)).thenReturn(List.of(cama));

        List<Cama> camas = hospitalService.obtenerCamasPorDependencia(idHospital, idDependencia);
        assertEquals(1, camas.size());
    }

    @Test
    void testAsignarCamaAHospitalYDependencia() {
        Long idHospital = 1L;
        Long idCama = 2L;
        Long idDependencia = 3L;

        Cama cama = new Cama();
        cama.setId(idCama);

        Hospital hospital = new Hospital();
        hospital.setId(1L);

        Dependencia dependencia = new Dependencia();
        dependencia.setId(3L);
        dependencia.setHospital(hospital);


        when(camaRepository.findById(idCama)).thenReturn(Optional.of(cama));
        when(hospitalRepository.findById(idHospital)).thenReturn(Optional.of(hospital));
        when(dependenciaRepository.findById(idDependencia)).thenReturn(Optional.of(dependencia));
        when(camaRepository.save(any(Cama.class))).thenAnswer(i -> i.getArgument(0));

        Cama resultado = hospitalService.asignarCamaAHospitalYDependencia(idHospital, idCama, idDependencia);

        assertNotNull(resultado);
        assertEquals(idHospital, resultado.getHospital().getId());
        assertEquals(idDependencia, resultado.getDependencia().getId());
    }

    @Test
    void testEliminarCamaDeHospital() {
        Long idHospital = 1L;
        Long idCama = 1L;

        Hospital hospital = new Hospital();
        hospital.setId(idHospital);

        Cama cama = new Cama();
        cama.setId(idCama);
        cama.setHospital(hospital); 
        cama.setDependencia(new Dependencia());

        when(camaRepository.findByIdAndHospitalId(idCama, idHospital)).thenReturn(Optional.of(cama));
        doNothing().when(camaRepository).deleteByIdAndHospitalId(idCama, idHospital);

        hospitalService.eliminarCamaDeHospital(idHospital, idCama);

        verify(camaRepository).deleteByIdAndHospitalId(idCama, idHospital);
    }
    
    @Test
    void testEliminarCamaDeHospital_CamaNoEncontrada() {
        Long idHospital = 1L;
        Long idCama = 1L;

        // Mockeamos que no se encuentra la cama
        when(camaRepository.findByIdAndHospitalId(idCama, idHospital)).thenReturn(Optional.empty());

        // Se espera que lance ResourceNotFoundException
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            hospitalService.eliminarCamaDeHospital(idHospital, idCama);
        });

        assertEquals("Cama no encontrada en el hospital especificado", exception.getMessage());

        // Verificamos que no se haya llamado a delete
        verify(camaRepository, never()).deleteByIdAndHospitalId(anyLong(), anyLong());
    }


    
    

}
