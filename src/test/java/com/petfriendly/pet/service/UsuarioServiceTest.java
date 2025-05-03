package com.petfriendly.pet.service;

import com.petfriendly.DTO.UsuarioDTO;
import com.petfriendly.pet.entity.Usuario;
import com.petfriendly.pet.exception.RecursoNoEncontradoException;
import com.petfriendly.pet.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Debe obtener todos los usuarios")
    void obtenerTodosTest() {
        Usuario usuario = Usuario.builder()
                .idUsuario(1L)
                .nombre("Fabian Vieira")
                .email("fabian@example.com")
                .password("123456") // cumple mínimo de 6 caracteres
                .rol("Dueño de mascota")
                .build();

        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario));

        List<UsuarioDTO> usuarios = usuarioService.obtenerTodos();

        assertEquals(1, usuarios.size());
        assertEquals("Fabian Vieira", usuarios.get(0).getNombre());
    }

    @Test
    @DisplayName("Debe obtener un usuario por ID")
    void obtenerPorIdTest() {
        Usuario usuario = Usuario.builder()
                .idUsuario(1L)
                .nombre("Ana Torres")
                .email("ana@example.com")
                .password("543210") // válido
                .rol("Conductor")
                .build();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        UsuarioDTO resultado = usuarioService.obtenerPorId(1L);

        assertEquals("Ana Torres", resultado.getNombre());
        assertEquals("Conductor", resultado.getRol());
    }

    @Test
    @DisplayName("Debe lanzar excepción al no encontrar usuario por ID")
    void obtenerPorIdExcepcionTest() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> usuarioService.obtenerPorId(1L));
    }

    @Test
    @DisplayName("Debe crear un nuevo usuario con datos válidos")
    void crearUsuarioTest() {
        UsuarioDTO nuevoUsuario = new UsuarioDTO(
                "Carlos Ruiz",
                "carlos@example.com",
                "segura123",            // válido
                "Dueño de mascota"
        );

        Usuario usuarioGuardado = Usuario.builder()
                .idUsuario(1L)
                .nombre("Carlos Ruiz")
                .email("carlos@example.com")
                .password("segura123")
                .rol("Dueño de mascota")
                .build();

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioGuardado);

        UsuarioDTO resultado = usuarioService.crear(nuevoUsuario);

        assertNotNull(resultado);
        assertEquals("Carlos Ruiz", resultado.getNombre());
        assertEquals("carlos@example.com", resultado.getEmail());
        assertEquals("Dueño de mascota", resultado.getRol());
    }

    @Test
    @DisplayName("Debe actualizar un usuario existente")
    void actualizarUsuarioTest() {
        Usuario usuarioExistente = Usuario.builder()
                .idUsuario(1L)
                .nombre("Usuario Antiguo")
                .email("antiguo@example.com")
                .password("antigua123")
                .rol("Dueño de mascota")
                .build();

        UsuarioDTO datosActualizados = new UsuarioDTO(
                "Usuario Actualizado",
                "nuevo@example.com",
                "nuevoPass123",
                "Conductor"
        );

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioExistente);

        UsuarioDTO resultado = usuarioService.actualizar(1L, datosActualizados);

        assertEquals("Usuario Actualizado", resultado.getNombre());
        assertEquals("nuevo@example.com", resultado.getEmail());
        assertEquals("Conductor", resultado.getRol());
    }

    @Test
    @DisplayName("Debe eliminar un usuario existente")
    void eliminarUsuarioTest() {
        Usuario usuarioExistente = Usuario.builder()
                .idUsuario(1L)
                .nombre("A eliminar")
                .email("eliminar@example.com")
                .password("clave456")
                .rol("Dueño de mascota")
                .build();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExistente));
        doNothing().when(usuarioRepository).delete(usuarioExistente);

        assertDoesNotThrow(() -> usuarioService.eliminar(1L));

        verify(usuarioRepository, times(1)).delete(usuarioExistente);
    }
}
