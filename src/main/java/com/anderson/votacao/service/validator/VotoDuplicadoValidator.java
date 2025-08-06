package com.anderson.votacao.service.validator;

import com.anderson.votacao.dto.VotoDTO;
import com.anderson.votacao.exception.BusinessException;
import com.anderson.votacao.repository.VotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VotoDuplicadoValidator implements VotoValidator {

    private final VotoRepository votoRepository;

    @Override
    public void validar(VotoDTO dto) {
        boolean jaVotou = votoRepository.existsByPautaIdAndAssociadoId(dto.getPautaId(), dto.getAssociadoId());
        if (jaVotou) {
            throw new BusinessException("Associado já votou nesta pauta");
        }
    }
}

