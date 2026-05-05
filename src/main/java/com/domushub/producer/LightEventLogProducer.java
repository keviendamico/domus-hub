package com.domushub.producer;

import com.domushub.config.kafka.KafkaConfig;
import com.domushub.model.LightEventLogMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class LightEventLogProducer {

    private final KafkaConfig kafkaConfig;
    private final KafkaTemplate<String, LightEventLogMessage> kafkaTemplate;

    public void send(LightEventLogMessage event) {
        kafkaTemplate.send(kafkaConfig.topics().lightEventLogTopic(), event.chatId(), event).whenComplete((res, ex) -> {
            if (ex != null) {
                log.error("Failed to publish light event log message to kafka - Room: {}", event.room(), ex);
            }
        });
    }
}
