package com.anderson.votacao.kafka;

import com.anderson.votacao.kafka.dto.VotoEvent;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = { "votos" })
@TestPropertySource(properties = {
        // garante que o Producer (KafkaTemplate) usa o broker embutido
        "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}"
})
class VotoEventProducerIntegrationTest {

    @Autowired
    private VotoEventProducer producer;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @Test
    void devePublicarEventoDeVoto() {
        // --- Consumer para ler do tópico "votos"
        Map<String, Object> consumerProps =
                KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafka);
        // Não confie só na prop; configure no deserializador também
        JsonDeserializer<VotoEvent> valueDeserializer = new JsonDeserializer<>(VotoEvent.class, false);
        valueDeserializer.addTrustedPackages("*");

        DefaultKafkaConsumerFactory<String, VotoEvent> cf =
                new DefaultKafkaConsumerFactory<>(consumerProps, new StringDeserializer(), valueDeserializer);

        var consumer = cf.createConsumer();
        embeddedKafka.consumeFromAnEmbeddedTopic(consumer, "votos");

        // --- Publica
        VotoEvent evento = VotoEvent.builder()
                .pautaId(99L).associadoId(7).voto(true)
                .build();
        producer.publicar(evento);

        // --- Lê 1 registro do tópico (aguarda até ~5s)
        var record = KafkaTestUtils.getSingleRecord(consumer, "votos");
        assertThat(record).isNotNull();
        VotoEvent recebido = record.value();

        assertThat(recebido).isNotNull();
        assertThat(recebido.getPautaId()).isEqualTo(99L);
        assertThat(recebido.getAssociadoId()).isEqualTo(7);

        consumer.close();
    }
}