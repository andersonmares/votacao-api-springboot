package com.anderson.votacao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VotoDTO {

    @NotBlank(message = "CPF é obrigatório")
    private String cpf;

    @NotNull(message = "ID da pauta é obrigatório")
    private Long pautaId;

    @NotNull(message = "associadoId é obrigatório")
    private Integer associadoId;

    @NotNull(message = "voto é obrigatório")
    private Boolean voto;
}