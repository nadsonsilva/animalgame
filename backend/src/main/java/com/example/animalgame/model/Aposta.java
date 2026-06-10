package com.example.animalgame.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "apostas")
public class Aposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Usuario usuario;

    @ManyToOne(optional = false)
    private Animal animal;

    @Column(nullable = false)
    private Double valor;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Column(nullable = false)
    private Boolean vencedora;

    @Column(nullable = false)
    private Double premio;

    @Column(nullable = false)
    private String tipoAposta = "GRUPO";

    @Column(nullable = false)
    private String status = "PENDENTE";

    private String numeroApostado;

    private String segundoNumero;

    private String numeroSorteado;

    public Aposta() {
    }

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Boolean getVencedora() {
        return vencedora;
    }

    public void setVencedora(Boolean vencedora) {
        this.vencedora = vencedora;
    }

    public Double getPremio() {
        return premio;
    }

    public void setPremio(Double premio) {
        this.premio = premio;
    }

    public String getTipoAposta() {
        return tipoAposta;
    }

    public void setTipoAposta(String tipoAposta) {
        this.tipoAposta = tipoAposta;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getNumeroSorteado() {
        return numeroSorteado;
    }

    public void setNumeroSorteado(String numeroSorteado) {
        this.numeroSorteado = numeroSorteado;
    }
}
