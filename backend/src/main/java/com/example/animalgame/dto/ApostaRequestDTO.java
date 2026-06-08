package com.example.animalgame.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class ApostaRequestDTO {

    @NotNull(message = "Usuário é obrigatório")
    private Long usuarioId;

    private Integer grupoAnimal;

    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser maior que zero")
    private Double valor;

    private String tipoAposta;
    private String numeroApostado;
    private String segundoNumero;

    public ApostaRequestDTO() {}

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Integer getGrupoAnimal() {
        return grupoAnimal;
    }

    public void setGrupoAnimal(Integer grupoAnimal) {
        this.grupoAnimal = grupoAnimal;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getTipoAposta() {
        return tipoAposta;
    }

    public void setTipoAposta(String tipoAposta) {
        this.tipoAposta = tipoAposta;
    }

    public String getNumeroApostado() {
        return numeroApostado;
    }

    public void setNumeroApostado(String numeroApostado) {
        this.numeroApostado = numeroApostado;
    }

    public String getSegundoNumero() {
        return segundoNumero;
    }

    public void setSegundoNumero(String segundoNumero) {
        this.segundoNumero = segundoNumero;
    }
}
