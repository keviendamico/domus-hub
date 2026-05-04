package com.domushub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.webhook.starter.SpringTelegramWebhookBot;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BotWebhookController {

    private final SpringTelegramWebhookBot bot;
    private final ObjectMapper objectMapper;

    @PostMapping("/webhook")
    public BotApiMethod<?> onUpdateReceived(@RequestBody String requestBody) {
        try {
            Update update = objectMapper.readValue(requestBody, Update.class);
            return bot.consumeUpdate(update);
        } catch (Exception e) {
            log.error("Failed to parse Telegram update: {}", e.getMessage());
            return null;
        }
    }
}
