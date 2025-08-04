package com.anderson.votacao.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

/**
 * Cliente para validação de CPF usando BrasilAPI
 */
@Component
public class CpfClient {

    private static final Logger logger = LoggerFactory.getLogger(CpfClient.class);

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public CpfClient(RestTemplate restTemplate,
                     @Value("${cpf.service.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    /**
     * Chama BrasilAPI para validar CPF.
     * @param cpf CPF a ser validado
     * @return CpfStatus.ABLE_TO_VOTE se válido, caso contrário UNABLE_TO_VOTE
     */
    public CpfStatus verificarCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF não pode ser nulo ou vazio");
        }
        String url = String.format("%s/%s", baseUrl, cpf);
        try {
            BrasilApiResponse response = restTemplate.getForObject(url, BrasilApiResponse.class);
            logger.info("Resposta BrasilAPI: cpf={} valid={}", response.getCpf(), response.isValid());
            if (response == null) {
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Resposta vazia do serviço de CPF");
            }
            return response.isValid() ? CpfStatus.ABLE_TO_VOTE : CpfStatus.UNABLE_TO_VOTE;
        } catch (RestClientException ex) {
            logger.error("Erro ao chamar BrasilAPI: {}", ex.getMessage());
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Erro ao validar CPF");
        }
    }
}