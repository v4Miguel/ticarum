package com.ticarum.aplicacion;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ticarum.aplicacion.entity.Cama;
import com.ticarum.aplicacion.entity.Dependencia;
import com.ticarum.aplicacion.entity.EstadoCama;
import com.ticarum.aplicacion.entity.Hospital;
import com.ticarum.aplicacion.repository.CamaRepository;
import com.ticarum.aplicacion.service.CamaServiceImp;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)  // ← Esto habilita Mockito sin levantar Spring
class CamaServiceTest {

    @Mock
    private CamaRepository camaRepository;

    @InjectMocks
    private CamaServiceImp camaService;

    @Test
    void testActualizarEstadoCama_Valido_SinHospital() {
        // Arrange
        Long idCama = 1L;
        Cama cama = new Cama();
        cama.setId(idCama);
        cama.setEstado(EstadoCama.LIBRE);
        cama.setHospital(null);  // ← Es importante que sea null

        when(camaRepository.findById(idCama)).thenReturn(Optional.of(cama));
        when(camaRepository.save(any(Cama.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Cama resultado = camaService.actualizarEstadoCama(idCama, EstadoCama.OCUPADA);

        // Assert
        assertNotNull(resultado);
        assertEquals(EstadoCama.OCUPADA, resultado.getEstado());
        verify(camaRepository).findById(idCama);
        verify(camaRepository).save(cama);
    }
    
    @Test
    void testObtenerCamaPorId_CamaExistente() {
        Long idCama = 1L;
        Cama cama = new Cama();
        cama.setId(idCama);
        cama.setEstado(EstadoCama.LIBRE);
        cama.setHospital(new Hospital());
        cama.setDependencia(new Dependencia());

        when(camaRepository.findById(idCama)).thenReturn(Optional.of(cama));

        Cama resultado = camaService.obtenerCamaConDetalles(idCama);
        assertNotNull(resultado);
        assertEquals(EstadoCama.LIBRE, resultado.getEstado());
        assertNotNull(resultado.getHospital());
        assertNotNull(resultado.getDependencia());
    }

    @Test
    void testObtenerCamaPorId_CamaNoExistente() {
        Long idCama = 99L;
        when(camaRepository.findById(idCama)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> camaService.obtenerCamaConDetalles(idCama));
    }
    
    @Test
    void testRegistrarNuevaCamaConValoresPorDefecto() {
        Cama nueva = new Cama();  // Simula que no se recibe ID aún (autogenerado)
        when(camaRepository.save(any(Cama.class))).thenAnswer(i -> {
            Cama c = i.getArgument(0);
            c.setId(1L);
            return c;
        });

        Cama resultado = camaService.crearCamaConValoresPorDefecto(); // Método que tú debes tener
        assertNotNull(resultado.getId());
        assertEquals(EstadoCama.LIBRE, resultado.getEstado());
    }

    @Test
    void testActualizarEstadoCama_CambioValido_SinHospital() {
        Cama cama = new Cama();
        cama.setId(1L);
        cama.setEstado(EstadoCama.LIBRE);
        cama.setHospital(null);

        when(camaRepository.findById(1L)).thenReturn(Optional.of(cama));
        when(camaRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Cama resultado = camaService.actualizarEstadoCama(1L, EstadoCama.OCUPADA);
        assertEquals(EstadoCama.OCUPADA, resultado.getEstado());
    }

    @Test
    void testActualizarEstadoCama_CambioNoPermitido() {
        Cama cama = new Cama();
        cama.setId(1L);
        cama.setEstado(EstadoCama.AVERIADA);
        cama.setHospital(null);

        when(camaRepository.findById(1L)).thenReturn(Optional.of(cama));

        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> camaService.actualizarEstadoCama(1L, EstadoCama.OCUPADA));
        assertTrue(ex.getMessage().contains("Transición de estado no válida"));
    }

    @Test
    void testActualizarEstadoCama_CamaConHospital() {
        Cama cama = new Cama();
        cama.setId(1L);
        cama.setEstado(EstadoCama.LIBRE);
        cama.setHospital(new Hospital());

        when(camaRepository.findById(1L)).thenReturn(Optional.of(cama));

        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> camaService.actualizarEstadoCama(1L, EstadoCama.OCUPADA));
        assertTrue(ex.getMessage().contains("cama asignada a un hospital"));
    }
    
    @Test
    void testDarDeBajaCama_Exitosa() {
        Cama cama = new Cama();
        cama.setId(1L);
        cama.setEstado(EstadoCama.LIBRE);

        when(camaRepository.findById(1L)).thenReturn(Optional.of(cama));
        when(camaRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        camaService.darDeBajaCama(1L);  // Método debe marcar estado como BAJA

        assertEquals(EstadoCama.BAJA, cama.getEstado());
        verify(camaRepository).save(cama);
    }


}
