package com.anderson.votacao.service;

import com.anderson.votacao.dto.SessaoDTO;
import com.anderson.votacao.entity.Pauta;
import com.anderson.votacao.entity.Sessao;
import com.anderson.votacao.exception.BusinessException;
import com.anderson.votacao.repository.PautaRepository;
import com.anderson.votacao.repository.SessaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessaoServiceTest {

    @Mock private SessaoRepository sessaoRepository;
    @Mock private PautaRepository pautaRepository;

    private SessaoService service;

    @BeforeEach
    void setup() {
        service = new SessaoService(sessaoRepository, pautaRepository);
    }

    @Test
    void listarTodas_deveRetornarDoRepositorio() {
        when(sessaoRepository.findAll()).thenReturn(List.of(new Sessao(), new Sessao()));

        assertThat(service.listarTodas()).hasSize(2);
        verify(sessaoRepository, times(1)).findAll(); // cobre a chamada ao repo
        verifyNoMoreInteractions(sessaoRepository);
    }

    @Test
    void abrirSessao_semDuracaoUsaDefaultDe1Minuto() {
        Pauta pauta = Pauta.builder().id(1L).titulo("T").descricao("D").build();
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));
        when(sessaoRepository.save(any(Sessao.class))).thenAnswer(inv -> inv.getArgument(0));

        SessaoDTO dto = SessaoDTO.builder().pautaId(1L).build(); // sem duração -> default 1
        Sessao salvo = service.abrirSessao(dto);

        ArgumentCaptor<Sessao> cap = ArgumentCaptor.forClass(Sessao.class);
        verify(sessaoRepository).save(cap.capture());
        Sessao enviado = cap.getValue();

        assertThat(enviado.getPauta()).isEqualTo(pauta);

        assertThat(enviado.getDataHoraInicio()).isNotNull();
        assertThat(enviado.getDataHoraFim()).isNotNull();
        assertThat(Duration.between(enviado.getDataHoraInicio(), enviado.getDataHoraFim()).toMinutes())
                .isEqualTo(1L);

        assertThat(salvo).isSameAs(enviado);

        verify(pautaRepository).findById(1L);
        verifyNoMoreInteractions(pautaRepository);
    }

    @Test
    void abrirSessao_comDuracaoInformada() {
        Pauta pauta = Pauta.builder().id(1L).titulo("T").descricao("D").build();
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));
        when(sessaoRepository.save(any(Sessao.class))).thenAnswer(inv -> inv.getArgument(0));

        SessaoDTO dto = SessaoDTO.builder().pautaId(1L).duracao(5).build();

        Sessao salvo = service.abrirSessao(dto);

        assertThat(salvo.getPauta()).isEqualTo(pauta);
        assertThat(Duration.between(salvo.getDataHoraInicio(), salvo.getDataHoraFim()).toMinutes())
                .isEqualTo(5L);

        verify(pautaRepository).findById(1L);
        verify(sessaoRepository).save(any(Sessao.class));
    }

    @Test
    void abrirSessao_pautaInexistente_deveLancarBusiness() {
        when(pautaRepository.findById(1L)).thenReturn(Optional.empty());

        SessaoDTO dto = SessaoDTO.builder().pautaId(1L).build();

        assertThatThrownBy(() -> service.abrirSessao(dto))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Pauta não encontrada");

        verify(pautaRepository).findById(1L);
        verify(sessaoRepository, never()).save(any());
    }

    @Test
    void buscarSessaoPorPauta_encontrada() {
        Sessao s = new Sessao();
        when(sessaoRepository.findByPautaId(10L)).thenReturn(Optional.of(s));

        assertThat(service.buscarSessaoPorPauta(10L)).isSameAs(s);
        verify(sessaoRepository).findByPautaId(10L);
    }

    @Test
    void buscarSessaoPorPauta_inexistente_deveLancarRuntime() {
        when(sessaoRepository.findByPautaId(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarSessaoPorPauta(10L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Sessão não encontrada");
    }
}