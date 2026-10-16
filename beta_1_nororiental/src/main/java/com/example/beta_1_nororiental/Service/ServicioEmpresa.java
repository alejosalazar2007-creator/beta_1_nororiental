package com.example.beta_1_nororiental.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.beta_1_nororiental.Repository.IRepositorioEmpresa;
import com.example.beta_1_nororiental.models.Empresa;

@Service 
public class ServicioEmpresa {

    @Autowired 
    private IRepositorioEmpresa repositorioEmpresa;
    // operaciones que habilitamos ejecutar en nuestra tabla

    //Guardar
    
    public Empresa guardarEmpresa(Empresa empresa){
        return repositorioEmpresa.save(empresa);
    }
    //Buscar
    public List<Empresa> buscar(){
        return this.repositorioEmpresa.findAll();
    }
    //Actualizar
    public Empresa modificar(UUID id, Empresa datosNuevos){
         Optional<Empresa> empresaBuscada=this.repositorioEmpresa.findById(id);
        if(empresaBuscada.isPresent()){
            //Hay a quien actualizar
            Empresa empresaEncontrada=empresaBuscada.get();

            //Modificando los datos
            empresaEncontrada.setNombre(datosNuevos.getNombre());
            //Guardo los cambios
            return this.repositorioEmpresa.save(empresaEncontrada);
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"empresa no encontrada");
        }
    }
    //Eliminar
    public boolean eliminar(UUID id){
        Optional<Empresa> categoriaBuscada=this.repositorioEmpresa.findById(id);
        if(categoriaBuscada.isPresent()){
            this.repositorioEmpresa.deleteById(id);
            return true;

        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No se encontro la categoria");
        }

    }

}
