package com.anderson.votacao.client;

/**
 * Representa a resposta da BrasilAPI para validação de CPF
 */
public class BrasilApiResponse {
    private String cpf;
    private boolean valid;

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}