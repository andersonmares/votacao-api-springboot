package com.anderson.votacao.validator;

import com.anderson.votacao.dto.VotoDTO;
import com.anderson.votacao.exception.BusinessException;
import com.anderson.votacao.service.validator.CpfObrigatorioValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CpfObrigatorioValidatorTest {

    private final CpfObrigatorioValidator validator = new CpfObrigatorioValidator();

    @Test
    void deveLancarExcecaoQuandoCpfForNulo() {
        VotoDTO dto = new VotoDTO(null, 1L, 1, true);
        BusinessException exception = assertThrows(BusinessException.class, () -> validator.validar(dto));
        assertEquals("O campo CPF é obrigatório", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoCpfForVazio() {
        VotoDTO dto = new VotoDTO("", 1L, 1, true);
        BusinessException exception = assertThrows(BusinessException.class, () -> validator.validar(dto));
        assertEquals("O campo CPF é obrigatório", exception.getMessage());
    }

    @Test
    void naoDeveLancarExcecaoQuandoCpfForValido() {
        VotoDTO dto = new VotoDTO("12345678900", 1L, 1, true);
        assertDoesNotThrow(() -> validator.validar(dto));
    }
}
