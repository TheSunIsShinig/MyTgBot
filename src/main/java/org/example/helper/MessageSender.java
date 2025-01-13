package org.example.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class MessageSender {

    private final SilentSender sender;

    @Autowired
    public MessageSender(@Lazy SilentSender sender) {
        this.sender = sender;
    }

    public void sendMessage(Long chatId, String messageText) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(messageText);
        sender.execute(sendMessage);
    }

    public void sendMessageWithKeyboard(Long chatId, String messageText, List<List<String>> buttons) {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for (List<String> rowLabels : buttons) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            for (String label : rowLabels) {
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(label);
                button.setCallbackData(label);
                row.add(button);
            }
            keyboard.add(row);
        }

        inlineKeyboard.setKeyboard(keyboard);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(messageText);
        sendMessage.setReplyMarkup(inlineKeyboard);

        sender.execute(sendMessage);
    }

    public void removeKeyboard(Long chatId, String messageText) {
        ReplyKeyboardRemove removeKeyboard = new ReplyKeyboardRemove();
        removeKeyboard.setRemoveKeyboard(true);
    }
}