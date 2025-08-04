package com.anderson.votacao.controller;

import com.anderson.votacao.dto.PautaDTO;
import com.anderson.votacao.dto.ResultadoDTO;
import com.anderson.votacao.service.PautaService;
import com.anderson.votacao.service.VotoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/v1/pautas")
@RequiredArgsConstructor
public class PautaController {

    private final PautaService pautaService;
    private final VotoService votoService;

    /**
     * Cria uma nova pauta.
     * @param pautaDTO corpo da requisição com título e descrição
     * @return a pauta criada com seu ID
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PautaDTO criarPauta(@Valid @RequestBody PautaDTO pautaDTO) {
        return pautaService.criarPauta(pautaDTO);
    }

    /**
     * Retorna todas as pautas cadastradas.
     */
    @GetMapping
    public List<PautaDTO> listarPautas() {
        return pautaService.listarPautas();
    }

    /**
     * Retorna o resultado da votação de uma pauta (quantidade de "Sim" e "Não").
     * @param id ID da pauta
     */
    @GetMapping("/{id}/resultado")
    public ResultadoDTO obterResultado(
            @PathVariable("id") Long id  // <— aqui!
    ) {
        long votosSim = votoService.contarVotosSim(id);
        long votosNao = votoService.contarVotosNao(id);
        return new ResultadoDTO(votosSim, votosNao);
    }
}
