package com.domushub.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "telegram.bot")
public record DomusHubBotConfig(String token, String webhookUrl, String path) {

}
