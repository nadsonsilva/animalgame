package com.example.animalgame.dto;

public class ApostaResponseDTO {

    private Integer numeroEscolhido;
    private String animalEscolhido;

    private Integer numeroSorteado;
    private String animalSorteado;

    private boolean ganhou;
    private Double valorGanho;
    private Double valorApostado;

    public ApostaResponseDTO(Integer numeroEscolhido, String animalEscolhido,
                             Integer numeroSorteado, String animalSorteado,
                             boolean ganhou, Double valorGanho, Double valorApostado) {
        this.numeroEscolhido = numeroEscolhido;
        this.animalEscolhido = animalEscolhido;
        this.numeroSorteado = numeroSorteado;
        this.animalSorteado = animalSorteado;
        this.ganhou = ganhou;
        this.valorGanho = valorGanho;
        this.valorApostado = valorApostado;
    }

    public Integer getNumeroEscolhido() {
        return numeroEscolhido;
    }

    public void setNumeroEscolhido(Integer numeroEscolhido) {
        this.numeroEscolhido = numeroEscolhido;
    }

    public String getAnimalEscolhido() {
        return animalEscolhido;
    }

    public void setAnimalEscolhido(String animalEscolhido) {
        this.animalEscolhido = animalEscolhido;
    }

    public Integer getNumeroSorteado() {
        return numeroSorteado;
    }

    public void setNumeroSorteado(Integer numeroSorteado) {
        this.numeroSorteado = numeroSorteado;
    }

    public String getAnimalSorteado() {
        return animalSorteado;
    }

    public void setAnimalSorteado(String animalSorteado) {
        this.animalSorteado = animalSorteado;
    }

    public boolean isGanhou() {
        return ganhou;
    }

    public void setGanhou(boolean ganhou) {
        this.ganhou = ganhou;
    }

    public Double getValorGanho() {
        return valorGanho;
    }

    public void setValorGanho(Double valorGanho) {
        this.valorGanho = valorGanho;
    }

    public Double getValorApostado() {
        return valorApostado;
    }

    public void setValorApostado(Double valorApostado) {
        this.valorApostado = valorApostado;
    }
}