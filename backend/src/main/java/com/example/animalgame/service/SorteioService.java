package com.example.animalgame.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class SorteioService {

    public int sortearGrupo() {
        Random random = new Random();
        return random.nextInt(25) + 1;
    }

    public double calcularPremio(double valorAposta) {
        return valorAposta * 18;
    }
}