package com.anderson.votacao.repository;

import com.anderson.votacao.entity.Voto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotoRepository extends JpaRepository<Voto, Long> {
    boolean existsByCpfAndPautaId(String cpf, Long pautaId);

    long countByPautaIdAndVoto(Long pautaId, boolean voto);
}
