package com.example.animalgame.service;

import com.example.animalgame.dto.ApostaResponseDTO;
import com.example.animalgame.dto.SorteioResponseDTO;
import com.example.animalgame.exception.RegraNegocioException;
import com.example.animalgame.model.Animal;
import com.example.animalgame.model.Aposta;
import com.example.animalgame.model.Usuario;
import com.example.animalgame.repository.AnimalRepository;
import com.example.animalgame.repository.ApostaRepository;
import com.example.animalgame.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApostaServiceTest {

    @Mock
    private ApostaRepository apostaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private SorteioService sorteioService;

    @InjectMocks
    private ApostaService apostaService;

    private Usuario usuario;
    private Animal animal;

    @BeforeEach
    void setup() {
        usuario = new Usuario();
        usuario.setNome("Nadson");
        usuario.setEmail("nadson@email.com");
        usuario.setSenha("123");
        usuario.setSaldo(100.0);

        animal = new Animal();
        animal.setGrupo(1);
        animal.setNome("Avestruz");
    }

    @Test
    void deveRegistrarApostaComSucessoComoPendente() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(animalRepository.findByGrupo(1)).thenReturn(Optional.of(animal));
        when(apostaRepository.save(any(Aposta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ApostaResponseDTO resultado = apostaService.registrarApostaComResumo(1L, 1, 10.0);

        assertNotNull(resultado);
        assertEquals("PENDENTE", resultado.getStatus());
        assertEquals(90.0, usuario.getSaldo());
        verify(apostaRepository, times(1)).save(any(Aposta.class));
        verify(sorteioService, never()).sortearCincoMilhares();
    }

    @Test
    void naoDevePermitirSaldoInsuficiente() {
        usuario.setSaldo(5.0);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        assertThrows(RegraNegocioException.class, () -> {
            apostaService.registrarApostaComResumo(1L, 1, 10.0);
        });

        verify(apostaRepository, never()).save(any(Aposta.class));
    }

    @Test
    void saldoNuncaDeveFicarNegativo() {
        usuario.setSaldo(10.0);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(animalRepository.findByGrupo(1)).thenReturn(Optional.of(animal));
        when(apostaRepository.save(any(Aposta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        apostaService.registrarApostaComResumo(1L, 1, 10.0);

        assertTrue(usuario.getSaldo() >= 0);
    }

    @Test
    void deveProcessarApostasPendentesAoSimularSorteioDoUsuarioLogado() {
        Aposta aposta = new Aposta();
        aposta.setUsuario(usuario);
        aposta.setAnimal(animal);
        aposta.setValor(10.0);
        aposta.setVencedora(false);
        aposta.setPremio(0.0);
        aposta.setTipoAposta("GRUPO");
        aposta.setStatus("PENDENTE");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(apostaRepository.findByUsuarioAndStatusOrderByDataHoraAsc(usuario, "PENDENTE"))
                .thenReturn(List.of(aposta));
        when(sorteioService.sortearCincoMilhares())
                .thenReturn(new String[]{"0001", "0005", "0009", "0013", "0017"});
        when(sorteioService.obterDezena("0001")).thenReturn("01");
        when(sorteioService.obterDezena("0005")).thenReturn("05");
        when(sorteioService.obterDezena("0009")).thenReturn("09");
        when(sorteioService.obterDezena("0013")).thenReturn("13");
        when(sorteioService.obterDezena("0017")).thenReturn("17");
        when(sorteioService.obterGrupoPorMilhar("0001")).thenReturn(1);
        when(sorteioService.obterGrupoPorMilhar("0005")).thenReturn(2);
        when(sorteioService.obterGrupoPorMilhar("0009")).thenReturn(3);
        when(sorteioService.obterGrupoPorMilhar("0013")).thenReturn(4);
        when(sorteioService.obterGrupoPorMilhar("0017")).thenReturn(5);
        when(sorteioService.calcularPremio(10.0)).thenReturn(180.0);
        when(animalRepository.findByGrupo(1)).thenReturn(Optional.of(animal));
        when(apostaRepository.save(any(Aposta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SorteioResponseDTO resultado = apostaService.simularSorteioParaUsuario(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.getApostasProcessadas().size());
        assertEquals("FINALIZADA", aposta.getStatus());
        assertTrue(aposta.getVencedora());
        assertEquals(180.0, aposta.getPremio());
    }
}
