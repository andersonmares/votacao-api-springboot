package com.anderson.votacao.repository;

import com.anderson.votacao.entity.Sessao;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SessaoRepository extends JpaRepository<Sessao, Long> {
    Optional<Sessao> findByPautaId(Long pautaId);

    boolean existsByPautaIdAndDataHoraFimAfter(@NotNull(message = "ID da pauta é obrigatório") Long pautaId, LocalDateTime now);
}
