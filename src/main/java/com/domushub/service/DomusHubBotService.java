package com.domushub.service;

import com.domushub.device.light.ShellyDeviceInterface;
import com.domushub.model.RoomEnum;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

@Component
@AllArgsConstructor
public class DomusHubBotService {

    private final ShellyDeviceInterface shellyDevice;
    private final TelegramClient telegramClient;

    public BotApiMethod<?> handle(Update update) {
        if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            String chatId = String.valueOf(update.getCallbackQuery().getMessage().getChatId());
            String callbackId = update.getCallbackQuery().getId();
            return handleCallback(data, chatId, callbackId);
        }
        String text = update.getMessage().getText();
        String chatId = String.valueOf(update.getMessage().getChatId());
        return switch (text) {
            case "/lights" -> {
                SendMessage message = new SendMessage(chatId, "Cosa vuoi fare?");
                message.setReplyMarkup(new InlineKeyboardMarkup(
                        List.of(new InlineKeyboardRow(
                                InlineKeyboardButton.builder().text("Sala da pranzo - Accendi").callbackData("living_room:on").build()
                            ),
                            new InlineKeyboardRow(
                                    InlineKeyboardButton.builder().text("Sala da pranzo - Spegni").callbackData("living_room:off").build()
                            )
                        )
                ));
                yield message;
            }
            default -> new SendMessage(chatId, "Comando non riconosciuto");
        };
    }

    private void answerCallback(String callbackId) {
        try {
            telegramClient.execute(AnswerCallbackQuery.builder().callbackQueryId(callbackId).build());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private BotApiMethod<?> handleCallback(String data, String chatId, String callbackId) {
        String[] dataSplit = data.split(":");
        if (dataSplit.length != 2) {
            return AnswerCallbackQuery.builder().callbackQueryId(callbackId).build();
        }
        RoomEnum room = RoomEnum.fromName(dataSplit[0]);
        String action = dataSplit[1];
        return switch (action) {
            case "on" -> {
                shellyDevice.turnOn(room);
                answerCallback(callbackId);
                yield new SendMessage(chatId, "💡");
            }
            case "off" -> {
                shellyDevice.turnOff(room);
                answerCallback(callbackId);
                yield new SendMessage(chatId, "🌑");
            }
            default -> AnswerCallbackQuery.builder().callbackQueryId(callbackId).build();
        };
    }
}
