package com.anderson.votacao.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessaoDTO {

    @NotNull(message = "pautaId é obrigatório")
    private Long pautaId;

    private Integer duracaoMinutos;

    private Integer duracao;
}
