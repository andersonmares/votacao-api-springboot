package com.anderson.votacao.service;

import com.anderson.votacao.dto.PautaDTO;
import com.anderson.votacao.entity.Pauta;
import com.anderson.votacao.repository.PautaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PautaServiceTest {

    @Mock
    private PautaRepository pautaRepository;

    @InjectMocks
    private PautaService pautaService;

    private PautaDTO entrada;

    @BeforeEach
    void setUp() {
        entrada = new PautaDTO();
        entrada.setTitulo("Título X");
        entrada.setDescricao("Descrição Y");
    }

    @Test
    void deveCriarPauta_chamarSaveEPropagarDadosBasicos() {
        when(pautaRepository.save(any(Pauta.class))).thenAnswer(inv -> {
            Pauta p = inv.getArgument(0);
            return Pauta.builder()
                    .id(10L)
                    .titulo(p.getTitulo())
                    .descricao(p.getDescricao())
                    .build();
        });

        var out = pautaService.criarPauta(entrada);

        ArgumentCaptor<Pauta> cap = ArgumentCaptor.forClass(Pauta.class);
        verify(pautaRepository).save(cap.capture());
        Pauta enviado = cap.getValue();
        assertThat(enviado.getTitulo()).isEqualTo("Título X");
        assertThat(enviado.getDescricao()).isEqualTo("Descrição Y");

        assertThat(out).isNotNull();
    }

    @Test
    void criarPauta_deveMapearSalvarERetornarDTO() {
        PautaDTO entrada = PautaDTO.builder()
                .titulo("Titulo X")
                .descricao("Descricao Y")
                .build();

        when(pautaRepository.save(any(Pauta.class))).thenAnswer(inv -> {
            Pauta p = inv.getArgument(0);
            p.setId(10L);
            return p;
        });

        PautaDTO resp = pautaService.criarPauta(entrada);

        assertThat(resp.getId()).isEqualTo(10L);
        assertThat(resp.getTitulo()).isEqualTo("Titulo X");
        assertThat(resp.getDescricao()).isEqualTo("Descricao Y");
    }

    @Test
    void listarPautas_deveRetornarListaMapeada() {
        when(pautaRepository.findAll()).thenReturn(List.of(
                Pauta.builder().id(1L).titulo("A").descricao("a").build(),
                Pauta.builder().id(2L).titulo("B").descricao("b").build()
        ));

        List<PautaDTO> lista = pautaService.listarPautas();

        assertThat(lista).hasSize(2);
        assertThat(lista).extracting(PautaDTO::getId).containsExactly(1L, 2L);
        assertThat(lista).extracting(PautaDTO::getTitulo).containsExactly("A", "B");
        assertThat(lista).extracting(PautaDTO::getDescricao).containsExactly("a", "b");
    }
}