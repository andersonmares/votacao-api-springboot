package com.anderson.votacao.service;

import com.anderson.votacao.dto.VotoDTO;
import com.anderson.votacao.entity.Pauta;
import com.anderson.votacao.entity.Sessao;
import com.anderson.votacao.entity.Voto;
import com.anderson.votacao.exception.BusinessException;
import com.anderson.votacao.repository.PautaRepository;
import com.anderson.votacao.repository.SessaoRepository;
import com.anderson.votacao.repository.VotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VotoService {

    private final VotoRepository votoRepository;
    private final SessaoRepository sessaoRepository;
    private final PautaRepository pautaRepository;

    /**
     * Registra um voto a partir dos dados do DTO.
     * @throws BusinessException em caso de regra de negócio violada
     */
    public void votar(VotoDTO dto) {
        String cpf      = dto.getCpf();
        Long pautaId    = dto.getPautaId();
        boolean escolha = dto.getVoto();

        // 1) Verifica existência da pauta
        Pauta pauta = pautaRepository.findById(pautaId)
                .orElseThrow(() -> new BusinessException("Pauta não encontrada"));

        // 2) Verifica existência da sessão
        Sessao sessao = sessaoRepository.findByPautaId(pautaId)
                .orElseThrow(() -> new BusinessException("Sessão não encontrada"));

        // 3) Verifica se a sessão ainda está aberta
        if (!sessao.isAberta()) {
            throw new BusinessException("Sessão encerrada");
        }

        // 4) Evita voto duplicado
        if (votoRepository.existsByCpfAndPautaId(cpf, pautaId)) {
            throw new BusinessException("Associado já votou nessa pauta");
        }

        // 5) Persiste o voto
        Voto voto = Voto.builder()
                .cpf(cpf)
                .voto(escolha)
                .pauta(pauta)
                .build();
        votoRepository.save(voto);
    }

    public long contarVotosSim(Long pautaId) {
        return votoRepository.countByPautaIdAndVoto(pautaId, true);
    }

    public long contarVotosNao(Long pautaId) {
        return votoRepository.countByPautaIdAndVoto(pautaId, false);
    }
}
