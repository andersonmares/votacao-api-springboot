package com.anderson.votacao.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "voto",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_voto_pauta_associado",
                columnNames = { "pauta_id", "associado_id" }
        )
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cpf;

    @Column(name = "associado_id", nullable = false)
    private Integer associadoId;

    @Column(nullable = false)
    private Boolean voto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pauta_id", nullable = false)
    private Pauta pauta;

}