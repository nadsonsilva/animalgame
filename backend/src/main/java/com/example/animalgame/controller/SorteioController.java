package com.example.animalgame.controller;

import com.example.animalgame.dto.SorteioResponseDTO;
import com.example.animalgame.service.ApostaService;
import com.example.animalgame.service.SorteioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sorteio")
@CrossOrigin(origins = "*")
public class SorteioController {

    private final SorteioService sorteioService;
    private final ApostaService apostaService;

    public SorteioController(SorteioService sorteioService, ApostaService apostaService) {
        this.sorteioService = sorteioService;
        this.apostaService = apostaService;
    }

    @PostMapping
    public ResponseEntity<SorteioResponseDTO> sortear(@RequestParam(required = false) Long usuarioId) {
        if (usuarioId != null) {
            return ResponseEntity.ok(apostaService.simularSorteioParaUsuario(usuarioId));
        }
        return ResponseEntity.ok(gerarSorteioCompleto());
    }

    @GetMapping("/simular")
    public ResponseEntity<SorteioResponseDTO> simularSorteio(@RequestParam(required = false) Long usuarioId) {
        if (usuarioId != null) {
            return ResponseEntity.ok(apostaService.simularSorteioParaUsuario(usuarioId));
        }
        return ResponseEntity.ok(gerarSorteioCompleto());
    }

    private SorteioResponseDTO gerarSorteioCompleto() {
        String[] premiosSorteados = sorteioService.sortearCincoMilhares();
        List<String> premios = Arrays.asList(premiosSorteados);

        List<String> dezenas = premios.stream()
                .map(sorteioService::obterDezena)
                .collect(Collectors.toList());

        List<Integer> grupos = premios.stream()
                .map(sorteioService::obterGrupoPorMilhar)
                .collect(Collectors.toList());

        Integer grupoSorteado = grupos.isEmpty() ? null : grupos.get(0);

        return new SorteioResponseDTO(
                grupoSorteado,
                "Sorteio realizado com sucesso",
                premios,
                dezenas,
                grupos
        );
    }
}
