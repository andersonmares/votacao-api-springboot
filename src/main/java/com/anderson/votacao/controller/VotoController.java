package com.anderson.votacao.controller;

import com.anderson.votacao.dto.VotoDTO;
import com.anderson.votacao.entity.Voto;
import com.anderson.votacao.service.VotoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/votos")
@RequiredArgsConstructor
public class VotoController {

    private final VotoService votoService;

    @PostMapping
    public ResponseEntity<Voto> votar(@Valid @RequestBody VotoDTO dto) {
        Voto salvo = votoService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }
}