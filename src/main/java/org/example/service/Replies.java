package org.example.service;

import org.example.constant.Buttons;
import org.example.constant.Constants;
import org.example.constant.UserState;
import org.example.model.AutoRiaCars;
import org.example.model.CarDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.example.constant.Constants.*;
import static org.example.constant.UserState.*;


@Service
public class Replies {

    private Map<Long, CarDetails> carDetailsMap = new HashMap<>();
    private final CarDetails carDetails = new CarDetails();
    private final CarStorage carStorage = new CarStorage();
    private final CarDetailsProcessor carDetailsProcessor = new CarDetailsProcessor();
    private final Buttons buttons = new Buttons();
    private final AutoRiaParser autoRiaParser = new AutoRiaParser();

    private final MessageSender messageSender;
    private final Map<Long, UserState> chatStates;
    private final Map<Long, UserState> carSetCallData;

    @Autowired
    public Replies(@Lazy SilentSender sender, DBContext db) {
        chatStates = db.getMap(Constants.CHAT_STATES);
        carSetCallData = db.getMap(Constants.CAR_SET_DATA);
        messageSender = new MessageSender(sender);
    }

    public void toName(long chatId, Message message) {
        messageSender.sendMessage(chatId,
                "Hello " + message.getText() + "\nWrite the parameters of the machine\n" +
                "Brand, Model, StartPrice, EndPrice, StartYear, EndYear");
        chatStates.put(chatId,PARAMETERS);
    }

    public void toParameters(long chatId, Message message){
        String[] parts = message.getText().split(", ");
        carDetails.setBrand(parts[0]);
        carDetails.setModel(parts[1]);
        carDetails.setStartYear(parts[2]);
        carDetails.setEndYear(parts[3]);
        carDetails.setStartPrice(parts[4]);
        carDetails.setEndPrice(parts[5]);

        carDetailsMap.put(chatId,carDetails);
        carStorage.saveCarDetails(carDetailsMap);

        chatStates.remove(chatId);

        messageSender.sendMessage(chatId, carDetailsProcessor.getCarDetailsString(chatId, carDetailsMap));
        messageSender.sendMessageWithKeyboard(chatId, YES_NO, buttons.YesNoButtons());
    }

    public void toYesCall(long chatId) throws Exception {
        carDetailsMap = carStorage.loadCarDetails();
        List<AutoRiaCars> cars = autoRiaParser.parse(chatId, carDetailsMap);
        for(AutoRiaCars car : cars){
            messageSender.sendMessage(chatId,car.toString());
        }
    }

    public void toChangeCall(long chatId){
        messageSender.sendMessageWithKeyboard(chatId, CHANGE_PARAMETERS, buttons.ChangeButtons());
        chatStates.put(chatId,AWAITING_CHANGE);
    }

    public void toChangeMessage(long chatId, String callbackData){
        messageSender.sendMessage(chatId, "write "+ callbackData);
        carSetCallData.put(chatId,UserState.fromCallbackData(callbackData));
    }

    public void toChangeParameters(long chatId,  Message message){
        carDetailsMap = carStorage.loadCarDetails();
        switch (carSetCallData.get(chatId)){
            case BRAND_SET ->  carDetailsMap.get(chatId).setBrand(message.getText());
            case MODEL_SET ->  carDetailsMap.get(chatId).setModel(message.getText());
            case START_YEAR_SET -> carDetailsMap.get(chatId).setStartYear(message.getText());
            case END_YEAR_SET ->  carDetailsMap.get(chatId).setEndYear(message.getText());
            case START_PRICE_SET -> carDetailsMap.get(chatId).setStartPrice(message.getText());
            case END_PRICE_SET -> carDetailsMap.get(chatId).setEndPrice(message.getText());
        }
        carStorage.saveCarDetails(carDetailsMap);
        messageSender.sendMessage(chatId, carDetailsProcessor.getCarDetailsString(chatId, carDetailsMap));
        messageSender.sendMessageWithKeyboard(chatId, YES_NO, buttons.YesNoButtons());
        chatStates.remove(chatId);
    }

    public void toStart(long chatId) {
        messageSender.sendMessage(chatId,START_TEXT);
        chatStates.put(chatId, AWAITING_NAME);
    }

    public void stopChat(long chatId) {
        messageSender.sendMessage(chatId, STOP_TEXT);
        chatStates.remove(chatId);
    }

    public void toShow(long chatId){
        carDetailsMap = carStorage.loadCarDetails();
        messageSender.sendMessage(chatId, carDetailsProcessor.getCarDetailsString(chatId, carDetailsMap));
    }

    public void unexpectedMessage(long chatId) {
        messageSender.sendMessage(chatId, UNEXPECTED_MESSAGE);
    }
}
