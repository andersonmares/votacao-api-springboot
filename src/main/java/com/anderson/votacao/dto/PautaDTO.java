package com.anderson.votacao.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PautaDTO {
    private Long id;

    /** Campo oficial. Aceita também "assunto" como alias para compatibilidade. */
    @NotBlank(message = "titulo é obrigatório")
    @JsonAlias("assunto")
    private String titulo;

    private String descricao;
}