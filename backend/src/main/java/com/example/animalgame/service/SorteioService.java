package com.example.animalgame.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class SorteioService {

    private final Random random = new Random();

    public int sortearGrupo() {
        return random.nextInt(25) + 1;
    }

    public String sortearMilhar() {
        return String.format("%04d", random.nextInt(10000));
    }

    public String[] sortearCincoMilhares() {
        String[] premios = new String[5];

        for (int i = 0; i < premios.length; i++) {
            premios[i] = sortearMilhar();
        }

        return premios;
    }

    public String obterDezena(String milhar) {
        return milhar.substring(milhar.length() - 2);
    }

    public String obterCentena(String milhar) {
        return milhar.substring(milhar.length() - 3);
    }

    public int obterGrupoPorMilhar(String milhar) {
        int dezena = Integer.parseInt(obterDezena(milhar));
        if (dezena == 0) {
            return 25;
        }
        return ((dezena - 1) / 4) + 1;
    }

    public double calcularPremio(double valorAposta) {
        return calcularPremioCabeca(valorAposta);
    }

    public double calcularPremioCabeca(double valorAposta) {
        return valorAposta * 18;
    }

    public double calcularPremioCercado(double valorAposta) {
        return calcularPremioCabeca(valorAposta) / 5;
    }
}
