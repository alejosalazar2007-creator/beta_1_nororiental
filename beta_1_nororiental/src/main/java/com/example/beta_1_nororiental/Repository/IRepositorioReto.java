package com.example.beta_1_nororiental.Repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.beta_1_nororiental.models.Reto;

@Repository 
public interface IRepositorioReto extends JpaRepository<Reto, UUID> {

}
