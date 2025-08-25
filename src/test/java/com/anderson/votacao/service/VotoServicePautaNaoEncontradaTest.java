package com.anderson.votacao.service;

import com.anderson.votacao.dto.VotoDTO;
import com.anderson.votacao.kafka.VotoEventProducer;
import com.anderson.votacao.repository.PautaRepository;
import com.anderson.votacao.repository.VotoRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VotoServicePautaNaoEncontradaTest {

    @Mock private VotoRepository votoRepository;
    @Mock private PautaRepository pautaRepository;
    @Mock private VotoEventProducer votoEventProducer;

    @Test
    void deveLancarIllegalArgumentException_QuandoPautaNaoEncontrada() {
        when(pautaRepository.findById(anyLong())).thenReturn(Optional.empty());

        VotoService service = new VotoService(votoRepository, pautaRepository, List.of(), votoEventProducer);

        VotoDTO dto = VotoDTO.builder()
                .cpf("12345678901")
                .pautaId(123L)
                .associadoId(1)
                .voto(true)
                .build();

        assertThatThrownBy(() -> service.salvar(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Pauta não encontrada");
    }
}