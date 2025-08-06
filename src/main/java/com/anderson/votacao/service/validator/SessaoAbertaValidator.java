package com.anderson.votacao.service.validator;

import com.anderson.votacao.dto.VotoDTO;
import com.anderson.votacao.exception.BusinessException;
import com.anderson.votacao.repository.SessaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class SessaoAbertaValidator implements VotoValidator {

    private final SessaoRepository sessaoRepository;

    @Override
    public void validar(VotoDTO dto) {
        boolean aberta = sessaoRepository
                .existsByPautaIdAndDataHoraFimAfter(dto.getPautaId(), LocalDateTime.now());
        if (!aberta) {
            throw new BusinessException("Sessão não está aberta");
        }
    }
}

