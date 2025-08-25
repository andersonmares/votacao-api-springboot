package com.anderson.votacao.service;

import com.anderson.votacao.dto.VotoDTO;
import com.anderson.votacao.entity.Pauta;
import com.anderson.votacao.entity.Voto;
import com.anderson.votacao.kafka.VotoEventProducer;
import com.anderson.votacao.repository.PautaRepository;
import com.anderson.votacao.repository.VotoRepository;
import com.anderson.votacao.service.validator.VotoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VotoServiceTest {

    @Mock private VotoRepository votoRepository;
    @Mock private PautaRepository pautaRepository;
    @Mock private VotoEventProducer votoEventProducer;

    private VotoService votoService;

    @BeforeEach
    void setup() {
        // lista de validadores vazia para o teste unitário
        votoService = new VotoService(
                votoRepository,
                pautaRepository,
                Collections.<VotoValidator>emptyList(),
                votoEventProducer
        );
    }

    @Test
    void deveSalvarVotoEPublicarEvento() {
        // given
        Pauta pauta = Pauta.builder().id(1L).titulo("Teste").descricao("Desc").build();
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));

        when(votoRepository.save(any(Voto.class))).thenAnswer(inv -> {
            Voto v = inv.getArgument(0);
            return Voto.builder()
                    .id(10L)
                    .pauta(v.getPauta())
                    .associadoId(v.getAssociadoId())
                    .voto(v.getVoto())
                    .cpf(v.getCpf())
                    .build();
        });

        VotoDTO dto = VotoDTO.builder()
                .cpf("12345678901")
                .pautaId(1L)
                .associadoId(7)
                .voto(true)
                .build();

        // when
        Voto salvo = votoService.salvar(dto);

        // then
        assertThat(salvo.getId()).isEqualTo(10L);
        assertThat(salvo.getPauta().getId()).isEqualTo(1L);
        assertThat(salvo.getAssociadoId()).isEqualTo(7);
        assertThat(salvo.getVoto()).isTrue();
        assertThat(salvo.getCpf()).isEqualTo("12345678901");

        // Verifica publicação do evento no producer
        verify(votoEventProducer, times(1)).publicar(any());

        // (opcional) capturar o Voto enviado ao repo
        ArgumentCaptor<Voto> cap = ArgumentCaptor.forClass(Voto.class);
        verify(votoRepository).save(cap.capture());
        assertThat(cap.getValue().getCpf()).isEqualTo("12345678901");
    }

    @Test
    void deveLancarExcecaoQuandoPautaNaoExiste() {
        // given
        when(pautaRepository.findById(999L)).thenReturn(Optional.empty());
        VotoDTO dto = VotoDTO.builder()
                .cpf("12345678901")
                .pautaId(999L)
                .associadoId(7)
                .voto(true)
                .build();

        // when/then
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> votoService.salvar(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Pauta não encontrada");
    }
}