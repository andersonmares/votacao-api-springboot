package com.anderson.votacao.service;

import com.anderson.votacao.dto.VotoDTO;
import com.anderson.votacao.entity.Pauta;
import com.anderson.votacao.entity.Voto;
import com.anderson.votacao.exception.BusinessException;
import com.anderson.votacao.repository.PautaRepository;
import com.anderson.votacao.repository.VotoRepository;
import com.anderson.votacao.service.validator.VotoValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VotoService {

    private static final Logger logger = LoggerFactory.getLogger(VotoService.class);

    private final VotoRepository votoRepository;
    private final PautaRepository pautaRepository;
    private final List<VotoValidator> validadores;

    public Voto votar(VotoDTO dto) {
        logger.info("Recebendo voto: pautaId={}, associadoId={}, voto={}",
                dto.getPautaId(), dto.getAssociadoId(), dto.getVoto());

        // Executa todos os validadores
        validadores.forEach(validador -> validador.validar(dto));

        Pauta pauta = pautaRepository.findById(dto.getPautaId())
                .orElseThrow(() -> new BusinessException("Pauta não encontrada"));

        Voto voto = Voto.builder()
                .cpf(dto.getCpf())
                .associadoId(dto.getAssociadoId())
                .voto(dto.getVoto())
                .pauta(pauta)
                .build();

        Voto salvo = votoRepository.save(voto);
        logger.info("Voto salvo com id={}", salvo.getId());
        return salvo;
    }

    public long contarVotosSim(Long pautaId) {
        return votoRepository.countByPautaIdAndVoto(pautaId, true);
    }

    public long contarVotosNao(Long pautaId) {
        return votoRepository.countByPautaIdAndVoto(pautaId, false);
    }
}
