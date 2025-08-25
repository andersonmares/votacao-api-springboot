package com.anderson.votacao.controller;

import com.anderson.votacao.dto.PautaDTO;
import com.anderson.votacao.service.PautaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pautas")
@RequiredArgsConstructor
public class PautaController {

    private final PautaService pautaService;

    @PostMapping
    public ResponseEntity<PautaDTO> criar(@Valid @RequestBody PautaDTO dto) {
        PautaDTO criada = pautaService.criarPauta(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }

    @GetMapping
    public ResponseEntity<List<PautaDTO>> listar() {
        List<PautaDTO> pautas = pautaService.listarPautas();
        return ResponseEntity.ok(pautas);
    }
}