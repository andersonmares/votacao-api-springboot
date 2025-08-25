package com.anderson.votacao.service;

import com.anderson.votacao.dto.VotoDTO;
import com.anderson.votacao.entity.Pauta;
import com.anderson.votacao.entity.Voto;
import com.anderson.votacao.kafka.VotoEventProducer;
import com.anderson.votacao.repository.PautaRepository;
import com.anderson.votacao.repository.VotoRepository;
import com.anderson.votacao.service.validator.VotoValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VotoServiceValidateCallTest {

    @Mock private VotoRepository votoRepository;
    @Mock private PautaRepository pautaRepository;
    @Mock private VotoEventProducer votoEventProducer;
    @Mock private VotoValidator validator;

    @Test
    void salvar_deveInvocarValidateComDtoEPauta() {
        Pauta pauta = Pauta.builder().id(1L).titulo("T").descricao("D").build();
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));
        when(votoRepository.save(ArgumentMatchers.any(Voto.class))).thenAnswer(inv -> inv.getArgument(0));

        VotoService svc = new VotoService(votoRepository, pautaRepository, List.of(validator), votoEventProducer);

        VotoDTO dto = VotoDTO.builder().cpf("11122233344").pautaId(1L).associadoId(9).voto(true).build();
        svc.salvar(dto);

        verify(validator).validate(dto, pauta);
    }
}