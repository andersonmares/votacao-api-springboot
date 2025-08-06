package com.anderson.votacao.controller;

import com.anderson.votacao.dto.VotoDTO;
import com.anderson.votacao.service.VotoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/votos")
@RequiredArgsConstructor
public class VotoController {

    private final VotoService votoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void votar(@Valid @RequestBody VotoDTO votoDTO) {
        votoService.votar(votoDTO);
    }
}