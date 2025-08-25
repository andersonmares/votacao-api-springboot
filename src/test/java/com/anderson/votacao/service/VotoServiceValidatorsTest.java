package com.anderson.votacao.service;

import com.anderson.votacao.dto.VotoDTO;
import com.anderson.votacao.entity.Pauta;
import com.anderson.votacao.exception.BusinessException;
import com.anderson.votacao.kafka.VotoEventProducer;
import com.anderson.votacao.repository.PautaRepository;
import com.anderson.votacao.repository.VotoRepository;
import com.anderson.votacao.service.validator.VotoValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class VotoServiceValidatorsTest {

    @Mock private VotoRepository votoRepository;
    @Mock private PautaRepository pautaRepository;
    @Mock private VotoEventProducer votoEventProducer;
    @Mock private VotoValidator validatorQueFalha;

    @Test
    void devePropagarExcecaoDoValidator() {
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(
                Pauta.builder().id(1L).titulo("T").descricao("D").build()
        ));

        doThrow(new BusinessException("CPF inválido"))
                .when(validatorQueFalha)
                .validate(any(), any());

        VotoService service = new VotoService(
                votoRepository, pautaRepository, List.of(validatorQueFalha), votoEventProducer
        );

        VotoDTO dto = VotoDTO.builder()
                .cpf("000")
                .pautaId(1L)
                .associadoId(9)
                .voto(true)
                .build();

        assertThatThrownBy(() -> service.salvar(dto))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("CPF inválido");
    }

    @Test
    void deveContarVotosSimENao() {
        VotoService service = new VotoService(votoRepository, pautaRepository, List.of(), votoEventProducer);

        when(votoRepository.countByPautaIdAndVoto(1L, true)).thenReturn(3L);
        when(votoRepository.countByPautaIdAndVoto(1L, false)).thenReturn(2L);

        assertThat(service.contarVotosSim(1L)).isEqualTo(3L);
        assertThat(service.contarVotosNao(1L)).isEqualTo(2L);
    }
}