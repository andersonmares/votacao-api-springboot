package com.anderson.votacao.repository;

import com.anderson.votacao.entity.Voto;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VotoRepository extends JpaRepository<Voto, Long> {
    boolean existsByCpfAndPautaId(String cpf, Long pautaId);

    @Query("SELECT COUNT(v) FROM Voto v WHERE v.pauta.id = :pautaId AND v.voto = :voto")
    long countByPautaIdAndVoto(@Param("pautaId") Long pautaId, @Param("voto") boolean voto);


    boolean existsByPautaIdAndAssociadoId(@NotNull(message = "ID da pauta é obrigatório") Long pautaId, Integer associadoId);
}
