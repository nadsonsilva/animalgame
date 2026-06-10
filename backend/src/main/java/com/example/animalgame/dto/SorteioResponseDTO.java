package com.example.animalgame.dto;

import java.util.List;

public class SorteioResponseDTO {

    private Integer grupoSorteado;
    private String mensagem;
    private List<String> premios;
    private List<String> dezenas;
    private List<Integer> grupos;
    private List<ApostaResponseDTO> apostasProcessadas;

    public SorteioResponseDTO() {
    }

    public SorteioResponseDTO(Integer grupoSorteado, String mensagem) {
        this.grupoSorteado = grupoSorteado;
        this.mensagem = mensagem;
    }

    public SorteioResponseDTO(Integer grupoSorteado, String mensagem, List<String> premios,
                              List<String> dezenas, List<Integer> grupos) {
        this.grupoSorteado = grupoSorteado;
        this.mensagem = mensagem;
        this.premios = premios;
        this.dezenas = dezenas;
        this.grupos = grupos;
    }

    public SorteioResponseDTO(Integer grupoSorteado, String mensagem, List<String> premios,
                              List<String> dezenas, List<Integer> grupos,
                              List<ApostaResponseDTO> apostasProcessadas) {
        this(grupoSorteado, mensagem, premios, dezenas, grupos);
        this.apostasProcessadas = apostasProcessadas;
    }

    public Integer getGrupoSorteado() {
        return grupoSorteado;
    }

    public void setGrupoSorteado(Integer grupoSorteado) {
        this.grupoSorteado = grupoSorteado;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public List<String> getPremios() {
        return premios;
    }

    public void setPremios(List<String> premios) {
        this.premios = premios;
    }

    public List<String> getDezenas() {
        return dezenas;
    }

    public void setDezenas(List<String> dezenas) {
        this.dezenas = dezenas;
    }

    public List<Integer> getGrupos() {
        return grupos;
    }

    public void setGrupos(List<Integer> grupos) {
        this.grupos = grupos;
    }

    public List<ApostaResponseDTO> getApostasProcessadas() {
        return apostasProcessadas;
    }

    public void setApostasProcessadas(List<ApostaResponseDTO> apostasProcessadas) {
        this.apostasProcessadas = apostasProcessadas;
    }
}
