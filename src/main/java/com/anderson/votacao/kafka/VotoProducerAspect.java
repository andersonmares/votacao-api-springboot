package com.anderson.votacao.kafka;

import com.anderson.votacao.dto.VotoDTO;
import com.anderson.votacao.entity.Voto;
import com.anderson.votacao.kafka.dto.VotoEvent;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
public class VotoProducerAspect {

    private final VotoEventProducer producer;

    // Publica evento após métodos do VotoService que recebem VotoDTO e retornam Voto
    @AfterReturning(pointcut = "execution(* com.anderson.votacao.service.VotoService.*(..)) && args(dto,..)", returning = "ret")
    public void publicarAposSalvar(VotoDTO dto, Object ret) {
        if (ret instanceof Voto && dto != null) {
            VotoEvent evento = VotoEvent.builder()
                    .pautaId(dto.getPautaId())
                    .associadoId(dto.getAssociadoId())
                    .voto(dto.getVoto())
                    .dataHora(LocalDateTime.now())
                    .build();
            try {
                producer.publicar(evento);
            } catch (Exception ignore) {
                // log pode ser adicionado se necessário
            }
        }
    }
}