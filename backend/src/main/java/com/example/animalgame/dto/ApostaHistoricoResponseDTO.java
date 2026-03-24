package com.example.animalgame.dto;

import java.time.LocalDateTime;

public class ApostaHistoricoResponseDTO {

    private Long id;
    private Long usuarioId;
    private String nomeUsuario;
    private Integer grupoAnimal;
    private String nomeAnimal;
    private Double valorApostado;
    private LocalDateTime dataHora;
    private Boolean vencedora;
    private Double premio;

    public ApostaHistoricoResponseDTO() {
    }

    public ApostaHistoricoResponseDTO(Long id, Long usuarioId, String nomeUsuario,
                                      Integer grupoAnimal, String nomeAnimal,
                                      Double valorApostado, LocalDateTime dataHora,
                                      Boolean vencedora, Double premio) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.nomeUsuario = nomeUsuario;
        this.grupoAnimal = grupoAnimal;
        this.nomeAnimal = nomeAnimal;
        this.valorApostado = valorApostado;
        this.dataHora = dataHora;
        this.vencedora = vencedora;
        this.premio = premio;
    }

    public Long getId() {
        return id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public Integer getGrupoAnimal() {
        return grupoAnimal;
    }

    public String getNomeAnimal() {
        return nomeAnimal;
    }

    public Double getValorApostado() {
        return valorApostado;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public Boolean getVencedora() {
        return vencedora;
    }

    public Double getPremio() {
        return premio;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public void setGrupoAnimal(Integer grupoAnimal) {
        this.grupoAnimal = grupoAnimal;
    }

    public void setNomeAnimal(String nomeAnimal) {
        this.nomeAnimal = nomeAnimal;
    }

    public void setValorApostado(Double valorApostado) {
        this.valorApostado = valorApostado;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public void setVencedora(Boolean vencedora) {
        this.vencedora = vencedora;
    }

    public void setPremio(Double premio) {
        this.premio = premio;
    }
}