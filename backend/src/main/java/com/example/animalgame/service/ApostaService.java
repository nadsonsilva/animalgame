package com.example.animalgame.service;

import com.example.animalgame.dto.ApostaHistoricoResponseDTO;
import com.example.animalgame.dto.ApostaResponseDTO;
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
import java.util.stream.Collectors;

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

    /**
     * Método público usado internamente caso precise salvar apenas a entidade aposta
     */
    @Transactional
    public Aposta registrarAposta(Long usuarioId, Integer grupoAnimal, Double valor) {
        return processarAposta(usuarioId, grupoAnimal, valor).apostaSalva;
    }

    /**
     * Método principal usado pelo controller (retorna resumo da aposta)
     */
    @Transactional
    public ApostaResponseDTO registrarApostaComResumo(Long usuarioId, Integer grupoAnimal, Double valor) {

        ResultadoAposta resultado = processarAposta(usuarioId, grupoAnimal, valor);

        return new ApostaResponseDTO(
                resultado.animalEscolhido.getGrupo(),
                resultado.animalEscolhido.getNome(),
                resultado.grupoSorteado,
                resultado.animalSorteado.getNome(),
                resultado.venceu,
                resultado.premio,
                resultado.valorApostado
        );
    }

    /**
     * Histórico de apostas por usuário (DTO)
     */
    public List<ApostaHistoricoResponseDTO> listarHistorico(Long usuarioId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado"));

        return apostaRepository.findByUsuario(usuario)
                .stream()
                .map(this::toHistoricoDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lista todas apostas do sistema (DTO)
     */
    public List<ApostaHistoricoResponseDTO> listarTodas() {

        return apostaRepository.findAll()
                .stream()
                .map(this::toHistoricoDTO)
                .collect(Collectors.toList());
    }

    /**
     * Método central da regra de negócio (fluxo único da aposta)
     */
    private ResultadoAposta processarAposta(Long usuarioId, Integer grupoAnimal, Double valor) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado"));

        Animal animalEscolhido = animalRepository.findByGrupo(grupoAnimal)
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

        Animal animalSorteado = animalRepository.findByGrupo(grupoSorteado)
                .orElseThrow(() -> new RegraNegocioException("Animal sorteado não encontrado"));

        boolean venceu = grupoAnimal.equals(grupoSorteado);

        double premio = venceu ? sorteioService.calcularPremio(valor) : 0.0;

        if (venceu) {
            usuario.setSaldo(usuario.getSaldo() + premio);
            usuarioRepository.save(usuario);
        }

        Aposta aposta = new Aposta();
        aposta.setUsuario(usuario);
        aposta.setAnimal(animalEscolhido);
        aposta.setValor(valor);
        aposta.setDataHora(LocalDateTime.now());
        aposta.setVencedora(venceu);
        aposta.setPremio(premio);

        Aposta apostaSalva = apostaRepository.save(aposta);

        return new ResultadoAposta(
                apostaSalva,
                animalEscolhido,
                grupoSorteado,
                animalSorteado,
                venceu,
                premio,
                valor
        );
    }

    /**
     * Conversão entidade → DTO histórico
     */
    private ApostaHistoricoResponseDTO toHistoricoDTO(Aposta aposta) {

        return new ApostaHistoricoResponseDTO(
                aposta.getId(),
                aposta.getUsuario().getId(),
                aposta.getUsuario().getNome(),
                aposta.getAnimal().getGrupo(),
                aposta.getAnimal().getNome(),
                aposta.getValor(),
                aposta.getDataHora(),
                aposta.getVencedora(),
                aposta.getPremio()
        );
    }

    /**
     * Classe interna auxiliar para retornar dados do processamento
     */
    private static class ResultadoAposta {

        private final Aposta apostaSalva;
        private final Animal animalEscolhido;
        private final Integer grupoSorteado;
        private final Animal animalSorteado;
        private final Boolean venceu;
        private final Double premio;
        private final Double valorApostado;

        public ResultadoAposta(
                Aposta apostaSalva,
                Animal animalEscolhido,
                Integer grupoSorteado,
                Animal animalSorteado,
                Boolean venceu,
                Double premio,
                Double valorApostado) {

            this.apostaSalva = apostaSalva;
            this.animalEscolhido = animalEscolhido;
            this.grupoSorteado = grupoSorteado;
            this.animalSorteado = animalSorteado;
            this.venceu = venceu;
            this.premio = premio;
            this.valorApostado = valorApostado;
        }
    }
}