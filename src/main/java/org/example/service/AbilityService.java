package org.example.service;

import org.example.constant.Buttons;
import org.example.constant.Constants;
import org.example.constant.UserState;
import org.example.helper.MessageSender;
//import org.example.model.BotUser;
//import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Map;

import static org.example.constant.Constants.*;


@Component
public class AbilityService {

    private final Buttons buttons = new Buttons();

//    private BotUser botUser;
//    private UserRepository userRepository;
    private final CarService carService;
    private final MessageSender messageSender;
    private final Map<Long, UserState> chatStates;

    @Autowired
    public AbilityService(@Lazy SilentSender sender, DBContext db) {
        chatStates = db.getMap(Constants.CHAT_STATES);
        messageSender = new MessageSender(sender);
        carService = new CarService(sender, db);
//        botUser = new BotUser();
    }

    public void toStart(long chatId, User user) {
//        botUser.setId(user.getId());
//        botUser.setUsername(user.getUserName());
//        userRepository.save(botUser);
        messageSender.sendMessageWithKeyboard(chatId,START_TEXT, buttons.CarsOrWeather());
    }

    public void toChange(long chatId){
        carService.toChangeCall(chatId);
    }

    public void stopChat(long chatId) {
        messageSender.sendMessage(chatId, STOP_TEXT);
        chatStates.remove(chatId);
    }

    public void toShow(long chatId){
        carService.carDetails(chatId);
    }

    public void unexpectedMessage(long chatId) {
        messageSender.sendMessage(chatId, UNEXPECTED_MESSAGE);
    }

}
