package com.anderson.votacao.service.validator;

import com.anderson.votacao.client.CpfStatus;
import com.anderson.votacao.client.FakeCpfClient;
import com.anderson.votacao.dto.VotoDTO;
import com.anderson.votacao.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class CpfStatusValidator implements VotoValidator {

    private final FakeCpfClient fakeCpfClient;

    @Override
    public void validar(VotoDTO dto) {
        CpfStatus status;
        try {
            status = fakeCpfClient.verificarCpf(dto.getCpf());
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new BusinessException("Erro na validação de CPF");
        }

        if (status == CpfStatus.UNABLE_TO_VOTE) {
            throw new BusinessException("Associado não está habilitado para votar");
        }
    }
}

