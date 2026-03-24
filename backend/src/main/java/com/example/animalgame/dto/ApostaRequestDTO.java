package com.example.animalgame.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class ApostaRequestDTO {

    @NotNull(message = "Usuário é obrigatório")
    private Long usuarioId;

    @NotNull(message = "Grupo do animal é obrigatório")
    private Integer grupoAnimal;

    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser maior que zero")
    private Double valor;

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
}