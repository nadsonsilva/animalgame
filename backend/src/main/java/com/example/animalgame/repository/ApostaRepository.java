package com.example.animalgame.repository;

import com.example.animalgame.model.Aposta;
import com.example.animalgame.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApostaRepository extends JpaRepository<Aposta, Long> {
    List<Aposta> findByUsuarioOrderByDataHoraDesc(Usuario usuario);
}