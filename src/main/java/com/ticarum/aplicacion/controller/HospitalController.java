package com.ticarum.aplicacion.controller;

import com.ticarum.aplicacion.dto.CamaRequest;
import com.ticarum.aplicacion.entity.Cama;
import com.ticarum.aplicacion.entity.Dependencia;
import com.ticarum.aplicacion.exception.ResourceNotFoundException;
import com.ticarum.aplicacion.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/hospitales")
@Tag(name = "Hospital", description = "Operaciones relacionadas con hospitales")
public class HospitalController {

    private final HospitalService hospitalService;

    @Autowired
    public HospitalController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @Operation(summary = "Listar camas por hospital")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
        @ApiResponse(responseCode = "404", description = "Hospital no encontrado")
    })
    @GetMapping("/{idHospital}/camas")
    public List<Cama> listarCamasPorHospital(@PathVariable Long idHospital) {
        return hospitalService.obtenerCamasPorHospital(idHospital);
    }

    @Operation(summary = "Listar dependencias por hospital")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
        @ApiResponse(responseCode = "404", description = "Hospital no encontrado")
    })
    @GetMapping("/{idHospital}/dependencias")
    public List<Dependencia> listarDependenciasPorHospital(@PathVariable Long idHospital) {
        return hospitalService.obtenerDependenciasPorHospital(idHospital);
    }

    @Operation(summary = "Listar camas por dependencia de un hospital")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
        @ApiResponse(responseCode = "404", description = "Hospital o dependencia no encontrados")
    })
    @GetMapping("/{idHospital}/{idDependencia}/camas")
    public List<Cama> listarCamasPorDependencia(@PathVariable Long idHospital, @PathVariable Long idDependencia) {
        return hospitalService.obtenerCamasPorDependencia(idHospital, idDependencia);
    }

    @Operation(summary = "Asignar cama a hospital y dependencia")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cama asignada correctamente"),
        @ApiResponse(responseCode = "404", description = "Hospital, cama o dependencia no encontrados"),
        @ApiResponse(responseCode = "409", description = "La dependencia no pertenece al hospital")
    })
    @PutMapping("/{idHospital}/camas/{idCama}")
    public ResponseEntity<Cama> asignarCama(@PathVariable Long idHospital,
                                            @PathVariable Long idCama,
                                            @RequestBody CamaRequest request) {
        try {
            Cama camaActualizada = hospitalService.asignarCamaAHospitalYDependencia(idHospital, idCama, request.getIdDependencia());
            return ResponseEntity.ok(camaActualizada);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).build();
        }
    }

    @Operation(summary = "Eliminar cama de un hospital")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cama eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Cama no encontrada en el hospital")
    })
    @DeleteMapping("/{idHospital}/camas/{idCama}")
    public ResponseEntity<Void> eliminarCamaDeHospital(@PathVariable Long idHospital, @PathVariable Long idCama) {
        try {
            hospitalService.eliminarCamaDeHospital(idHospital, idCama);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

    
   
