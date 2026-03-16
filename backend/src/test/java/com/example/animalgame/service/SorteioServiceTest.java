package com.example.animalgame.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SorteioServiceTest {

    private final SorteioService sorteioService = new SorteioService();

    @Test
    void deveSortearGrupoEntre1e25() {
        int grupo = sorteioService.sortearGrupo();

        assertTrue(grupo >= 1 && grupo <= 25);
    }

    @Test
    void deveCalcularPremioCorretamente() {
        double premio = sorteioService.calcularPremio(10.0);

        assertEquals(180.0, premio);
    }
}