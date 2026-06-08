package com.example.animalgame.dto;

import java.util.List;

public class ApostaResponseDTO {

    private Integer numeroEscolhido;
    private String animalEscolhido;

    private Integer numeroSorteado;
    private String animalSorteado;

    private boolean ganhou;
    private Double valorGanho;
    private Double valorApostado;

    private String tipoAposta;
    private String numeroApostado;
    private String segundoNumero;
    private String milharSorteada;
    private List<String> premiosSorteados;
    private String resultadoComparado;

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

    public ApostaResponseDTO(Integer numeroEscolhido, String animalEscolhido,
                             Integer numeroSorteado, String animalSorteado,
                             boolean ganhou, Double valorGanho, Double valorApostado,
                             String tipoAposta, String numeroApostado, String segundoNumero,
                             String milharSorteada, List<String> premiosSorteados,
                             String resultadoComparado) {
        this(numeroEscolhido, animalEscolhido, numeroSorteado, animalSorteado, ganhou, valorGanho, valorApostado);
        this.tipoAposta = tipoAposta;
        this.numeroApostado = numeroApostado;
        this.segundoNumero = segundoNumero;
        this.milharSorteada = milharSorteada;
        this.premiosSorteados = premiosSorteados;
        this.resultadoComparado = resultadoComparado;
    }

    public Integer getNumeroEscolhido() { return numeroEscolhido; }
    public void setNumeroEscolhido(Integer numeroEscolhido) { this.numeroEscolhido = numeroEscolhido; }
    public String getAnimalEscolhido() { return animalEscolhido; }
    public void setAnimalEscolhido(String animalEscolhido) { this.animalEscolhido = animalEscolhido; }
    public Integer getNumeroSorteado() { return numeroSorteado; }
    public void setNumeroSorteado(Integer numeroSorteado) { this.numeroSorteado = numeroSorteado; }
    public String getAnimalSorteado() { return animalSorteado; }
    public void setAnimalSorteado(String animalSorteado) { this.animalSorteado = animalSorteado; }
    public boolean isGanhou() { return ganhou; }
    public void setGanhou(boolean ganhou) { this.ganhou = ganhou; }
    public Double getValorGanho() { return valorGanho; }
    public void setValorGanho(Double valorGanho) { this.valorGanho = valorGanho; }
    public Double getValorApostado() { return valorApostado; }
    public void setValorApostado(Double valorApostado) { this.valorApostado = valorApostado; }
    public String getTipoAposta() { return tipoAposta; }
    public void setTipoAposta(String tipoAposta) { this.tipoAposta = tipoAposta; }
    public String getNumeroApostado() { return numeroApostado; }
    public void setNumeroApostado(String numeroApostado) { this.numeroApostado = numeroApostado; }
    public String getSegundoNumero() { return segundoNumero; }
    public void setSegundoNumero(String segundoNumero) { this.segundoNumero = segundoNumero; }
    public String getMilharSorteada() { return milharSorteada; }
    public void setMilharSorteada(String milharSorteada) { this.milharSorteada = milharSorteada; }
    public List<String> getPremiosSorteados() { return premiosSorteados; }
    public void setPremiosSorteados(List<String> premiosSorteados) { this.premiosSorteados = premiosSorteados; }
    public String getResultadoComparado() { return resultadoComparado; }
    public void setResultadoComparado(String resultadoComparado) { this.resultadoComparado = resultadoComparado; }
}
