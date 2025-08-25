package com.anderson.votacao.validator;

import com.anderson.votacao.dto.VotoDTO;
import com.anderson.votacao.entity.Pauta;
import com.anderson.votacao.service.validator.VotoValidator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VotoValidatorDefaultMethodTest {

    static class DummyValidator implements VotoValidator {
        boolean called = false;
        @Override public void validar(VotoDTO dto) { called = true; }
    }

    @Test
    void defaultValidateDeveDelegarParaValidar() {
        DummyValidator v = new DummyValidator();
        v.validate(VotoDTO.builder().build(), Pauta.builder().id(1L).build());
        assertThat(v.called).isTrue();
    }
}