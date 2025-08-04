package com.anderson.votacao.repository;

import com.anderson.votacao.entity.Voto;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotoRepository extends JpaRepository<Voto, Long> {
    boolean existsByCpfAndPautaId(String cpf, Long pautaId);

    long countByPautaIdAndVoto(Long pautaId, boolean voto);

    boolean existsByPautaIdAndAssociadoId(@NotNull(message = "ID da pauta é obrigatório") Long pautaId, Integer associadoId);
}
