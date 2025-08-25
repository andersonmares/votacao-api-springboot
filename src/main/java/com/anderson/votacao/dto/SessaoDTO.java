package com.anderson.votacao.dto;

import jakarta.validation.constraints.Min;
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

    /** Campo legado mantido por compatibilidade. */
    @Min(value = 1, message = "duracao deve ser >= 1")
    private Integer duracao;

    /** Preferível: duração em minutos. */
    @Min(value = 1, message = "duracaoMinutos deve ser >= 1")
    private Integer duracaoMinutos;

    public long resolveDuracaoMinutosOrDefault(long defaultMinutes) {
        Integer val = duracaoMinutos != null ? duracaoMinutos : duracao;
        return val != null ? val.longValue() : defaultMinutes;
    }
}