package com.anderson.votacao.service.validator;

import com.anderson.votacao.dto.VotoDTO;
import com.anderson.votacao.entity.Pauta;

public interface VotoValidator {

    void validar(VotoDTO dto);

    default void validate(VotoDTO dto, Pauta pauta) {
        validar(dto);
    }
}
