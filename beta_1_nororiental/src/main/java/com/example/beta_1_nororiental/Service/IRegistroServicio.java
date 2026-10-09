package com.example.beta_1_nororiental.Service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.beta_1_nororiental.DTO.RegistroRequestDTO;
import com.example.beta_1_nororiental.DTO.RegistroResponseDTO;
import com.example.beta_1_nororiental.models.Registro;
import com.example.beta_1_nororiental.models.Reto;
import com.example.beta_1_nororiental.models.Usuario;
import com.example.beta_1_nororiental.Repository.IRegistroRepository;
import com.example.beta_1_nororiental.Repository.IRepositorioReto;
import com.example.beta_1_nororiental.Repository.IRepositorioUsuario;

// 1. INTERFAZ PÚBLICA (Coincide exactamente con el nombre del archivo IRegistroServicio.java)
public interface IRegistroServicio {
    RegistroResponseDTO crear(RegistroRequestDTO dto);
    List<RegistroResponseDTO> listar();
    RegistroResponseDTO buscarPorId(UUID id);
    RegistroResponseDTO actualizar(UUID id, RegistroRequestDTO dto);
    void eliminar(UUID id);
}

// 2. CLASE DE SERVICIO (Sin la palabra 'public' para estar dentro del mismo archivo)
@Service
class RegistroServicioImpl implements IRegistroServicio {

    // Campos private final (Inyección por constructor, sin @Autowired en los campos)
    private final IRegistroRepository registroRepository;
    private final IRepositorioUsuario usuarioRepository;
    private final IRepositorioReto retoRepository;

    // Constructor para inyección de dependencias
    public RegistroServicioImpl(IRegistroRepository registroRepository,
                                IRepositorioUsuario usuarioRepository,
                                IRepositorioReto retoRepository) {
        this.registroRepository = registroRepository;
        this.usuarioRepository = usuarioRepository;
        this.retoRepository = retoRepository;
    }

    @Override
    public RegistroResponseDTO crear(RegistroRequestDTO dto) {
        // Validar reglas de negocio básicas
        validarReglasDeNegocio(dto);

        // Verificar que el usuario y el reto existan (404 Not Found si no existen)
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        Reto reto = retoRepository.findById(dto.getRetoId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reto no encontrado"));

        // Regla: No permitir el mismo par usuario+reto dos veces (409 Conflict)
        if (registroRepository.existsByUsuarioAndReto(usuario, reto)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario ya se encuentra registrado en este reto");
        }

        // Convertir DTO a Entidad mediante métodos POO
        Registro registro = dto.toEntity(usuario, reto);

        // Guardar y retornar DTO de respuesta
        Registro guardado = registroRepository.save(registro);
        return RegistroResponseDTO.fromEntity(guardado);
    }

    @Override
    public List<RegistroResponseDTO> listar() {
        return registroRepository.findAll()
                .stream()
                .map(RegistroResponseDTO::fromEntity)
                .toList();
    }

    @Override
    public RegistroResponseDTO buscarPorId(UUID id) {
        Registro registro = registroRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro no encontrado"));

        return RegistroResponseDTO.fromEntity(registro);
    }

    @Override
    public RegistroResponseDTO actualizar(UUID id, RegistroRequestDTO dto) {
        // Verificar existencia del registro a actualizar
        Registro registroExistente = registroRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro no encontrado"));

        // Validar reglas de negocio
        validarReglasDeNegocio(dto);

        // Verificar entidades relacionadas
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        Reto reto = retoRepository.findById(dto.getRetoId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reto no encontrado"));

        // Si cambia usuario o reto, validar que el nuevo par no exista previamente
        if ((!registroExistente.getUsuario().getId().equals(usuario.getId()) || 
             !registroExistente.getReto().getId().equals(reto.getId())) &&
            registroRepository.existsByUsuarioAndReto(usuario, reto)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario ya se encuentra registrado en este reto");
        }

        // Actualizar valores de la entidad existente
        registroExistente.setUsuario(usuario);
        registroExistente.setReto(reto);
        registroExistente.setEstado(dto.getEstado());
        registroExistente.setObservacion(dto.getObservacion());

        Registro actualizado = registroRepository.save(registroExistente);
        return RegistroResponseDTO.fromEntity(actualizado);
    }

    @Override
    public void eliminar(UUID id) {
        Registro registro = registroRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro no encontrado"));

        registroRepository.delete(registro);
    }

    // Método privado para validar reglas de negocio (Lanza 400 Bad Request)
    private void validarReglasDeNegocio(RegistroRequestDTO dto) {
        // Regla: estado solo admite INSCRITO, EN_PROCESO, FINALIZADO, CANCELADO
        if (dto.getEstado() == null || 
           (!dto.getEstado().equals("INSCRITO") && 
            !dto.getEstado().equals("EN_PROCESO") && 
            !dto.getEstado().equals("FINALIZADO") && 
            !dto.getEstado().equals("CANCELADO"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El estado sólo admite: INSCRITO, EN_PROCESO, FINALIZADO, CANCELADO");
        }

        // Regla: observacion no supera los 300 caracteres
        if (dto.getObservacion() != null && dto.getObservacion().length() > 300) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La observación no puede superar los 300 caracteres");
        }
    }
}