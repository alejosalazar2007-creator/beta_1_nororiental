package com.example.beta_1_nororiental.Service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.beta_1_nororiental.DTO.RetoRequestDTO;
import com.example.beta_1_nororiental.DTO.RetoResponseDTO;
import com.example.beta_1_nororiental.models.Categoria;
import com.example.beta_1_nororiental.models.Empresa;
import com.example.beta_1_nororiental.models.Prioridad;
import com.example.beta_1_nororiental.models.Reto;
import com.example.beta_1_nororiental.Repository.ICategoriaRepository;
import com.example.beta_1_nororiental.Repository.IEmpresaRepository;
import com.example.beta_1_nororiental.Repository.IPrioridadRepository;
import com.example.beta_1_nororiental.Repository.IRepositorioReto;

// 1. Interfaz (SIN la palabra 'public' para que viva en el mismo archivo)
interface IRetoServicio {
    RetoResponseDTO crear(RetoRequestDTO dto);
    List<RetoResponseDTO> listar();
    RetoResponseDTO buscarPorId(UUID id);
    RetoResponseDTO actualizar(UUID id, RetoRequestDTO dto);
    void eliminar(UUID id);
}

// 2. Clase Principal (debe coincidir con el nombre de tu archivo .java)
@Service
public class RetoServicioImpl implements IRetoServicio {

    // Inyección por constructor (campos private final sin @Autowired)
    private final IRepositorioReto repositorioReto;
    private final IEmpresaRepository repositorioEmpresa;
    private final ICategoriaRepository repositorioCategoria;
    private final IPrioridadRepository repositorioPrioridad;

    public RetoServicioImpl(IRepositorioReto repositorioReto,
                            IEmpresaRepository repositorioEmpresa,
                            ICategoriaRepository repositorioCategoria,
                            IPrioridadRepository repositorioPrioridad) {
        this.repositorioReto = repositorioReto;
        this.repositorioEmpresa = repositorioEmpresa;
        this.repositorioCategoria = repositorioCategoria;
        this.repositorioPrioridad = repositorioPrioridad;
    }

    @Override
    public RetoResponseDTO crear(RetoRequestDTO dto) {
        // Validar reglas de negocio
        validarReglasDeNegocio(dto);

        // Validar existencia de entidades relacionadas
        Empresa empresa = repositorioEmpresa.findById(dto.getEmpresaId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa no encontrada"));

        Categoria categoria = repositorioCategoria.findById(dto.getCategoriaId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada"));

        Prioridad prioridad = repositorioPrioridad.findById(dto.getPrioridadId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prioridad no encontrada"));

        // Conversión DTO -> Entidad
        Reto reto = dto.toEntity(empresa, categoria, prioridad);

        // Guardar y retornar ResponseDTO
        Reto guardado = repositorioReto.save(reto);
        return RetoResponseDTO.fromEntity(guardado);
    }

    @Override
    public List<RetoResponseDTO> listar() {
        return repositorioReto.findAll()
                .stream()
                .map(RetoResponseDTO::fromEntity)
                .toList();
    }

    @Override
    public RetoResponseDTO buscarPorId(UUID id) {
        Reto reto = repositorioReto.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reto no encontrado"));

        return RetoResponseDTO.fromEntity(reto);
    }

    @Override
    public RetoResponseDTO actualizar(UUID id, RetoRequestDTO dto) {
        Reto retoExistente = repositorioReto.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reto no encontrado"));

        validarReglasDeNegocio(dto);

        Empresa empresa = repositorioEmpresa.findById(dto.getEmpresaId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa no encontrada"));

        Categoria categoria = repositorioCategoria.findById(dto.getCategoriaId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada"));

        Prioridad prioridad = repositorioPrioridad.findById(dto.getPrioridadId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prioridad no encontrada"));

        retoExistente.setNombre(dto.getNombre());
        retoExistente.setFechaInicio(dto.getFechaInicio());
        retoExistente.setFechaFin(dto.getFechaFin());
        retoExistente.setEstado(dto.getEstado());
        retoExistente.setEmpresa(empresa);
        retoExistente.setCategoria(categoria);
        retoExistente.setPrioridad(prioridad);

        Reto actualizado = repositorioReto.save(retoExistente);
        return RetoResponseDTO.fromEntity(actualizado);
    }

    @Override
    public void eliminar(UUID id) {
        Reto reto = repositorioReto.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reto no encontrado"));

        repositorioReto.delete(reto);
    }

    // Reglas de negocio obligatorias
    private void validarReglasDeNegocio(RetoRequestDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().trim().length() < 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre es obligatorio y debe tener al menos 5 caracteres");
        }

        if (dto.getFechaFin() != null && dto.getFechaInicio() != null && !dto.getFechaFin().isAfter(dto.getFechaInicio())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha de fin debe ser posterior a la fecha de inicio");
        }

        if (dto.getEstado() == null || 
           (!dto.getEstado().equals("PROPUESTO") && 
            !dto.getEstado().equals("EN_CURSO") && 
            !dto.getEstado().equals("CERRADO"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El estado solo admite: PROPUESTO, EN_CURSO o CERRADO");
        }
    }
}