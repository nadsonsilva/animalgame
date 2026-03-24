package com.example.animalgame.controller;

import com.example.animalgame.dto.UsuarioRequestDTO;
import com.example.animalgame.model.Usuario;
import com.example.animalgame.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<Usuario> cadastrar(@Valid @RequestBody UsuarioRequestDTO request) {
        Usuario salvo = usuarioService.cadastrar(request);
        return ResponseEntity.ok(salvo);
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }
}