package com.example.beta_1_nororiental.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.beta_1_nororiental.models.Registro;
import com.example.beta_1_nororiental.Repository.IRepositorioRegistro;

@Service
public class ServicioRegistro {

    // Inyecto la dependencia hacia el repositorio
    @Autowired
    private IRepositorioRegistro repositorioRegistro;

    // Operaciones que habilitamos ejecutar en nuestra tabla

    // Guardar
    public Registro guardarregistro(Registro datosRegistro) {
        return this.repositorioRegistro.save(datosRegistro);
    }

    // Buscar
    public List<Registro> buscar() {
        return this.repositorioRegistro.findAll();
    }

    // Actualizar
    public Registro modificar(UUID id, Registro datosNuevos) {

        Optional<Registro> registroBuscado = this.repositorioRegistro.findById(id);
        if (registroBuscado.isPresent()) {
            // Hay a quien actualizar
            Registro registroEncontrado = registroBuscado.get();

            // Modificando los datos
            registroEncontrado.setEstado(datosNuevos.getEstado());
            registroEncontrado.setObservacion(datosNuevos.getObservacion());

            // Guardo los cambios
            return this.repositorioRegistro.save(registroEncontrado);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro no encontrado");
        }
    }

    // Eliminar
    public boolean eliminar(UUID id) {
        Optional<Registro> registroBuscado = this.repositorioRegistro.findById(id);
        if (registroBuscado.isPresent()) {
            this.repositorioRegistro.deleteById(id);
            return true;

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro el registro");
        }
    }
}