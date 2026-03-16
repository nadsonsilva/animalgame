package com.example.animalgame.repository;

import com.example.animalgame.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnimalRepository extends JpaRepository<Animal, Long> {
    Optional<Animal> findByGrupo(Integer grupo);
}