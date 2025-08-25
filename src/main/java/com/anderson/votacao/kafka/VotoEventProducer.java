package com.anderson.votacao.kafka;

import com.anderson.votacao.kafka.dto.VotoEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.kafka.enabled", havingValue = "true")
@Slf4j
public class VotoEventProducer {

    private final KafkaTemplate<String, VotoEvent> kafkaTemplate;

    @Value("${app.kafka.topic.votos:votos}")
    private String votosTopic;

    public void publicar(VotoEvent evento) {
        String key = String.valueOf(evento.getPautaId());
        kafkaTemplate.send(votosTopic, key, evento);
        log.info("Voto salvo com sucesso!");
    }
}