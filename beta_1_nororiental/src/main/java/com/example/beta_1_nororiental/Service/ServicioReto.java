package com.example.beta_1_nororiental.Service;

package services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.BIZTRACK2.models.Reto;
import com.example.BIZTRACK2.repository.IRepositorioReto;

@Service
public class ServicioReto {
    // inyecto una dependencia hacia el repositorio
    @Autowired
    private IRepositorioReto repositorioReto;

    // operaciones que habilitamos ejecutar en nuestra tabla

    // Guardar
    public Reto guardarreto(Reto datosReto) {

        return this.repositorioReto.save(datosReto);

    }

    // Buscar
    public List<Reto> buscar() {
        return this.repositorioReto.findAll();
    }

    // Actualizar
    public Reto modificar(UUID id, Reto datosNuevos) {

        Optional<Reto> retoBuscado = this.repositorioReto.findById(id);
        if (retoBuscado.isPresent()) {
            // Hay a quien actualizar
            Reto retoEncontrado = retoBuscado.get();

            // Modificando los datos
            retoEncontrado.setNombre(datosNuevos.getNombre());
            retoEncontrado.setFechaInicio(datosNuevos.getFechaInicio());
            retoEncontrado.setFechaFin(datosNuevos.getFechaFin());
            retoEncontrado.setEstado(datosNuevos.getEstado());

            // Guardo los cambios
            return this.repositorioReto.save(retoEncontrado);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reto no encontrado");
        }
    }

    // Eliminar
    public boolean eliminar(UUID id) {
        Optional<Reto> retoBuscado = this.repositorioReto.findById(id);
        if (retoBuscado.isPresent()) {
            this.repositorioReto.deleteById(id);
            return true;

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro el reto");
        }

    }
}