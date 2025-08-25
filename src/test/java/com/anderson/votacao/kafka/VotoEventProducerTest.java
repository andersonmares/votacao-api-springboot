package com.anderson.votacao.kafka;

import com.anderson.votacao.kafka.dto.VotoEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VotoEventProducerTest {

    @Mock
    private KafkaTemplate<String, VotoEvent> kafkaTemplate;

    @InjectMocks
    private VotoEventProducer producer;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void devePublicarEvento() {
        VotoEvent evento = VotoEvent.builder()
                .pautaId(1L)
                .associadoId(2)
                .voto(true)
                .build();

        producer.publicar(evento);

        // O producer está usando o overload send(topic, key, value)
        verify(kafkaTemplate, times(1))
                .send(any(), any(), ArgumentMatchers.eq(evento));

        verifyNoMoreInteractions(kafkaTemplate);
    }
}