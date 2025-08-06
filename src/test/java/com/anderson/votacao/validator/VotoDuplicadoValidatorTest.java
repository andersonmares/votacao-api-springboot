package com.anderson.votacao.validator;

import com.anderson.votacao.dto.VotoDTO;
import com.anderson.votacao.exception.BusinessException;
import com.anderson.votacao.repository.VotoRepository;
import com.anderson.votacao.service.validator.VotoDuplicadoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VotoDuplicadoValidatorTest {

    private VotoRepository votoRepository;
    private VotoDuplicadoValidator validator;

    @BeforeEach
    void setup() {
        votoRepository = mock(VotoRepository.class);
        validator = new VotoDuplicadoValidator(votoRepository);
    }

    @Test
    void devePermitirQuandoAssociadoNaoVotou() {
        VotoDTO dto = new VotoDTO("123", 1L, 2, true);
        when(votoRepository.existsByPautaIdAndAssociadoId(1L, 2)).thenReturn(false);
        assertDoesNotThrow(() -> validator.validar(dto));
    }

    @Test
    void deveLancarExcecaoQuandoAssociadoJaVotou() {
        VotoDTO dto = new VotoDTO("123", 1L, 2, true);
        when(votoRepository.existsByPautaIdAndAssociadoId(1L, 2)).thenReturn(true);
        BusinessException ex = assertThrows(BusinessException.class, () -> validator.validar(dto));
        assertEquals("Associado já votou nesta pauta", ex.getMessage());
    }
}
