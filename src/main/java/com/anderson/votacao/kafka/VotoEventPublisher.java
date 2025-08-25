package com.anderson.votacao.kafka;

import com.anderson.votacao.kafka.dto.VotoEvent;

public interface VotoEventPublisher {
    void publicar(VotoEvent evento);
}