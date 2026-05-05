package com.domushub.consumer;

import com.domushub.model.LightEventLogMessage;
import com.domushub.service.LightEventLogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class LightEventLogConsumer {

    private final LightEventLogService lightEventLogService;

    @KafkaListener(topics = "${domus-hub.kafka.topics.light-event-log-topic}", groupId = "${domus-hub.kafka.groups.light-event-log-group-id}")
    public void consume(LightEventLogMessage event) {
        lightEventLogService.createOrUpdateLightEntities(event.chatId(), event.room(), event.action());
    }
}
