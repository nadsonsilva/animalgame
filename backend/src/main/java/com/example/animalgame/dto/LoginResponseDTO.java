package com.example.animalgame.dto;

public class LoginResponseDTO {

    private String token;
    private String tipo;
    private Long usuarioId;
    private String nome;
    private String email;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(String token, String tipo, Long usuarioId, String nome, String email) {
        this.token = token;
        this.tipo = tipo;
        this.usuarioId = usuarioId;
        this.nome = nome;
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public String getTipo() {
        return tipo;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}