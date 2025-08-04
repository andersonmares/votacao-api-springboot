package com.anderson.votacao.service;

import com.anderson.votacao.entity.Pauta;
import com.anderson.votacao.entity.Sessao;
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

    public Sessao abrirSessao(Long pautaId, Integer duracaoMinutos) {
        Pauta pauta = pautaRepository.findById(pautaId)
                .orElseThrow(() -> new RuntimeException("Pauta não encontrada"));

        if (sessaoRepository.findByPautaId(pautaId).isPresent()) {
            throw new RuntimeException("Sessão já aberta para essa pauta");
        }

        Sessao sessao = new Sessao();
        sessao.setPauta(pauta);
        sessao.setInicio(LocalDateTime.now());
        sessao.setFim(LocalDateTime.now().plusMinutes(duracaoMinutos != null ? duracaoMinutos : 1));
        return sessaoRepository.save(sessao);
    }

    public Sessao buscarSessaoPorPauta(Long pautaId) {
        return sessaoRepository.findByPautaId(pautaId)
                .orElseThrow(() -> new RuntimeException("Sessão não encontrada"));
    }
}
