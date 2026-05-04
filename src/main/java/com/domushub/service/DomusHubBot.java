package com.domushub.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class DomusHubBot {

    public BotApiMethod<?> handle(Update update) {
        String text = update.getMessage().getText();
        String chatId = String.valueOf(update.getMessage().getChatId());
        return switch (text) {
            case "/on" -> new SendMessage(chatId, "💡");
            case "/off" -> new SendMessage(chatId, "🌑 ");
            default -> new SendMessage(chatId, "Comando non riconosciuto");
        };
    }
}
