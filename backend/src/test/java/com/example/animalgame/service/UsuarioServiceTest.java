package com.example.animalgame.service;

import com.example.animalgame.exception.RegraNegocioException;
import com.example.animalgame.model.Usuario;
import com.example.animalgame.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setNome("Nadson");
        usuario.setEmail("nadson@email.com");
        usuario.setSenha("123456");
    }

    @Test
    void deveCadastrarUsuarioComSucesso() {
        when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(false);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario resultado = usuarioService.cadastrar(usuario);

        assertNotNull(resultado);
        assertEquals("Nadson", resultado.getNome());
        assertEquals("nadson@email.com", resultado.getEmail());

        verify(usuarioRepository, times(1)).existsByEmail(usuario.getEmail());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void naoDeveCadastrarUsuarioComEmailDuplicado() {
        when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(true);

        RegraNegocioException exception = assertThrows(
                RegraNegocioException.class,
                () -> usuarioService.cadastrar(usuario)
        );

        assertEquals("E-mail já cadastrado", exception.getMessage());

        verify(usuarioRepository, times(1)).existsByEmail(usuario.getEmail());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void deveListarUsuarios() {
        List<Usuario> usuarios = List.of(usuario);

        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<Usuario> resultado = usuarioService.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Nadson", resultado.get(0).getNome());

        verify(usuarioRepository, times(1)).findAll();
    }
}