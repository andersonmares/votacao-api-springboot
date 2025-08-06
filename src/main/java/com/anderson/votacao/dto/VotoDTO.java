package com.anderson.votacao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VotoDTO {

    @NotBlank(message = "CPF é obrigatório")
    private String cpf;

    @NotNull(message = "ID da pauta é obrigatório")
    private Long pautaId;

    @NotNull(message = "ID do associado é obrigatório")
    private Integer associadoId;

    @NotNull(message = "Voto (true/false) é obrigatório")
    private Boolean voto;

}