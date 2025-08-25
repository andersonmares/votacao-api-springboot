package com.anderson.votacao.service;

import com.anderson.votacao.kafka.VotoEventProducer;
import com.anderson.votacao.repository.PautaRepository;
import com.anderson.votacao.repository.VotoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VotoServiceCountTest {

    @Mock private VotoRepository votoRepository;
    @Mock private PautaRepository pautaRepository;
    @Mock private VotoEventProducer votoEventProducer;

    @Test
    void contarVotosSimENao_deveDelegarParaRepositorio() {
        when(votoRepository.countByPautaIdAndVoto(10L, true)).thenReturn(7L);
        when(votoRepository.countByPautaIdAndVoto(10L, false)).thenReturn(3L);

        VotoService svc = new VotoService(votoRepository, pautaRepository, List.of(), votoEventProducer);

        assertThat(svc.contarVotosSim(10L)).isEqualTo(7L);
        assertThat(svc.contarVotosNao(10L)).isEqualTo(3L);

        verify(votoRepository).countByPautaIdAndVoto(10L, true);
        verify(votoRepository).countByPautaIdAndVoto(10L, false);
    }
}