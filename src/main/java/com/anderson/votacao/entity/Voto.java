package com.anderson.votacao.entity;

import com.anderson.votacao.dto.VotoDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "voto", uniqueConstraints = {
        @UniqueConstraint(name = "uk_voto_pauta_associado", columnNames = {"pauta_id", "associado_id"})
})
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cpf;

    private Boolean voto;

    @ManyToOne
    private Pauta pauta;

    private Integer associadoId;

    public Voto(VotoDTO dto) {
    }
}
