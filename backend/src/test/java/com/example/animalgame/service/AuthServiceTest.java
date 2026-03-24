package com.example.animalgame.service;

import com.example.animalgame.dto.LoginRequestDTO;
import com.example.animalgame.dto.LoginResponseDTO;
import com.example.animalgame.exception.RegraNegocioException;
import com.example.animalgame.model.Usuario;
import com.example.animalgame.repository.UsuarioRepository;
import com.example.animalgame.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void deveRealizarLoginComSucesso() throws Exception {
        Usuario usuario = new Usuario();

        Field field = Usuario.class.getDeclaredField("id");
        field.setAccessible(true);
        field.set(usuario, 1L);

        usuario.setNome("Nadson");
        usuario.setEmail("nadson@email.com");
        usuario.setSenha("senha-criptografada");
        usuario.setSaldo(100.0);

        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("nadson@email.com");
        request.setSenha("123456");

        when(usuarioRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(request.getSenha(), usuario.getSenha())).thenReturn(true);
        when(jwtService.gerarToken(usuario.getEmail())).thenReturn("token-fake");

        LoginResponseDTO response = authService.login(request);

        assertNotNull(response);
        assertEquals("token-fake", response.getToken());
        assertEquals("Bearer", response.getTipo());
        assertEquals(1L, response.getUsuarioId());
        assertEquals("Nadson", response.getNome());
        assertEquals("nadson@email.com", response.getEmail());
    }

    @Test
    void deveLancarExcecaoQuandoEmailNaoExistir() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("naoexiste@email.com");
        request.setSenha("123456");

        when(usuarioRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        RegraNegocioException exception = assertThrows(
                RegraNegocioException.class,
                () -> authService.login(request)
        );

        assertEquals("Email ou senha inválidos", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoSenhaEstiverErrada() throws Exception {
        Usuario usuario = new Usuario();

        Field field = Usuario.class.getDeclaredField("id");
        field.setAccessible(true);
        field.set(usuario, 1L);

        usuario.setNome("Nadson");
        usuario.setEmail("nadson@email.com");
        usuario.setSenha("senha-criptografada");

        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("nadson@email.com");
        request.setSenha("senha-errada");

        when(usuarioRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(request.getSenha(), usuario.getSenha())).thenReturn(false);

        RegraNegocioException exception = assertThrows(
                RegraNegocioException.class,
                () -> authService.login(request)
        );

        assertEquals("Email ou senha inválidos", exception.getMessage());
    }
}