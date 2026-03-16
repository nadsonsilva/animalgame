package com.example.animalgame.controller;

import com.example.animalgame.exception.RegraNegocioException;
import com.example.animalgame.model.Aposta;
import com.example.animalgame.service.ApostaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> registrar(@RequestParam Long usuarioId,
                                       @RequestParam Integer grupoAnimal,
                                       @RequestParam Double valor) {
        try {
            Aposta aposta = apostaService.registrarAposta(usuarioId, grupoAnimal, valor);
            return ResponseEntity.ok(aposta);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Aposta>> listarTodas() {
        return ResponseEntity.ok(apostaService.listarTodas());
    }

    @GetMapping("/historico/{usuarioId}")
    public ResponseEntity<List<Aposta>> historico(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(apostaService.listarHistorico(usuarioId));
    }
}