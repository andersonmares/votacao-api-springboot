package com.anderson.votacao.service;

import com.anderson.votacao.client.CpfStatus;
import com.anderson.votacao.client.FakeCpfClient;
import com.anderson.votacao.dto.VotoDTO;
import com.anderson.votacao.entity.Voto;
import com.anderson.votacao.exception.BusinessException;
import com.anderson.votacao.repository.PautaRepository;
import com.anderson.votacao.repository.SessaoRepository;
import com.anderson.votacao.repository.VotoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VotoService {

    private final VotoRepository votoRepository;
    private final SessaoRepository sessaoRepository;
    private final PautaRepository pautaRepository;
    private final FakeCpfClient fakeCpfClient;
    private static final Logger logger = LoggerFactory.getLogger(VotoService.class);

    /**
     * Registra um voto a partir dos dados do DTO.
     * Valida CPF antes de registrar o voto.
     * @throws BusinessException em caso de regra de negócio violada
     */
    public Voto votar(VotoDTO dto) {
        logger.info("Recebendo voto: pautaId={}, associadoId={}, escolha={}", dto.getPautaId(), dto.getAssociadoId(), dto.getEscolha());

        // Validação de CPF via serviço externo
        CpfStatus status;
        try {
            status = fakeCpfClient.verificarCpf(dto.getCpf());
        } catch (ResponseStatusException ex) {
            logger.warn("Erro na validação de CPF {}: status {}", dto.getCpf(), ex.getStatusCode());
            throw ex;
        }
        if (status == CpfStatus.UNABLE_TO_VOTE) {
            logger.warn("CPF {} não habilitado para votar", dto.getCpf());
            throw new BusinessException("Associado não está habilitado para votar");
        }

        // Verifica se sessão está aberta
        if (!sessaoRepository.existsByPautaIdAndDataHoraFimAfter(dto.getPautaId(), LocalDateTime.now())) {
            logger.warn("Sessão não está aberta para pauta {}", dto.getPautaId());
            throw new BusinessException("Sessão não está aberta");
        }

        // Verifica voto único
        if (votoRepository.existsByPautaIdAndAssociadoId(dto.getPautaId(), dto.getAssociadoId())) {
            logger.warn("Associado {} já votou na pauta {}", dto.getAssociadoId(), dto.getPautaId());
            throw new BusinessException("Associado já votou nesta pauta");
        }

        Voto voto = new Voto(dto);
        Voto salvo = votoRepository.save(voto);
        logger.info("Voto salvo: id={}", salvo.getId());
        return salvo;
    }

    public long contarVotosSim(Long pautaId) {
        return votoRepository.countByPautaIdAndVoto(pautaId, true);
    }

    public long contarVotosNao(Long pautaId) {
        return votoRepository.countByPautaIdAndVoto(pautaId, false);
    }
}