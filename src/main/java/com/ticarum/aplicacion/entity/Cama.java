package com.ticarum.aplicacion.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cama {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EstadoCama estado;

    @ManyToOne
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @ManyToOne
    @JoinColumn(name = "dependencia_id")
    private Dependencia dependencia;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public EstadoCama getEstado() { return estado; }
    public void setEstado(EstadoCama estado) { this.estado = estado; }

    public Hospital getHospital() { return hospital; }
    public void setHospital(Hospital hospital) { this.hospital = hospital; }

    public Dependencia getDependencia() { return dependencia; }
    public void setDependencia(Dependencia dependencia) { this.dependencia = dependencia; }
}

