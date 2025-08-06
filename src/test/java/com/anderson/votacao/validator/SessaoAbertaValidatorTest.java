package com.anderson.votacao.validator;

import com.anderson.votacao.dto.VotoDTO;
import com.anderson.votacao.exception.BusinessException;
import com.anderson.votacao.repository.SessaoRepository;
import com.anderson.votacao.service.validator.SessaoAbertaValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessaoAbertaValidatorTest {

    private SessaoRepository sessaoRepository;
    private SessaoAbertaValidator validator;

    @BeforeEach
    void setup() {
        sessaoRepository = mock(SessaoRepository.class);
        validator = new SessaoAbertaValidator(sessaoRepository);
    }

    @Test
    void devePermitirQuandoSessaoEstiverAberta() {
        VotoDTO dto = new VotoDTO("123", 1L, 2, true);
        when(sessaoRepository.existsByPautaIdAndDataHoraFimAfter(eq(1L), any(LocalDateTime.class))).thenReturn(true);
        assertDoesNotThrow(() -> validator.validar(dto));
    }

    @Test
    void deveLancarExcecaoQuandoSessaoNaoEstiverAberta() {
        VotoDTO dto = new VotoDTO("123", 1L, 2, true);
        when(sessaoRepository.existsByPautaIdAndDataHoraFimAfter(eq(1L), any(LocalDateTime.class))).thenReturn(false);
        BusinessException ex = assertThrows(BusinessException.class, () -> validator.validar(dto));
        assertEquals("Sessão não está aberta", ex.getMessage());
    }
}
