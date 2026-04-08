package com.example.animalgame.controller;

import com.example.animalgame.dto.UsuarioRequestDTO;
import com.example.animalgame.dto.UsuarioResponseDTO;
import com.example.animalgame.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.animalgame.dto.DepositoRequestDTO;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }


    @PostMapping("/{id}/depositar")
    public ResponseEntity<UsuarioResponseDTO> depositar(
            @PathVariable Long id,
            @Valid @RequestBody DepositoRequestDTO request
    ) {
        return ResponseEntity.ok(usuarioService.depositar(id, request.getValor()));
    }
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@Valid @RequestBody UsuarioRequestDTO request) {
        UsuarioResponseDTO salvo = usuarioService.cadastrar(request);
        return ResponseEntity.ok(salvo);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }
}