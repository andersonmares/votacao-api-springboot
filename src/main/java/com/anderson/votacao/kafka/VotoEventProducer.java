package com.anderson.votacao.kafka;

import com.anderson.votacao.kafka.dto.VotoEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VotoEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.topic.votos:votos}")
    private String votosTopic;

    public void publicar(VotoEvent evento) {
        kafkaTemplate.send(votosTopic, String.valueOf(evento.getPautaId()), evento);
    }
}