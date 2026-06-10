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
    private String tipoAposta;
    private String numeroApostado;
    private String segundoNumero;
    private String numeroSorteado;
    private String status;

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

    public ApostaHistoricoResponseDTO(Long id, Long usuarioId, String nomeUsuario,
                                      Integer grupoAnimal, String nomeAnimal,
                                      Double valorApostado, LocalDateTime dataHora,
                                      Boolean vencedora, Double premio, String tipoAposta,
                                      String numeroApostado, String segundoNumero,
                                      String numeroSorteado) {
        this(id, usuarioId, nomeUsuario, grupoAnimal, nomeAnimal, valorApostado, dataHora, vencedora, premio);
        this.tipoAposta = tipoAposta;
        this.numeroApostado = numeroApostado;
        this.segundoNumero = segundoNumero;
        this.numeroSorteado = numeroSorteado;
    }

    public ApostaHistoricoResponseDTO(Long id, Long usuarioId, String nomeUsuario,
                                      Integer grupoAnimal, String nomeAnimal,
                                      Double valorApostado, LocalDateTime dataHora,
                                      Boolean vencedora, Double premio, String tipoAposta,
                                      String numeroApostado, String segundoNumero,
                                      String numeroSorteado, String status) {
        this(id, usuarioId, nomeUsuario, grupoAnimal, nomeAnimal, valorApostado, dataHora, vencedora, premio,
                tipoAposta, numeroApostado, segundoNumero, numeroSorteado);
        this.status = status;
    }

    public Long getId() { return id; }
    public Long getUsuarioId() { return usuarioId; }
    public String getNomeUsuario() { return nomeUsuario; }
    public Integer getGrupoAnimal() { return grupoAnimal; }
    public String getNomeAnimal() { return nomeAnimal; }
    public Double getValorApostado() { return valorApostado; }
    public LocalDateTime getDataHora() { return dataHora; }
    public Boolean getVencedora() { return vencedora; }
    public Double getPremio() { return premio; }
    public String getTipoAposta() { return tipoAposta; }
    public String getNumeroApostado() { return numeroApostado; }
    public String getSegundoNumero() { return segundoNumero; }
    public String getNumeroSorteado() { return numeroSorteado; }
    public String getStatus() { return status; }
    public void setId(Long id) { this.id = id; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }
    public void setGrupoAnimal(Integer grupoAnimal) { this.grupoAnimal = grupoAnimal; }
    public void setNomeAnimal(String nomeAnimal) { this.nomeAnimal = nomeAnimal; }
    public void setValorApostado(Double valorApostado) { this.valorApostado = valorApostado; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public void setVencedora(Boolean vencedora) { this.vencedora = vencedora; }
    public void setPremio(Double premio) { this.premio = premio; }
    public void setTipoAposta(String tipoAposta) { this.tipoAposta = tipoAposta; }
    public void setNumeroApostado(String numeroApostado) { this.numeroApostado = numeroApostado; }
    public void setSegundoNumero(String segundoNumero) { this.segundoNumero = segundoNumero; }
    public void setNumeroSorteado(String numeroSorteado) { this.numeroSorteado = numeroSorteado; }
    public void setStatus(String status) { this.status = status; }
}
