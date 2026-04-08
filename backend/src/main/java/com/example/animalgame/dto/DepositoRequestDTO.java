package com.example.animalgame.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

public class DepositoRequestDTO {

    @NotNull(message = "O valor do depósito é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor do depósito deve ser maior que zero")
    private Double valor;

    public DepositoRequestDTO() {
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}