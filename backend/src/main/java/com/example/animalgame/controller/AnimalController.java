package com.example.animalgame.controller;

import com.example.animalgame.model.Animal;
import com.example.animalgame.repository.AnimalRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/animais")
@CrossOrigin(origins = "*")
public class AnimalController {

    private final AnimalRepository animalRepository;

    public AnimalController(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    @GetMapping
    public ResponseEntity<List<Animal>> listar() {
        return ResponseEntity.ok(animalRepository.findAll());
    }
}