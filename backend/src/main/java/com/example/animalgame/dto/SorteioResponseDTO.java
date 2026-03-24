package com.example.animalgame.dto;

public class SorteioResponseDTO {

    private Integer grupoSorteado;
    private String mensagem;

    public SorteioResponseDTO() {
    }

    public SorteioResponseDTO(Integer grupoSorteado, String mensagem) {
        this.grupoSorteado = grupoSorteado;
        this.mensagem = mensagem;
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
}