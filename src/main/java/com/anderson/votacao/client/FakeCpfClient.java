package com.anderson.votacao.client;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Random;

@Component
public class FakeCpfClient {

    private final Random random = new Random();

    /**
     * Simula verificação de CPF:
     * - ~20% dos casos lança 404 (CPF inválido)
     * - Caso contrário, retorna aleatoriamente ABLE_TO_VOTE ou UNABLE_TO_VOTE
     */
    public CpfStatus verificarCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF não pode ser nulo ou vazio");
        }

        // CPF “não encontrado” em ~20% das requisições
        if (random.nextInt(100) < 20) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CPF não encontrado");
        }

        // CPF encontrado: decide se pode ou não votar
        return random.nextBoolean()
                ? CpfStatus.ABLE_TO_VOTE
                : CpfStatus.UNABLE_TO_VOTE;
    }
}