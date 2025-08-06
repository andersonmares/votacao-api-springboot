package com.anderson.votacao.service.validator;

import com.anderson.votacao.dto.VotoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AssociadoObrigatorioValidator implements VotoValidator {

    @Override
    public void validar(VotoDTO dto) {
        if (dto.getAssociadoId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo associadoId é obrigatório");
        }
    }
}

