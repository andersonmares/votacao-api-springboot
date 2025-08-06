package com.anderson.votacao.validator;

import com.anderson.votacao.client.FakeCpfClient;
import com.anderson.votacao.client.CpfStatus;
import com.anderson.votacao.dto.VotoDTO;
import com.anderson.votacao.exception.BusinessException;
import com.anderson.votacao.service.validator.CpfStatusValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CpfStatusValidatorTest {

    private FakeCpfClient cpfClient;
    private CpfStatusValidator validator;

    @BeforeEach
    void setup() {
        cpfClient = mock(FakeCpfClient.class);
        validator = new CpfStatusValidator(cpfClient);
    }

    @Test
    void deveLancarExcecaoQuandoCpfEstiverInapto() {
        when(cpfClient.verificarCpf("12345678900")).thenReturn(CpfStatus.UNABLE_TO_VOTE);
        VotoDTO dto = new VotoDTO("12345678900", 1L, 1, true);

        BusinessException exception = assertThrows(BusinessException.class, () -> validator.validar(dto));
        assertEquals("Associado não está habilitado para votar", exception.getMessage());
    }

    @Test
    void naoDeveLancarExcecaoQuandoCpfEstiverApto() {
        when(cpfClient.verificarCpf("12345678900")).thenReturn(CpfStatus.ABLE_TO_VOTE);
        VotoDTO dto = new VotoDTO("12345678900", 1L, 1, true);

        assertDoesNotThrow(() -> validator.validar(dto));
    }

    @Test
    void deveLancarExcecaoGenericaQuandoOcorrerErroNoCliente() {
        when(cpfClient.verificarCpf("12345678900")).thenThrow(new RuntimeException("Erro inesperado"));
        VotoDTO dto = new VotoDTO("12345678900", 1L, 1, true);

        BusinessException exception = assertThrows(BusinessException.class, () -> validator.validar(dto));
        assertEquals("Erro na validação de CPF", exception.getMessage());
    }
}
