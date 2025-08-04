package com.anderson.votacao.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PautaDTO {
    private Long id;
    private String titulo;
    private String descricao;
}
