package com.anderson.votacao.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessaoResponseDTO {
    private Long id;
    private Long pautaId;
    private Integer duracaoMinutos;
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;
}