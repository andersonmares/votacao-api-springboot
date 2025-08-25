package com.anderson.votacao.service;

import com.anderson.votacao.dto.VotoDTO;
import com.anderson.votacao.entity.Pauta;
import com.anderson.votacao.entity.Voto;
import com.anderson.votacao.kafka.VotoEventProducer;
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

    private final VotoRepository votoRepository;
    private final PautaRepository pautaRepository;
    private final List<VotoValidator> validators;

    public Voto salvar(VotoDTO dto) {
        Pauta pauta = pautaRepository.findById(dto.getPautaId())
                .orElseThrow(() -> new IllegalArgumentException("Pauta não encontrada: " + dto.getPautaId()));

        for (VotoValidator v : validators) {
            v.validate(dto, pauta);
        }

        Voto voto = Voto.builder()
                .cpf(dto.getCpf())
                .pauta(pauta)
                .associadoId(dto.getAssociadoId())
                .voto(dto.getVoto())
                .build();

        return votoRepository.save(voto);
    }

    public long contarVotosSim(Long pautaId) {
        return votoRepository.countByPautaIdAndVoto(pautaId, true);
    }

    public long contarVotosNao(Long pautaId) {
        return votoRepository.countByPautaIdAndVoto(pautaId, false);
    }
}
