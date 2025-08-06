package com.anderson.votacao.service;

import com.anderson.votacao.client.CpfStatus;
import com.anderson.votacao.client.FakeCpfClient;
import com.anderson.votacao.dto.VotoDTO;
import com.anderson.votacao.entity.Pauta;
import com.anderson.votacao.entity.Voto;
import com.anderson.votacao.exception.BusinessException;
import com.anderson.votacao.repository.PautaRepository;
import com.anderson.votacao.repository.SessaoRepository;
import com.anderson.votacao.repository.VotoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VotoService {

    private static final Logger logger = LoggerFactory.getLogger(VotoService.class);

    private final VotoRepository votoRepository;
    private final SessaoRepository sessaoRepository;
    private final PautaRepository pautaRepository;
    private final FakeCpfClient fakeCpfClient;

    @Transactional
    public Voto votar(VotoDTO dto) {
        logger.info("Recebendo voto: pautaId={}, associadoId={}, voto={}",
                dto.getPautaId(), dto.getAssociadoId(), dto.getVoto());

        // 1) Validações básicas de DTO
        if (dto.getCpf() == null || dto.getCpf().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo CPF é obrigatório");
        }
        if (dto.getAssociadoId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo associadoId é obrigatório");
        }

        // 2) Verifica CPF junto ao FakeCpfClient
        CpfStatus status;
        try {
            status = fakeCpfClient.verificarCpf(dto.getCpf());
        } catch (ResponseStatusException ex) {
            // Propaga 400 ou 404 conforme o client lançou
            throw ex;
        } catch (Exception ex) {
            logger.warn("Erro inesperado ao verificar CPF {}: {}", dto.getCpf(), ex.getMessage());
            throw new BusinessException("Erro na validação de CPF");
        }

        // 3) Caso UNABLE_TO_VOTE, retornamos 404 também
        if (status == CpfStatus.UNABLE_TO_VOTE) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Associado não está habilitado para votar");
        }

        // 4) Sessão aberta?
        boolean sessaoAberta = sessaoRepository
                .existsByPautaIdAndDataHoraFimAfter(dto.getPautaId(), LocalDateTime.now());
        if (!sessaoAberta) {
            throw new BusinessException("Sessão não está aberta");
        }

        // 5) Já votou nesta pauta?
        if (votoRepository.existsByPautaIdAndAssociadoId(dto.getPautaId(), dto.getAssociadoId())) {
            throw new BusinessException("Associado já votou nesta pauta");
        }

        // 6) Busca a pauta
        Pauta pauta = pautaRepository.findById(dto.getPautaId())
                .orElseThrow(() -> new BusinessException("Pauta não encontrada"));

        // 7) Cria e salva o voto
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
