package com.example.animalgame.service;

import com.example.animalgame.exception.RegraNegocioException;
import com.example.animalgame.model.Animal;
import com.example.animalgame.model.Aposta;
import com.example.animalgame.model.Usuario;
import com.example.animalgame.repository.AnimalRepository;
import com.example.animalgame.repository.ApostaRepository;
import com.example.animalgame.repository.UsuarioRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApostaService {

    private final ApostaRepository apostaRepository;
    private final UsuarioRepository usuarioRepository;
    private final AnimalRepository animalRepository;
    private final SorteioService sorteioService;

    public ApostaService(
            ApostaRepository apostaRepository,
            UsuarioRepository usuarioRepository,
            AnimalRepository animalRepository,
            SorteioService sorteioService) {

        this.apostaRepository = apostaRepository;
        this.usuarioRepository = usuarioRepository;
        this.animalRepository = animalRepository;
        this.sorteioService = sorteioService;
    }

    @Transactional
    public Aposta registrarAposta(Long usuarioId, Integer grupoAnimal, Double valor) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado"));

        Animal animal = animalRepository.findByGrupo(grupoAnimal)
                .orElseThrow(() -> new RegraNegocioException("Animal não encontrado"));

        if (valor <= 0) {
            throw new RegraNegocioException("O valor da aposta deve ser maior que zero");
        }

        if (usuario.getSaldo() < valor) {
            throw new RegraNegocioException("Saldo insuficiente");
        }

        double novoSaldo = usuario.getSaldo() - valor;

        if (novoSaldo < 0) {
            throw new RegraNegocioException("Saldo não pode ficar negativo");
        }

        usuario.setSaldo(novoSaldo);
        usuarioRepository.save(usuario);

        int grupoSorteado = sorteioService.sortearGrupo();

        boolean venceu = grupoAnimal.equals(grupoSorteado);

        double premio = venceu ? sorteioService.calcularPremio(valor) : 0.0;

        if (venceu) {
            usuario.setSaldo(usuario.getSaldo() + premio);
            usuarioRepository.save(usuario);
        }

        Aposta aposta = new Aposta();
        aposta.setUsuario(usuario);
        aposta.setAnimal(animal);
        aposta.setValor(valor);
        aposta.setDataHora(LocalDateTime.now());
        aposta.setVencedora(venceu);
        aposta.setPremio(premio);

        return apostaRepository.save(aposta);
    }

    public List<Aposta> listarHistorico(Long usuarioId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado"));

        return apostaRepository.findByUsuario(usuario);
    }

    public List<Aposta> listarTodas() {
        return apostaRepository.findAll();
    }
}