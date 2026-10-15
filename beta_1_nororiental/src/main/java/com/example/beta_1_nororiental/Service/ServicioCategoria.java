package com.example.beta_1_nororiental.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.beta_1_nororiental.Repository.IRepositorioCategoria;
import com.example.beta_1_nororiental.models.Categoria;

@Service 
public class ServicioCategoria {
@Autowired 
private IRepositorioCategoria repositorioCategoria;


    // operaciones que habilitamos ejecutar en nuestra tabla

    //Guardar
    public Categoria guardarCategoria(Categoria categoria){
        return repositorioCategoria.save(categoria);
    }
    //Buscar
    public List<Categoria> buscar(){
        return this.repositorioCategoria.findAll();
    }
    //Actualizar
    public Categoria modificar(UUID id, Categoria datosNuevos){
         Optional<Categoria> CategoriaBuscada=this.repositorioCategoria.findById(id);
        if(CategoriaBuscada.isPresent()){
            //Hay a quien actualizar
            Categoria categoriaEncontrada=CategoriaBuscada.get();

            //Modificando los datos
            categoriaEncontrada.setNombre(datosNuevos.getNombre());
            //Guardo los cambios
            return this.repositorioCategoria.save(categoriaEncontrada);
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Categoria no encontrada");
        }
    }
    //Eliminar
    public boolean eliminar(UUID id){
        Optional<Categoria> categoriaBuscada=this.repositorioCategoria.findById(id);
        if(categoriaBuscada.isPresent()){
            this.repositorioCategoria.deleteById(id);
            return true;

        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST ,"No se encontro la categoria");
        }

    }

    
}

