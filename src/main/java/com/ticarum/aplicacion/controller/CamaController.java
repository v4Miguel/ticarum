package com.ticarum.aplicacion.controller;

import com.ticarum.aplicacion.entity.Cama;
import com.ticarum.aplicacion.entity.EstadoCama;
import com.ticarum.aplicacion.exception.EntityNotFoundException;
import com.ticarum.aplicacion.service.CamaService;
import com.ticarum.aplicacion.repository.CamaRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/camas")
@Tag(name = "Camas", description = "Operaciones relacionadas con la gestión de camas")
public class CamaController {

    @Autowired
    private CamaService camaService;

    @Operation(summary = "Obtener información detallada de una cama")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cama encontrada"),
        @ApiResponse(responseCode = "404", description = "Cama no encontrada")
    })
    @GetMapping("/{idCama}")
    public ResponseEntity<Cama> obtenerCama(
            @Parameter(description = "ID de la cama") @PathVariable Long idCama) {
        Cama cama = camaService.obtenerCamaConDetalles(idCama);
        return ResponseEntity.ok(cama);
    }

    @Operation(summary = "Crear una nueva cama con valores por defecto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cama creada exitosamente")
    })
    @PostMapping("")
    public ResponseEntity<Cama> crearCama() {
        Cama nuevaCama = camaService.crearCamaConValoresPorDefecto();
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCama);
    }

    @Operation(summary = "Actualizar el estado de una cama")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Estado inválido"),
        @ApiResponse(responseCode = "404", description = "Cama no encontrada")
    })
    @PutMapping("{idCama}")
    public ResponseEntity<Cama> actualizarEstadoCama(
            @Parameter(description = "ID de la cama") @PathVariable Long idCama,
            @Parameter(description = "Nuevo estado de la cama (ej. LIBRE, OCUPADA, MANTENIMIENTO)") @RequestBody String nuevoEstado) {

        EstadoCama estado;
        try {
            estado = EstadoCama.valueOf(nuevoEstado.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }

        Cama camaActualizada = camaService.actualizarEstadoCama(idCama, estado);
        return ResponseEntity.ok(camaActualizada);
    }

    @Operation(summary = "Dar de baja una cama")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cama dada de baja correctamente"),
        @ApiResponse(responseCode = "404", description = "Cama no encontrada"),
        @ApiResponse(responseCode = "409", description = "No se puede dar de baja la cama por su estado actual")
    })
    @DeleteMapping("{idCama}")
    public ResponseEntity<Void> darDeBajaCama(
            @Parameter(description = "ID de la cama") @PathVariable Long idCama) {
        try {
            camaService.darDeBajaCama(idCama);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
