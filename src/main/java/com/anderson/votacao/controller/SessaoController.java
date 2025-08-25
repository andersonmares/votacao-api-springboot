package com.anderson.votacao.controller;

import com.anderson.votacao.dto.SessaoDTO;
import com.anderson.votacao.dto.SessaoResponseDTO;
import com.anderson.votacao.entity.Sessao;
import com.anderson.votacao.mapper.SessaoMapper;
import com.anderson.votacao.service.SessaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sessoes")
@RequiredArgsConstructor
public class SessaoController {

    private final SessaoService sessaoService;
    private final SessaoMapper sessaoMapper;

    @PostMapping
    public ResponseEntity<SessaoResponseDTO> abrir(@Valid @RequestBody SessaoDTO dto) {
        Sessao s = sessaoService.abrirSessao(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(sessaoMapper.toResponse(s));
    }

    @GetMapping
    public ResponseEntity<List<SessaoResponseDTO>> listar() {
        List<Sessao> sessoes = sessaoService.listarTodas();
        List<SessaoResponseDTO> response = sessaoMapper.toResponseList(sessoes);
        return ResponseEntity.ok(response);
    }
}