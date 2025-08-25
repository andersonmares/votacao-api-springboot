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

    private static final Logger logger = LoggerFactory.getLogger(VotoService.class);

    private final VotoRepository votoRepository;
    private final PautaRepository pautaRepository;
    private final List<VotoValidator> validators;
    private final VotoEventProducer votoEventProducer;

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

        Voto salvo = votoRepository.save(voto);

        votoEventProducer.publicar(
                com.anderson.votacao.kafka.dto.VotoEvent.builder()
                        .pautaId(salvo.getPauta().getId())
                        .associadoId(salvo.getAssociadoId())
                        .voto(salvo.getVoto())
                        .build()
        );
        return salvo;
    }

    public long contarVotosSim(Long pautaId) {
        return votoRepository.countByPautaIdAndVoto(pautaId, true);
    }

    public long contarVotosNao(Long pautaId) {
        return votoRepository.countByPautaIdAndVoto(pautaId, false);
    }
}
