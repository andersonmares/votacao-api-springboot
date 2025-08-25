package com.anderson.votacao.kafka;

import com.anderson.votacao.kafka.dto.VotoEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.kafka.enabled", havingValue = "true")
public class KafkaVotoEventPublisher implements VotoEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.topic.votos:votos}")
    private String votosTopic;

    @Override
    public void publicar(VotoEvent evento) {
        kafkaTemplate.send(votosTopic, String.valueOf(evento.getPautaId()), evento);
    }
}