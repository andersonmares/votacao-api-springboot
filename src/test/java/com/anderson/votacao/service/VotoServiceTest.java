package com.anderson.votacao.service;

import com.anderson.votacao.dto.VotoDTO;
import com.anderson.votacao.entity.Voto;
import com.anderson.votacao.exception.BusinessException;
import com.anderson.votacao.repository.VotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VotoServiceTest {

    @Mock
    private VotoRepository votoRepository;

    @InjectMocks
    private VotoService votoService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveSalvarVotoQuandoNaoExistir() {
        VotoDTO dto = new VotoDTO("12312312310",1L, Boolean.TRUE,  1, "SIM");
        when(votoRepository.existsByPautaIdAndAssociadoId(1L, 1)).thenReturn(false);
        Voto voto = new Voto(dto);
        when(votoRepository.save(any(Voto.class))).thenReturn(voto);

        Voto resultado = votoService.votar(dto);
        assertNotNull(resultado);
        verify(votoRepository).save(any(Voto.class));
    }

    @Test
    void deveLancarExceptionQuandoJaVotou() {
        VotoDTO dto = new VotoDTO("12312313210", 1L, Boolean.FALSE, 1,"NAO");
        when(votoRepository.existsByPautaIdAndAssociadoId(1L, 1)).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class, () -> votoService.votar(dto));
        assertEquals("Associado já votou nesta pauta", ex.getMessage());
    }
}