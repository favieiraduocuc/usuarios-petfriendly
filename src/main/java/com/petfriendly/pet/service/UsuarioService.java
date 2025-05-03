package com.petfriendly.pet.service;

import org.springframework.beans.factory.annotation.Autowired;
//import com.fvieira.usuarios.dto.usuarioDTO;
//import com.fvieira.usuarios.entity.usuario;
//import com.fvieira.usuarios.exception.RecursoNoEncontradoException;
//import com.fvieira.usuarios.repository.usuarioRepository;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petfriendly.DTO.UsuarioDTO;
import com.petfriendly.pet.entity.Usuario;
import com.petfriendly.pet.exception.RecursoNoEncontradoException;
import com.petfriendly.pet.repository.UsuarioRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<UsuarioDTO> obtenerTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public UsuarioDTO obtenerPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con ID: " + id));
        return toDTO(usuario);
    }

    public UsuarioDTO crear(UsuarioDTO dto) {
        Usuario usuario = toEntity(dto);
        return toDTO(usuarioRepository.save(usuario));
    }

    public UsuarioDTO actualizar(Long id, UsuarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con ID: " + id));
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());
        usuario.setRol(dto.getRol());
        return toDTO(usuarioRepository.save(usuario));
    }

    public void eliminar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con ID: " + id));
        usuarioRepository.delete(usuario);
    }
    private UsuarioDTO toDTO(Usuario u) {
        return new UsuarioDTO(u.getNombre(), u.getEmail(), u.getPassword(), u.getRol());
    }

    private Usuario toEntity(UsuarioDTO dto) {
        return Usuario.builder()
                .nombre(dto.getNombre())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .rol(dto.getRol())
                .build();
    }
}