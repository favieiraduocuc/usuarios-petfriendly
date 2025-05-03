package com.petfriendly.pet.controller;

import com.petfriendly.DTO.UsuarioDTO;
import com.petfriendly.pet.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> listar() {
        List<Map<String, Object>> usuariosConLinks = new ArrayList<>();

        usuarioService.obtenerTodos().forEach(usuario -> {
            Map<String, Object> usuarioMap = new HashMap<>();
            usuarioMap.put("usuario", usuario);
            usuarioMap.put("_links", crearLinksGenerales());
            usuariosConLinks.add(usuarioMap);
        });

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Lista de usuarios obtenida correctamente.");
        response.put("usuarios", usuariosConLinks);
        response.put("_links", crearLinksGenerales());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> buscarPorId(@PathVariable Long id) {
        UsuarioDTO usuario = usuarioService.obtenerPorId(id);

        Map<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("usuario", usuario);
        usuarioMap.put("_links", crearLinksGenerales());

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Usuario encontrado con éxito.");
        response.put("detalle", usuarioMap);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody UsuarioDTO dto) {
        UsuarioDTO nuevo = usuarioService.crear(dto);

        Map<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("usuario", nuevo);
        usuarioMap.put("_links", crearLinksGenerales());

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Usuario creado exitosamente.");
        response.put("detalle", usuarioMap);

        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioDTO dto) {
        UsuarioDTO actualizado = usuarioService.actualizar(id, dto);

        Map<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("usuario", actualizado);
        usuarioMap.put("_links", crearLinksGenerales());

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Usuario actualizado correctamente.");
        response.put("detalle", usuarioMap);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Usuario eliminado correctamente.");
        response.put("_links", crearLinksGenerales());

        return ResponseEntity.ok(response);
    }

    private Map<String, String> crearLinksGenerales() {
        Map<String, String> links = new HashMap<>();
        links.put("listar-usuarios", linkTo(methodOn(UsuarioController.class).listar()).withRel("listar").getHref());
        return links;
    }
}
