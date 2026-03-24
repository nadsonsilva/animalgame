package com.example.animalgame.service;

import com.example.animalgame.dto.UsuarioRequestDTO;
import com.example.animalgame.dto.UsuarioResponseDTO;
import com.example.animalgame.exception.RegraNegocioException;
import com.example.animalgame.model.Usuario;
import com.example.animalgame.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private UsuarioRequestDTO request;

    @BeforeEach
    void setUp() throws Exception {
        usuario = new Usuario();

        Field field = Usuario.class.getDeclaredField("id");
        field.setAccessible(true);
        field.set(usuario, 1L);

        usuario.setNome("Nadson");
        usuario.setEmail("nadson@email.com");
        usuario.setSenha("senha-criptografada");
        usuario.setSaldo(100.0);

        request = new UsuarioRequestDTO();
        request.setNome("Nadson");
        request.setEmail("nadson@email.com");
        request.setSenha("123456");
        request.setSaldo(100.0);
    }

    @Test
    void deveCadastrarUsuarioComSucesso() {
        when(usuarioRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getSenha())).thenReturn("senha-criptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioResponseDTO resultado = usuarioService.cadastrar(request);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Nadson", resultado.getNome());
        assertEquals("nadson@email.com", resultado.getEmail());
        assertEquals(100.0, resultado.getSaldo());

        verify(usuarioRepository, times(1)).existsByEmail(request.getEmail());
        verify(passwordEncoder, times(1)).encode(request.getSenha());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void naoDeveCadastrarUsuarioComEmailDuplicado() {
        when(usuarioRepository.existsByEmail(request.getEmail())).thenReturn(true);

        RegraNegocioException exception = assertThrows(
                RegraNegocioException.class,
                () -> usuarioService.cadastrar(request)
        );

        assertEquals("E-mail já cadastrado", exception.getMessage());

        verify(usuarioRepository, times(1)).existsByEmail(request.getEmail());
        verify(passwordEncoder, never()).encode(anyString());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void deveListarUsuarios() {
        List<Usuario> usuarios = List.of(usuario);

        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<UsuarioResponseDTO> resultado = usuarioService.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Nadson", resultado.get(0).getNome());
        assertEquals("nadson@email.com", resultado.get(0).getEmail());
        assertEquals(100.0, resultado.get(0).getSaldo());

        verify(usuarioRepository, times(1)).findAll();
    }
}