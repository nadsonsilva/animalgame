package com.example.animalgame.service;

import com.example.animalgame.dto.ApostaResponseDTO;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
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
    void deveRegistrarApostaComSucesso() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        when(sorteioService.sortearCincoMilhares())
                .thenReturn(new String[]{"0001", "0005", "0009", "0013", "0017"});

        when(sorteioService.obterGrupoPorMilhar("0001")).thenReturn(1);

        when(animalRepository.findByGrupo(1)).thenReturn(Optional.of(animal));

        when(sorteioService.calcularPremio(anyDouble())).thenReturn(20.0);

        Aposta apostaSalva = new Aposta();
        apostaSalva.setUsuario(usuario);
        apostaSalva.setAnimal(animal);
        apostaSalva.setValor(10.0);
        apostaSalva.setVencedora(true);
        apostaSalva.setPremio(20.0);

        when(apostaRepository.save(any(Aposta.class))).thenReturn(apostaSalva);

        ApostaResponseDTO resultado = apostaService.registrarApostaComResumo(1L, 1, 10.0);

        assertNotNull(resultado);
        verify(apostaRepository, times(1)).save(any(Aposta.class));
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

        Animal animalSorteado = new Animal();
        animalSorteado.setGrupo(2);
        animalSorteado.setNome("Águia");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        when(sorteioService.sortearCincoMilhares())
                .thenReturn(new String[]{"0005", "0009", "0013", "0017", "0021"});

        when(sorteioService.obterGrupoPorMilhar("0005")).thenReturn(2);
        when(sorteioService.obterGrupoPorMilhar("0005")).thenReturn(2);

        when(animalRepository.findByGrupo(1)).thenReturn(Optional.of(animal));
        when(animalRepository.findByGrupo(2)).thenReturn(Optional.of(animalSorteado));

        Aposta apostaSalva = new Aposta();
        apostaSalva.setUsuario(usuario);
        apostaSalva.setAnimal(animal);
        apostaSalva.setValor(10.0);
        apostaSalva.setVencedora(false);
        apostaSalva.setPremio(0.0);

        when(apostaRepository.save(any(Aposta.class))).thenReturn(apostaSalva);

        apostaService.registrarApostaComResumo(1L, 1, 10.0);

        assertTrue(usuario.getSaldo() >= 0);
    }
}