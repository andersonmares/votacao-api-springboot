package com.anderson.votacao.controller;

import com.anderson.votacao.dto.SessaoDTO;
import com.anderson.votacao.entity.Sessao;
import com.anderson.votacao.service.SessaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/sessoes")
@RequiredArgsConstructor
public class SessaoController {

    private final SessaoService sessaoService;

    /**
     * Abre uma sessão de votação para a pauta informada.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void abrirSessao(@Valid @RequestBody SessaoDTO sessaoDTO) {
        sessaoService.abrirSessao(
                sessaoDTO.getPautaId(),
                sessaoDTO.getDuracaoMinutos()
        );
    }

    /**
     * Lista todas as sessões abertas (ou já encerradas) no sistema.
     */
    @GetMapping
    public List<SessaoDTO> listarSessoes() {
        List<Sessao> sessoes = sessaoService.listarTodas();
        return sessoes.stream()
                .map(s -> SessaoDTO.builder()
                        .pautaId(s.getPauta().getId())
                        // calcula duração em minutos entre início e fim
                        .duracaoMinutos((int) Duration.between(s.getInicio(), s.getFim()).toMinutes())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
