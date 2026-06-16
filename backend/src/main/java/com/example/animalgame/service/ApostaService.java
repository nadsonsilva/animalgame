package com.example.animalgame.service;

import com.example.animalgame.dto.ApostaHistoricoResponseDTO;
import com.example.animalgame.dto.ApostaResponseDTO;
import com.example.animalgame.dto.SorteioResponseDTO;
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
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class ApostaService {

    private static final String TIPO_GRUPO = "GRUPO";
    private static final String TIPO_DEZENA = "DEZENA";
    private static final String TIPO_CENTENA = "CENTENA";
    private static final String TIPO_MILHAR = "MILHAR";
    private static final String TIPO_DUQUE_DEZENA = "DUQUE_DE_DEZENA";

    private static final String STATUS_PENDENTE = "PENDENTE";
    private static final String STATUS_FINALIZADA = "FINALIZADA";

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
        return registrarApostaPendente(usuarioId, grupoAnimal, valor, TIPO_GRUPO, null, null);
    }

    @Transactional
    public ApostaResponseDTO registrarApostaComResumo(Long usuarioId, Integer grupoAnimal, Double valor) {
        Aposta aposta = registrarApostaPendente(usuarioId, grupoAnimal, valor, TIPO_GRUPO, null, null);
        return toResponseDTO(aposta, null, null, null, null, "Aposta registrada. Aguarde o sorteio.");
    }

    @Transactional
    public ApostaResponseDTO registrarApostaComResumo(Long usuarioId, Integer grupoAnimal, Double valor,
                                                       String tipoAposta, String numeroApostado,
                                                       String segundoNumero) {
        Aposta aposta = registrarApostaPendente(usuarioId, grupoAnimal, valor, tipoAposta, numeroApostado, segundoNumero);
        return toResponseDTO(aposta, null, null, null, null, "Aposta registrada. Aguarde o sorteio.");
    }

    @Transactional
    public SorteioResponseDTO simularSorteioParaUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado"));

        String[] premiosSorteados = sorteioService.sortearCincoMilhares();
        List<String> premios = Arrays.asList(premiosSorteados);

        List<String> dezenas = premios.stream()
                .map(sorteioService::obterDezena)
                .collect(Collectors.toList());

        List<Integer> grupos = premios.stream()
                .map(sorteioService::obterGrupoPorMilhar)
                .collect(Collectors.toList());

        List<Aposta> apostasPendentes = apostaRepository
                .findByUsuarioAndStatusOrderByDataHoraAsc(usuario, STATUS_PENDENTE);

        List<ApostaResponseDTO> apostasProcessadas = apostasPendentes.stream()
                .map(aposta -> processarResultadoDaAposta(aposta, premiosSorteados))
                .collect(Collectors.toList());

        Integer grupoSorteado = grupos.isEmpty() ? null : grupos.get(0);
        String mensagem = apostasProcessadas.isEmpty()
                ? "Sorteio realizado com sucesso. Nenhuma aposta pendente para este usuário."
                : "Sorteio realizado com sucesso. Apostas pendentes processadas.";

        return new SorteioResponseDTO(
                grupoSorteado,
                mensagem,
                premios,
                dezenas,
                grupos,
                apostasProcessadas
        );
    }

    public List<ApostaHistoricoResponseDTO> listarHistorico(Long usuarioId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado"));

        return apostaRepository.findByUsuarioOrderByDataHoraDesc(usuario)
                .stream()
                .map(this::toHistoricoDTO)
                .collect(Collectors.toList());
    }

    public List<ApostaHistoricoResponseDTO> listarTodas() {

        return apostaRepository.findAll()
                .stream()
                .sorted((a, b) -> b.getDataHora().compareTo(a.getDataHora()))
                .map(this::toHistoricoDTO)
                .collect(Collectors.toList());
    }

    private Aposta registrarApostaPendente(Long usuarioId, Integer grupoAnimal, Double valor,
                                           String tipoAposta, String numeroApostado,
                                           String segundoNumero) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado"));

        String tipo = normalizarTipoAposta(tipoAposta);
        validarValor(valor);

        if (usuario.getSaldo() < valor) {
            throw new RegraNegocioException("Saldo insuficiente");
        }

        String numeroFormatado = formatarNumeroPrincipal(tipo, numeroApostado);
        String segundoNumeroFormatado = formatarSegundoNumero(tipo, segundoNumero);
        Animal animalEscolhido = obterAnimalEscolhido(tipo, grupoAnimal, numeroFormatado);

        usuario.setSaldo(usuario.getSaldo() - valor);

        if (usuario.getSaldo() < 0) {
            throw new RegraNegocioException("Saldo não pode ficar negativo");
        }

        usuarioRepository.save(usuario);

        Aposta aposta = new Aposta();
        aposta.setUsuario(usuario);
        aposta.setAnimal(animalEscolhido);
        aposta.setValor(valor);
        aposta.setDataHora(LocalDateTime.now());
        aposta.setVencedora(false);
        aposta.setPremio(0.0);
        aposta.setTipoAposta(tipo);
        aposta.setStatus(STATUS_PENDENTE);
        aposta.setNumeroApostado(numeroFormatado);
        aposta.setSegundoNumero(segundoNumeroFormatado);
        aposta.setNumeroSorteado(null);

        return apostaRepository.save(aposta);
    }

    private ApostaResponseDTO processarResultadoDaAposta(Aposta aposta, String[] premiosSorteados) {
        ResultadoConferencia resultado = conferirResultado(
                aposta.getTipoAposta(),
                aposta.getAnimal().getGrupo(),
                aposta.getNumeroApostado(),
                aposta.getSegundoNumero(),
                premiosSorteados,
                aposta.getValor()
        );

        int grupoSorteado = resultado.grupoReferencia;

        Animal animalSorteado = animalRepository.findByGrupo(grupoSorteado)
                .orElseThrow(() -> new RegraNegocioException("Animal sorteado não encontrado"));

        aposta.setVencedora(resultado.venceu);
        aposta.setPremio(resultado.premio);
        aposta.setStatus(STATUS_FINALIZADA);
        aposta.setNumeroSorteado(String.join(", ", premiosSorteados));

        if (resultado.venceu) {
            Usuario usuario = aposta.getUsuario();
            usuario.setSaldo(usuario.getSaldo() + resultado.premio);
            usuarioRepository.save(usuario);
        }

        Aposta apostaSalva = apostaRepository.save(aposta);

        return toResponseDTO(
                apostaSalva,
                grupoSorteado,
                animalSorteado,
                resultado.milharReferencia,
                Arrays.asList(premiosSorteados),
                resultado.resultadoComparado
        );
    }

    private ApostaResponseDTO toResponseDTO(Aposta aposta, Integer grupoSorteado, Animal animalSorteado,
                                            String milharSorteada, List<String> premiosSorteados,
                                            String resultadoComparado) {
        return new ApostaResponseDTO(
                aposta.getAnimal().getGrupo(),
                aposta.getAnimal().getNome(),
                grupoSorteado,
                animalSorteado != null ? animalSorteado.getNome() : null,
                Boolean.TRUE.equals(aposta.getVencedora()),
                aposta.getPremio(),
                aposta.getValor(),
                aposta.getTipoAposta(),
                aposta.getNumeroApostado(),
                aposta.getSegundoNumero(),
                milharSorteada,
                premiosSorteados,
                resultadoComparado,
                aposta.getStatus()
        );
    }

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
                aposta.getPremio(),
                aposta.getTipoAposta(),
                aposta.getNumeroApostado(),
                aposta.getSegundoNumero(),
                aposta.getNumeroSorteado(),
                aposta.getStatus()
        );
    }

    private String normalizarTipoAposta(String tipoAposta) {
        if (tipoAposta == null || tipoAposta.trim().isEmpty()) {
            return TIPO_GRUPO;
        }

        String tipo = tipoAposta.trim().toUpperCase(Locale.ROOT)
                .replace(" ", "_")
                .replace("-", "_");

        if ("DUQUE_DEZENA".equals(tipo)) {
            return TIPO_DUQUE_DEZENA;
        }

        if (!TIPO_GRUPO.equals(tipo)
                && !TIPO_DEZENA.equals(tipo)
                && !TIPO_CENTENA.equals(tipo)
                && !TIPO_MILHAR.equals(tipo)
                && !TIPO_DUQUE_DEZENA.equals(tipo)) {
            throw new RegraNegocioException("Tipo de aposta inválido");
        }

        return tipo;
    }

    private void validarValor(Double valor) {
        if (valor == null || valor <= 0) {
            throw new RegraNegocioException("O valor da aposta deve ser maior que zero");
        }
    }

    private String formatarNumeroPrincipal(String tipo, String numeroApostado) {
        if (TIPO_GRUPO.equals(tipo)) {
            return null;
        }

        if (numeroApostado == null || numeroApostado.trim().isEmpty()) {
            throw new RegraNegocioException("Informe o número da aposta");
        }

        String numero = numeroApostado.trim();

        if (!numero.matches("\\d+")) {
            throw new RegraNegocioException("O número da aposta deve conter apenas dígitos");
        }

        if (TIPO_DEZENA.equals(tipo) || TIPO_DUQUE_DEZENA.equals(tipo)) {
            return preencherComZeros(numero, 2, "A dezena deve ter no máximo 2 dígitos");
        }

        if (TIPO_CENTENA.equals(tipo)) {
            return preencherComZeros(numero, 3, "A centena deve ter no máximo 3 dígitos");
        }

        return preencherComZeros(numero, 4, "A milhar deve ter no máximo 4 dígitos");
    }

    private String formatarSegundoNumero(String tipo, String segundoNumero) {
        if (!TIPO_DUQUE_DEZENA.equals(tipo)) {
            return null;
        }

        if (segundoNumero == null || segundoNumero.trim().isEmpty()) {
            throw new RegraNegocioException("Informe a segunda dezena do duque");
        }

        String numero = segundoNumero.trim();

        if (!numero.matches("\\d+")) {
            throw new RegraNegocioException("A segunda dezena deve conter apenas dígitos");
        }

        return preencherComZeros(numero, 2, "A segunda dezena deve ter no máximo 2 dígitos");
    }

    private String preencherComZeros(String numero, int tamanho, String mensagemErro) {
        if (numero.length() > tamanho) {
            throw new RegraNegocioException(mensagemErro);
        }

        return String.format("%" + tamanho + "s", numero).replace(' ', '0');
    }

    private Animal obterAnimalEscolhido(String tipo, Integer grupoAnimal, String numeroFormatado) {
        Integer grupo;

        if (TIPO_GRUPO.equals(tipo)) {
            if (grupoAnimal == null) {
                throw new RegraNegocioException("Grupo do animal é obrigatório");
            }
            grupo = grupoAnimal;
        } else {
            String dezena = numeroFormatado.length() >= 2
                    ? numeroFormatado.substring(numeroFormatado.length() - 2)
                    : numeroFormatado;
            int dezenaInt = Integer.parseInt(dezena);
            grupo = dezenaInt == 0 ? 25 : ((dezenaInt - 1) / 4) + 1;
        }

        return animalRepository.findByGrupo(grupo)
                .orElseThrow(() -> new RegraNegocioException("Animal não encontrado"));
    }

    private boolean premioCorrespondeAoTipo(String tipo, Integer grupoAnimal, String numeroApostado,
                                            String segundoNumero, String premio, String[] premiosSorteados,
                                            int indicePremio) {
        if (TIPO_GRUPO.equals(tipo)) {
            return grupoAnimal.equals(sorteioService.obterGrupoPorMilhar(premio));
        }

        if (TIPO_DEZENA.equals(tipo)) {
            return numeroApostado.equals(sorteioService.obterDezena(premio));
        }

        if (TIPO_CENTENA.equals(tipo)) {
            return numeroApostado.equals(sorteioService.obterCentena(premio));
        }

        if (TIPO_MILHAR.equals(tipo)) {
            return numeroApostado.equals(premio);
        }

        return false;
    }

    private ResultadoConferencia conferirResultado(String tipo, Integer grupoAnimal, String numeroApostado,
                                                 String segundoNumero, String[] premiosSorteados,
                                                 double valorApostado) {
        if (TIPO_DUQUE_DEZENA.equals(tipo)) {
            return conferirDuqueDeDezena(numeroApostado, segundoNumero, premiosSorteados, valorApostado);
        }

        for (int i = 0; i < premiosSorteados.length; i++) {
            String premio = premiosSorteados[i];

            if (premioCorrespondeAoTipo(tipo, grupoAnimal, numeroApostado, segundoNumero, premio, premiosSorteados, i)) {
                boolean cabeca = i == 0;
                double valorPremio = cabeca
                        ? sorteioService.calcularPremioCabeca(valorApostado)
                        : sorteioService.calcularPremioCercado(valorApostado);

                return new ResultadoConferencia(
                        true,
                        valorPremio,
                        premio,
                        sorteioService.obterGrupoPorMilhar(premio),
                        montarResultadoComparado(tipo, premiosSorteados, i, cabeca)
                );
            }
        }

        String primeiroPremio = premiosSorteados[0];

        return new ResultadoConferencia(
                false,
                0.0,
                primeiroPremio,
                sorteioService.obterGrupoPorMilhar(primeiroPremio),
                montarResultadoComparado(tipo, premiosSorteados, -1, false)
        );
    }


    private ResultadoConferencia conferirDuqueDeDezena(String numeroApostado, String segundoNumero,
                                                       String[] premiosSorteados, double valorApostado) {
        List<String> dezenasSorteadas = Arrays.stream(premiosSorteados)
                .map(sorteioService::obterDezena)
                .collect(Collectors.toList());

        String primeiroPremio = premiosSorteados[0];

        if (dezenasSorteadas.contains(numeroApostado) && dezenasSorteadas.contains(segundoNumero)) {
            return new ResultadoConferencia(
                    true,
                    sorteioService.calcularPremioCercado(valorApostado),
                    primeiroPremio,
                    sorteioService.obterGrupoPorMilhar(primeiroPremio),
                    "Cercado: Duque de dezena encontrado nos 5 prêmios"
            );
        }

        return new ResultadoConferencia(
                false,
                0.0,
                primeiroPremio,
                sorteioService.obterGrupoPorMilhar(primeiroPremio),
                montarResultadoComparado(TIPO_DUQUE_DEZENA, premiosSorteados, -1, false)
        );
    }

    private String montarResultadoComparado(String tipo, String[] premiosSorteados, int indiceVencedor, boolean cabeca) {
        if (indiceVencedor >= 0) {
            String premioVencedor = premiosSorteados[indiceVencedor];
            String posicao = (indiceVencedor + 1) + "º prêmio";
            String modalidade = cabeca ? "Cabeça" : "Cercado";

            if (TIPO_GRUPO.equals(tipo)) {
                return modalidade + " (" + posicao + "): Grupo " + sorteioService.obterGrupoPorMilhar(premioVencedor);
            }

            if (TIPO_DEZENA.equals(tipo)) {
                return modalidade + " (" + posicao + "): Dezena " + sorteioService.obterDezena(premioVencedor);
            }

            if (TIPO_CENTENA.equals(tipo)) {
                return modalidade + " (" + posicao + "): Centena " + sorteioService.obterCentena(premioVencedor);
            }

            if (TIPO_MILHAR.equals(tipo)) {
                return modalidade + " (" + posicao + "): Milhar " + premioVencedor;
            }

            return modalidade + ": Duque de dezena encontrado nos 5 prêmios";
        }

        if (TIPO_GRUPO.equals(tipo)) {
            return Arrays.stream(premiosSorteados)
                    .map(premio -> String.valueOf(sorteioService.obterGrupoPorMilhar(premio)))
                    .collect(Collectors.joining(", "));
        }

        if (TIPO_DEZENA.equals(tipo)) {
            return Arrays.stream(premiosSorteados)
                    .map(sorteioService::obterDezena)
                    .collect(Collectors.joining(", "));
        }

        if (TIPO_CENTENA.equals(tipo)) {
            return Arrays.stream(premiosSorteados)
                    .map(sorteioService::obterCentena)
                    .collect(Collectors.joining(", "));
        }

        if (TIPO_MILHAR.equals(tipo)) {
            return String.join(", ", premiosSorteados);
        }

        return Arrays.stream(premiosSorteados)
                .map(sorteioService::obterDezena)
                .collect(Collectors.joining(", "));
    }

    private static class ResultadoConferencia {

        private final boolean venceu;
        private final double premio;
        private final String milharReferencia;
        private final int grupoReferencia;
        private final String resultadoComparado;

        private ResultadoConferencia(boolean venceu, double premio, String milharReferencia,
                                     int grupoReferencia, String resultadoComparado) {
            this.venceu = venceu;
            this.premio = premio;
            this.milharReferencia = milharReferencia;
            this.grupoReferencia = grupoReferencia;
            this.resultadoComparado = resultadoComparado;
        }
    }

}
