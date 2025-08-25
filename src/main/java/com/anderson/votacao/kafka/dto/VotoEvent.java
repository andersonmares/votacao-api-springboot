package com.anderson.votacao.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VotoEvent {
    private Long pautaId;
    private Integer associadoId;
    private Boolean voto;
    private LocalDateTime dataHora;
}