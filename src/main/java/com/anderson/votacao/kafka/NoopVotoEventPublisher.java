package com.anderson.votacao.kafka;

import com.anderson.votacao.kafka.dto.VotoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.kafka.enabled", havingValue = "false", matchIfMissing = true)
public class NoopVotoEventPublisher implements VotoEventPublisher {
    private static final Logger log = LoggerFactory.getLogger(NoopVotoEventPublisher.class);

    @Override
    public void publicar(VotoEvent evento) {
        // No-op (pode logar em debug se quiser)
        log.debug("Kafka desabilitado. Evento não publicado: {}", evento);
    }
}