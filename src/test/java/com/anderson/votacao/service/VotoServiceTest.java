package com.anderson.votacao.service;

import com.anderson.votacao.dto.VotoDTO;
import com.anderson.votacao.entity.Pauta;
import com.anderson.votacao.entity.Voto;
import com.anderson.votacao.exception.BusinessException;
import com.anderson.votacao.repository.PautaRepository;
import com.anderson.votacao.repository.VotoRepository;
import com.anderson.votacao.service.validator.VotoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VotoServiceTest {

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private VotoValidator validador1;

    @Mock
    private VotoValidator validador2;

    @InjectMocks
    private VotoService votoService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        votoService = new VotoService(
                votoRepository,
                pautaRepository,
                List.of(validador1, validador2) // Injetando lista de validadores simulados
        );
    }

    @Test
    void deveSalvarVotoQuandoNaoExistir() {
        VotoDTO dto = new VotoDTO("12312312310", 1L, 1, Boolean.TRUE);

        // Nenhum validador lança exceção
        doNothing().when(validador1).validar(dto);
        doNothing().when(validador2).validar(dto);

        when(pautaRepository.findById(1L))
                .thenReturn(Optional.of(new Pauta(1L, "Pauta Teste", "teste")));

        when(votoRepository.save(any(Voto.class)))
                .thenAnswer(invocation -> invocation.getArgument(0)); // retorna o próprio voto salvo

        Voto resultado = votoService.votar(dto);

        assertNotNull(resultado);
        assertEquals(dto.getCpf(), resultado.getCpf());
        assertEquals(dto.getAssociadoId(), resultado.getAssociadoId());
        assertEquals(dto.getVoto(), resultado.getVoto());

        verify(validador1).validar(dto);
        verify(validador2).validar(dto);
        verify(votoRepository).save(any(Voto.class));
    }

    @Test
    void deveLancarExceptionQuandoAlgumValidadorFalhar() {
        VotoDTO dto = new VotoDTO("12312313210", 1L, 1, Boolean.FALSE);

        // Simula que o segundo validador lança exceção
        doNothing().when(validador1).validar(dto);
        doThrow(new BusinessException("Erro no validador")).when(validador2).validar(dto);

        BusinessException ex = assertThrows(BusinessException.class, () -> votoService.votar(dto));
        assertEquals("Erro no validador", ex.getMessage());

        verify(validador1).validar(dto);
        verify(validador2).validar(dto);
        verifyNoInteractions(pautaRepository);
        verify(votoRepository, never()).save(any());
    }
}
