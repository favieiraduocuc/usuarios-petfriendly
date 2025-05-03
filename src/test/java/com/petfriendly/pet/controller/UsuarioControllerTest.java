package com.petfriendly.pet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petfriendly.DTO.UsuarioDTO;
import com.petfriendly.pet.service.UsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /usuarios - debe retornar lista de usuarios con HATEOAS")
    void listarUsuariosTest() throws Exception {
        UsuarioDTO usuario1 = new UsuarioDTO("Fabian Vieira", "fabian@example.com", "123456", "Dueño de mascota");
        UsuarioDTO usuario2 = new UsuarioDTO("Ana Torres", "ana@example.com", "abcdef", "Conductor");

        when(usuarioService.obtenerTodos()).thenReturn(Arrays.asList(usuario1, usuario2));

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Lista de usuarios obtenida correctamente."))
                .andExpect(jsonPath("$.usuarios").isArray())
                .andExpect(jsonPath("$.usuarios[0].usuario.nombre").value("Fabian Vieira"))
                .andExpect(jsonPath("$.usuarios[0]._links.listar-usuarios").exists());
    }

    @Test
    @DisplayName("GET /usuarios/{id} - debe retornar un usuario con HATEOAS")
    void obtenerUsuarioPorIdTest() throws Exception {
        UsuarioDTO usuario = new UsuarioDTO("Pedro Soto", "pedro@example.com", "qwerty", "Dueño de mascota");

        when(usuarioService.obtenerPorId(1L)).thenReturn(usuario);

        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Usuario encontrado con éxito."))
                .andExpect(jsonPath("$.detalle.usuario.nombre").value("Pedro Soto"))
                .andExpect(jsonPath("$.detalle._links.listar-usuarios").exists());
    }

    @Test
    @DisplayName("POST /usuarios - debe crear un usuario con HATEOAS")
    void crearUsuarioTest() throws Exception {
        UsuarioDTO nuevoUsuario = new UsuarioDTO("Carlos Rojas", "carlos@example.com", "pass123", "Conductor");

        when(usuarioService.crear(Mockito.any(UsuarioDTO.class))).thenReturn(nuevoUsuario);

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoUsuario)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensaje").value("Usuario creado exitosamente."))
                .andExpect(jsonPath("$.detalle.usuario.nombre").value("Carlos Rojas"))
                .andExpect(jsonPath("$.detalle._links.listar-usuarios").exists());
    }

    @Test
    @DisplayName("PUT /usuarios/{id} - debe actualizar un usuario con HATEOAS")
    void actualizarUsuarioTest() throws Exception {
        UsuarioDTO actualizado = new UsuarioDTO("Nombre Actualizado", "nuevo@example.com", "passnuevo", "Dueño de mascota");

        when(usuarioService.actualizar(Mockito.eq(1L), Mockito.any(UsuarioDTO.class))).thenReturn(actualizado);

        mockMvc.perform(put("/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Usuario actualizado correctamente."))
                .andExpect(jsonPath("$.detalle.usuario.nombre").value("Nombre Actualizado"))
                .andExpect(jsonPath("$.detalle._links.listar-usuarios").exists());
    }

    @Test
    @DisplayName("DELETE /usuarios/{id} - debe eliminar un usuario y retornar HATEOAS")
    void eliminarUsuarioTest() throws Exception {
        Mockito.doNothing().when(usuarioService).eliminar(1L);

        mockMvc.perform(delete("/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Usuario eliminado correctamente."))
                .andExpect(jsonPath("$._links.listar-usuarios").exists());
    }
}
