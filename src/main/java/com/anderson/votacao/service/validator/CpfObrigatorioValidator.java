package com.anderson.votacao.service.validator;

import com.anderson.votacao.dto.VotoDTO;
import com.anderson.votacao.exception.BusinessException;
import org.springframework.stereotype.Component;

@Component
public class CpfObrigatorioValidator implements VotoValidator {

    @Override
    public void validar(VotoDTO dto) {
        if (dto.getCpf() == null || dto.getCpf().isBlank()) {
            throw new BusinessException("O campo CPF é obrigatório");
        }
    }
}

