package com.ticarum.aplicacion.dto;

//DTO para devolver info completa de una cama
public class CamaResponse {
 private Long id;
 private String estado;
 private HospitalDTO hospital;
 private DependenciaDTO dependencia;

 public Long getId() {
     return id;
 }

 public void setId(Long id) {
     this.id = id;
 }

 public String getEstado() {
     return estado;
 }

 public void setEstado(String estado) {
     this.estado = estado;
 }

 public HospitalDTO getHospital() {
     return hospital;
 }

 public void setHospital(HospitalDTO hospital) {
     this.hospital = hospital;
 }

 public DependenciaDTO getDependencia() {
     return dependencia;
 }

 public void setDependencia(DependenciaDTO dependencia) {
     this.dependencia = dependencia;
 }
}