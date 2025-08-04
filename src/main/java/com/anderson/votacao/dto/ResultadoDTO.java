package com.anderson.votacao.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoDTO {
    private long votosSim;
    private long votosNao;
}
