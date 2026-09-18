package com.example.beta_1_nororiental.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "empresas")
public class Empresa {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "", nullable = false, unique = false, length = 50)
    private String nombre;
    @Column(name = "", nullable = false, unique = false, length = 20)
    private String nit;
    @Column(name = "", nullable = false, unique = true, length = 30)
    private String sector;
    @Column(name = "", nullable = true, unique = true, length = 20)
    private String contacto;
    @Column(name = "", nullable = false, unique = true, length = 80)
    private String correo;
    @Column(name = "", nullable = false, unique = true, length = 20)
    private String telefono;
    @Column(name = "", nullable = false, unique = false)
    private boolean activo;
    @OneToMany(mappedBy = "empresa")
    @JsonManagedReference
    private List<Reto> retos = new ArrayList<>();

    public Empresa(){

    }

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getNit() {
        return nit;
    }
    public void setNit(String nit) {
        this.nit = nit;
    }
    public String getSector() {
        return sector;
    }
    public void setSector(String sector) {
        this.sector = sector;
    }
    public String getContacto() {
        return contacto;
    }
    public void setContacto(String contacto) {
        this.contacto = contacto;
    }
    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public boolean isActivo() {
        return activo;
    }
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    
}
