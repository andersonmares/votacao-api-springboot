package com.anderson.votacao.service;

import com.anderson.votacao.dto.PautaDTO;
import com.anderson.votacao.entity.Pauta;
import com.anderson.votacao.repository.PautaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PautaService {

    private final PautaRepository repository;

    /**
     * Cria uma nova pauta a partir de um PautaDTO e retorna o DTO com o ID gerado.
     */
    public PautaDTO criarPauta(PautaDTO dto) {
        Pauta entidade = Pauta.builder()
                .titulo(dto.getTitulo())
                .descricao(dto.getDescricao())
                .build();

        Pauta salvo = repository.save(entidade);

        return PautaDTO.builder()
                .id(salvo.getId())
                .titulo(salvo.getTitulo())
                .descricao(salvo.getDescricao())
                .build();
    }

    /**
     * Retorna todas as pautas cadastradas como lista de DTOs.
     */
    public List<PautaDTO> listarPautas() {
        return repository.findAll().stream()
                .map(p -> PautaDTO.builder()
                        .id(p.getId())
                        .titulo(p.getTitulo())
                        .descricao(p.getDescricao())
                        .build())
                .collect(Collectors.toList());
    }
}
