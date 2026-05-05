package com.domushub.config.kafka;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "domus-hub.kafka")
public record KafkaConfig(Topics topics, Groups groups) {
    public record Topics(String lightEventLogTopic) {}
    public record Groups(String lightEventLogGroupId) {}
}
