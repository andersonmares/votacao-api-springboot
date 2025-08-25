// src/main/java/com/anderson/votacao/kafka/VotoProducerAspect.java
package com.anderson.votacao.kafka;

import com.anderson.votacao.dto.VotoDTO;
import com.anderson.votacao.entity.Voto;
import com.anderson.votacao.kafka.dto.VotoEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class VotoProducerAspect {

    private final VotoEventPublisher publisher;

    // Publica somente após o salvar (evita interceptar outros métodos)
    @AfterReturning(
            pointcut = "execution(* com.anderson.votacao.service.VotoService.salvar(..)) && args(dto,..)",
            returning = "ret")
    public void publicarAposSalvar(VotoDTO dto, Object ret) {
        log.info("VOTO PUBLICAR SALVAR");
        if (ret instanceof Voto && dto != null) {
            VotoEvent evento = VotoEvent.builder()
                    .pautaId(dto.getPautaId())
                    .associadoId(dto.getAssociadoId())
                    .voto(dto.getVoto())
                    .dataHora(LocalDateTime.now())
                    .build();
            publisher.publicar(evento);
            log.info("Voto salva com sucesso!");
        }
    }
}