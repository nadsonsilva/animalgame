package com.example.animalgame.service;

import com.example.animalgame.dto.UsuarioRequestDTO;
import com.example.animalgame.exception.RegraNegocioException;
import com.example.animalgame.model.Usuario;
import com.example.animalgame.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario cadastrar(UsuarioRequestDTO request) {

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RegraNegocioException("E-mail já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenha(request.getSenha());
        usuario.setSaldo(request.getSaldo());

        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }
}