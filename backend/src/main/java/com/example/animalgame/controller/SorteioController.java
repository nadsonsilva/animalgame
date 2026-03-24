package com.example.animalgame.controller;

import com.example.animalgame.dto.SorteioResponseDTO;
import com.example.animalgame.service.SorteioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sorteio")
@CrossOrigin(origins = "*")
public class SorteioController {

    private final SorteioService sorteioService;

    public SorteioController(SorteioService sorteioService) {
        this.sorteioService = sorteioService;
    }

    @PostMapping
    public ResponseEntity<SorteioResponseDTO> sortear() {
        Integer grupoSorteado = sorteioService.sortearGrupo();

        SorteioResponseDTO response = new SorteioResponseDTO(
                grupoSorteado,
                "Sorteio realizado com sucesso"
        );

        return ResponseEntity.ok(response);
    }
}