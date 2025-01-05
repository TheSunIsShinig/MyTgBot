package org.example.controller;

import org.example.constant.Constants;
import org.example.constant.UserState;
import org.example.service.AbilityService;
import org.example.service.CarService;
import org.example.service.WeatherService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;
import java.util.Objects;

@Component
public class ResponseHandler {

    private final WeatherService weatherService;
    private final AbilityService repliesService;
    private final CarService carService;
    protected final Map<Long, UserState> chatStates;

    public ResponseHandler(@Lazy SilentSender sender, DBContext db){
        repliesService = new AbilityService(sender, db);
        carService = new CarService(sender, db);
        weatherService = new WeatherService(sender, db);
        chatStates = db.getMap(Constants.CHAT_STATES);
    }

    public void replyToText(long chatId, Message message) {
        if(Objects.equals(message.getText(), "/stop")){
            repliesService.stopChat(chatId);
        }

        switch (chatStates.get(chatId)) {
            case PARAMETERS -> carService.toParameters(chatId, message);
            case AWAITING_CHANGE -> carService.toChangeParameters(chatId, message);
            case AWAITING_CITY -> weatherService.getWeatherDetails(chatId, message);
            default -> repliesService.unexpectedMessage(chatId);
        }
    }

    public void replyToCallBackData(long chatId, String callbackData, Message message ) throws Exception {
        switch(callbackData){
            case "Yes" -> carService.toYesCall(chatId);
            case "No" -> carService.toChangeCall(chatId);
            case "Cars" -> carService.toName(chatId);
            case "Weather" -> weatherService.setCityMessage(chatId);
            default -> carService.toChangeMessage(chatId, callbackData);
        }
    }

    public boolean userIsActive(Long chatId) {
        return chatStates.containsKey(chatId);
    }

}