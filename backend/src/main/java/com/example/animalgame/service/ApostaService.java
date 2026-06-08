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
        return processarAposta(usuarioId, grupoAnimal, valor, TIPO_GRUPO, null, null).apostaSalva;
    }

    @Transactional
    public ApostaResponseDTO registrarApostaComResumo(Long usuarioId, Integer grupoAnimal, Double valor) {
        ResultadoAposta resultado = processarAposta(usuarioId, grupoAnimal, valor, TIPO_GRUPO, null, null);
        return toResponseDTO(resultado);
    }

    @Transactional
    public ApostaResponseDTO registrarApostaComResumo(Long usuarioId, Integer grupoAnimal, Double valor,
                                                       String tipoAposta, String numeroApostado,
                                                       String segundoNumero) {
        ResultadoAposta resultado = processarAposta(usuarioId, grupoAnimal, valor, tipoAposta, numeroApostado, segundoNumero);
        return toResponseDTO(resultado);
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

    private ResultadoAposta processarAposta(Long usuarioId, Integer grupoAnimal, Double valor,
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

        String[] premiosSorteados = sorteioService.sortearCincoMilhares();
        if (premiosSorteados == null || premiosSorteados.length == 0 || premiosSorteados[0] == null) {
            int grupoGerado = sorteioService.sortearGrupo();
            premiosSorteados = new String[] { gerarMilharPorGrupo(grupoGerado), "0000", "0000", "0000", "0000" };
        }
        String primeiroPremio = premiosSorteados[0];
        int grupoSorteado = sorteioService.obterGrupoPorMilhar(primeiroPremio);

        Animal animalSorteado = animalRepository.findByGrupo(grupoSorteado)
                .orElseThrow(() -> new RegraNegocioException("Animal sorteado não encontrado"));

        Animal animalEscolhido = obterAnimalEscolhido(tipo, grupoAnimal, numeroFormatado);

        boolean venceu = verificarResultado(tipo, animalEscolhido.getGrupo(), numeroFormatado, segundoNumeroFormatado, premiosSorteados);
        double premio = venceu ? sorteioService.calcularPremio(valor) : 0.0;

        usuario.setSaldo(usuario.getSaldo() - valor);

        if (usuario.getSaldo() < 0) {
            throw new RegraNegocioException("Saldo não pode ficar negativo");
        }

        if (venceu) {
            usuario.setSaldo(usuario.getSaldo() + premio);
        }

        usuarioRepository.save(usuario);

        Aposta aposta = new Aposta();
        aposta.setUsuario(usuario);
        aposta.setAnimal(animalEscolhido);
        aposta.setValor(valor);
        aposta.setDataHora(LocalDateTime.now());
        aposta.setVencedora(venceu);
        aposta.setPremio(premio);
        aposta.setTipoAposta(tipo);
        aposta.setNumeroApostado(numeroFormatado);
        aposta.setSegundoNumero(segundoNumeroFormatado);
        aposta.setNumeroSorteado(String.join(", ", premiosSorteados));

        Aposta apostaSalva = apostaRepository.save(aposta);

        return new ResultadoAposta(
                apostaSalva,
                animalEscolhido,
                grupoSorteado,
                animalSorteado,
                venceu,
                premio,
                valor,
                tipo,
                numeroFormatado,
                segundoNumeroFormatado,
                primeiroPremio,
                Arrays.asList(premiosSorteados),
                montarResultadoComparado(tipo, premiosSorteados)
        );
    }

    private ApostaResponseDTO toResponseDTO(ResultadoAposta resultado) {
        return new ApostaResponseDTO(
                resultado.animalEscolhido.getGrupo(),
                resultado.animalEscolhido.getNome(),
                resultado.grupoSorteado,
                resultado.animalSorteado.getNome(),
                resultado.venceu,
                resultado.premio,
                resultado.valorApostado,
                resultado.tipoAposta,
                resultado.numeroApostado,
                resultado.segundoNumero,
                resultado.milharSorteada,
                resultado.premiosSorteados,
                resultado.resultadoComparado
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
                aposta.getNumeroSorteado()
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
            grupo = sorteioService.obterGrupoPorMilhar(String.format("%04d", Integer.parseInt(dezena)));
        }

        return animalRepository.findByGrupo(grupo)
                .orElseThrow(() -> new RegraNegocioException("Animal não encontrado"));
    }

    private boolean verificarResultado(String tipo, Integer grupoAnimal, String numeroApostado,
                                       String segundoNumero, String[] premiosSorteados) {
        String primeiroPremio = premiosSorteados[0];

        if (TIPO_GRUPO.equals(tipo)) {
            return grupoAnimal.equals(sorteioService.obterGrupoPorMilhar(primeiroPremio));
        }

        if (TIPO_DEZENA.equals(tipo)) {
            return numeroApostado.equals(sorteioService.obterDezena(primeiroPremio));
        }

        if (TIPO_CENTENA.equals(tipo)) {
            return numeroApostado.equals(sorteioService.obterCentena(primeiroPremio));
        }

        if (TIPO_MILHAR.equals(tipo)) {
            return numeroApostado.equals(primeiroPremio);
        }

        List<String> dezenasSorteadas = Arrays.stream(premiosSorteados)
                .map(sorteioService::obterDezena)
                .collect(Collectors.toList());

        return dezenasSorteadas.contains(numeroApostado) && dezenasSorteadas.contains(segundoNumero);
    }

    private String gerarMilharPorGrupo(int grupo) {
        int dezena = grupo == 25 ? 97 : ((grupo - 1) * 4) + 1;
        return String.format("00%02d", dezena);
    }

    private String montarResultadoComparado(String tipo, String[] premiosSorteados) {
        String primeiroPremio = premiosSorteados[0];

        if (TIPO_GRUPO.equals(tipo)) {
            return String.valueOf(sorteioService.obterGrupoPorMilhar(primeiroPremio));
        }

        if (TIPO_DEZENA.equals(tipo)) {
            return sorteioService.obterDezena(primeiroPremio);
        }

        if (TIPO_CENTENA.equals(tipo)) {
            return sorteioService.obterCentena(primeiroPremio);
        }

        if (TIPO_MILHAR.equals(tipo)) {
            return primeiroPremio;
        }

        return Arrays.stream(premiosSorteados)
                .map(sorteioService::obterDezena)
                .collect(Collectors.joining(", "));
    }

    private static class ResultadoAposta {

        private final Aposta apostaSalva;
        private final Animal animalEscolhido;
        private final Integer grupoSorteado;
        private final Animal animalSorteado;
        private final Boolean venceu;
        private final Double premio;
        private final Double valorApostado;
        private final String tipoAposta;
        private final String numeroApostado;
        private final String segundoNumero;
        private final String milharSorteada;
        private final List<String> premiosSorteados;
        private final String resultadoComparado;

        public ResultadoAposta(
                Aposta apostaSalva,
                Animal animalEscolhido,
                Integer grupoSorteado,
                Animal animalSorteado,
                Boolean venceu,
                Double premio,
                Double valorApostado,
                String tipoAposta,
                String numeroApostado,
                String segundoNumero,
                String milharSorteada,
                List<String> premiosSorteados,
                String resultadoComparado) {

            this.apostaSalva = apostaSalva;
            this.animalEscolhido = animalEscolhido;
            this.grupoSorteado = grupoSorteado;
            this.animalSorteado = animalSorteado;
            this.venceu = venceu;
            this.premio = premio;
            this.valorApostado = valorApostado;
            this.tipoAposta = tipoAposta;
            this.numeroApostado = numeroApostado;
            this.segundoNumero = segundoNumero;
            this.milharSorteada = milharSorteada;
            this.premiosSorteados = premiosSorteados;
            this.resultadoComparado = resultadoComparado;
        }
    }
}
