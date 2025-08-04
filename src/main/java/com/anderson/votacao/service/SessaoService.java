package com.anderson.votacao.service;

import com.anderson.votacao.dto.SessaoDTO;
import com.anderson.votacao.entity.Pauta;
import com.anderson.votacao.entity.Sessao;
import com.anderson.votacao.exception.BusinessException;
import com.anderson.votacao.repository.PautaRepository;
import com.anderson.votacao.repository.SessaoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SessaoService {

    private final SessaoRepository sessaoRepository;
    private final PautaRepository pautaRepository;

    public SessaoService(SessaoRepository sessaoRepository, PautaRepository pautaRepository) {
        this.sessaoRepository = sessaoRepository;
        this.pautaRepository = pautaRepository;
    }

    public List<Sessao> listarTodas() {
        return sessaoRepository.findAll();
    }

    public Sessao abrirSessao(SessaoDTO sessaoDTO) {
        Sessao sessao = new Sessao();
        sessao.setPauta(pautaRepository.findById(sessaoDTO.getPautaId())
                .orElseThrow(() -> new BusinessException("Pauta não encontrada")));
        sessao.setDataHoraInicio(LocalDateTime.now());
        long duracao = sessaoDTO.getDuracao() != null ? sessaoDTO.getDuracao() : 1L;
        sessao.setDataHoraFim(sessao.getDataHoraInicio().plusMinutes(duracao));
        return sessaoRepository.save(sessao);
    }

    public Sessao buscarSessaoPorPauta(Long pautaId) {
        return sessaoRepository.findByPautaId(pautaId)
                .orElseThrow(() -> new RuntimeException("Sessão não encontrada"));
    }
}
