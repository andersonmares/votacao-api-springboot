package com.anderson.votacao.client;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Random;

@Component
public class FakeCpfClient {

    private final Random random = new Random();

    public CpfStatus verificarCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF não pode ser nulo ou vazio");
        }
        if (random.nextBoolean()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CPF não encontrado");
        }
        return random.nextBoolean() ? CpfStatus.ABLE_TO_VOTE : CpfStatus.UNABLE_TO_VOTE;
    }
}