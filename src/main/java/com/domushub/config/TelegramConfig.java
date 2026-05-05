package com.domushub.config;

import com.domushub.service.DomusHubBotService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.updates.DeleteWebhook;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.webhook.starter.SpringTelegramWebhookBot;

@Slf4j
@Configuration
public class TelegramConfig {

    @Bean
    public TelegramClient telegramClient(DomusHubBotConfig domusHubBotConfig) {
        return new OkHttpTelegramClient(domusHubBotConfig.token());
    }

    @Bean
    public SpringTelegramWebhookBot telegramWebhookBot(DomusHubBotConfig domusHubBotConfig, TelegramClient client, DomusHubBotService bot) {
        return SpringTelegramWebhookBot
                .builder()
                .botPath(domusHubBotConfig.path())
                .updateHandler(bot::handle)
                .setWebhook(() -> {
                    String url = domusHubBotConfig.webhookUrl() + domusHubBotConfig.path();
                    SetWebhook webhook = SetWebhook.builder().url(url).build();
                    log.info("Registering Telegram webhook at {}", url);
                    try {
                        client.execute(webhook);
                        log.info("Webhook registered successfully");
                    } catch (TelegramApiException e) {
                        log.error("Failed to register Telegram webhook", e);
                        throw new RuntimeException(e);
                    }
                })
                .deleteWebhook(() -> {
                    DeleteWebhook webhook = DeleteWebhook.builder().build();
                    log.info("Deleting Telegram webhook");
                    try {
                        client.execute(webhook);
                        log.info("Webhook deleted successfully");
                    } catch (TelegramApiException e) {
                        log.error("Failed to delete Telegram webhook", e);
                    }
                })
                .build();
    }
}
