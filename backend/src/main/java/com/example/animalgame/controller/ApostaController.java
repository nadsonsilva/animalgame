package com.example.animalgame.controller;

import com.example.animalgame.dto.ApostaHistoricoResponseDTO;
import com.example.animalgame.dto.ApostaRequestDTO;
import com.example.animalgame.dto.ApostaResponseDTO;
import com.example.animalgame.service.ApostaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/apostas")
@CrossOrigin(origins = "*")
public class ApostaController {

    private final ApostaService apostaService;

    public ApostaController(ApostaService apostaService) {
        this.apostaService = apostaService;
    }

    @PostMapping
    public ResponseEntity<ApostaResponseDTO> registrar(@Valid @RequestBody ApostaRequestDTO request) {
        ApostaResponseDTO resposta = apostaService.registrarApostaComResumo(
                request.getUsuarioId(),
                request.getGrupoAnimal(),
                request.getValor()
        );
        return ResponseEntity.ok(resposta);
    }

    @GetMapping
    public ResponseEntity<List<ApostaHistoricoResponseDTO>> listarTodas() {
        return ResponseEntity.ok(apostaService.listarTodas());
    }

    @GetMapping("/historico/{usuarioId}")
    public ResponseEntity<List<ApostaHistoricoResponseDTO>> historico(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(apostaService.listarHistorico(usuarioId));
    }
}