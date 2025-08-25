package com.anderson.votacao.service;

import com.anderson.votacao.dto.SessaoDTO;
import com.anderson.votacao.exception.BusinessException;
import com.anderson.votacao.repository.PautaRepository;
import com.anderson.votacao.repository.SessaoRepository;
import com.anderson.votacao.mapper.SessaoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessaoServiceErroTest {

    @Mock private SessaoRepository sessaoRepository;
    @Mock private PautaRepository pautaRepository;
    @Mock private SessaoMapper sessaoMapper;

    @InjectMocks
    private SessaoService sessaoService;

    @Test
    void deveLancarBusinessException_QuandoPautaNaoEncontrada() {
        when(pautaRepository.findById(anyLong())).thenReturn(Optional.empty());

        SessaoDTO dto = SessaoDTO.builder().pautaId(999L).build();

        assertThatThrownBy(() -> sessaoService.abrirSessao(dto))
                .isInstanceOf(BusinessException.class);
    }
}