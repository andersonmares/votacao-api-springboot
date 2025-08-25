package com.anderson.votacao.validator;

import com.anderson.votacao.dto.VotoDTO;
import com.anderson.votacao.service.validator.AssociadoObrigatorioValidator;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AssociadoObrigatorioValidatorTest {

    private final AssociadoObrigatorioValidator validator = new AssociadoObrigatorioValidator();

    @Test
    void deveLancarQuandoAssociadoIdNulo() {
        VotoDTO dto = VotoDTO.builder()
                .cpf("111")
                .pautaId(1L)
                .associadoId(null)
                .voto(true)
                .build();

        assertThrows(ResponseStatusException.class, () -> validator.validar(dto));
    }

    @Test
    void naoDeveLancarQuandoAssociadoIdPresente() {
        VotoDTO dto = VotoDTO.builder()
                .cpf("111")
                .pautaId(1L)
                .associadoId(7)
                .voto(true)
                .build();

        assertDoesNotThrow(() -> validator.validar(dto));
    }
}