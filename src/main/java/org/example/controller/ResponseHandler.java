package org.example.controller;

import org.example.constant.Constants;
import org.example.constant.UserState;
import org.example.service.Replies;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;

@Controller
public class ResponseHandler {

    protected final Map<Long, UserState> chatStates;
    private final Replies reply;

    public ResponseHandler(@Lazy SilentSender sender, DBContext db){
        this.reply = new Replies(sender, db);
        chatStates = db.getMap(Constants.CHAT_STATES);
    }

    public void replyToButtons(long chatId, Message message) {
        if (message.getText().equalsIgnoreCase("/stop")) {
            reply.stopChat(chatId);
        }

        switch (chatStates.get(chatId)) {
            case AWAITING_NAME -> reply.toName(chatId, message);
            case PARAMETERS -> reply.toParameters(chatId, message);
            case AWAITING_CHANGE -> reply.toChangeParameters(chatId, message);
            default -> reply.unexpectedMessage(chatId);
        }
    }

    public void replyToCallBackData(long chatId, String callbackData, Message message ) throws Exception {
        switch(callbackData){
            case "Yes" -> reply.toYesCall(chatId);
            case "No" -> reply.toChangeCall(chatId);
            default -> reply.toChangeMessage(chatId, callbackData);
        }
    }

    public boolean userIsActive(Long chatId) {
        return chatStates.containsKey(chatId);
    }

}