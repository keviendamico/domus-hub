package com.domushub.config.kafka;

import lombok.AllArgsConstructor;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@AllArgsConstructor
public class KafkaTopicConfig {

    private final KafkaConfig kafkaConfig;

    @Bean
    public NewTopic lightEventLogTopic() {
        return TopicBuilder.name(kafkaConfig.topics().lightEventLogTopic()).partitions(3).replicas(1).build();
    }
}
