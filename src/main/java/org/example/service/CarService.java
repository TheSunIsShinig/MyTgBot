package org.example.service;

import org.example.constant.Buttons;
import org.example.constant.Constants;
import org.example.constant.UserState;
import org.example.helper.AutoRiaParser;
import org.example.helper.CarDetailsProcessor;
import org.example.helper.CarStorage;
import org.example.helper.MessageSender;
import org.example.model.AutoRiaCars;
//import org.example.model.BotUser;
import org.example.model.CarDetails;
//import org.example.repository.CarDetailsRepository;
//import org.example.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.example.constant.Constants.*;
import static org.example.constant.UserState.AWAITING_CHANGE;
import static org.example.constant.UserState.PARAMETERS;

@Component
public class CarService {

//    private CarDetailsRepository carDetailsRepository;
//    private UserRepository userRepository;
    private final CarDetails carDetails = new CarDetails();
    private Map<Long, CarDetails> carDetailsMap = new HashMap<>();
    private final CarStorage carStorage = new CarStorage();
    private final CarDetailsProcessor carDetailsProcessor = new CarDetailsProcessor();
    private final AutoRiaParser autoRiaParser = new AutoRiaParser();
    private final Buttons buttons = new Buttons();

    private final Map<Long, UserState> chatStates;
    private final Map<Long, UserState> carSetCallData;
    private final MessageSender messageSender;

    public CarService(@Lazy SilentSender sender, DBContext db){
        messageSender = new MessageSender(sender);
        chatStates = db.getMap(Constants.CHAT_STATES);
        carSetCallData = db.getMap(Constants.CAR_SET_DATA);
    }

    //
    public void toName(long chatId) {
        messageSender.sendMessage(chatId, PARAMETERS_TEXT);
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

//        //
//        BotUser user = userRepository.findById(chatId).orElseThrow(() -> new RuntimeException("User not found"));
//        carDetails.setBotUser(user);
//        carDetailsRepository.save(carDetails);
//        //

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
        carSetCallData.put(chatId, UserState.fromCallbackData(callbackData));
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

    public void carDetails(long chatId){
        carDetailsMap = carStorage.loadCarDetails();
        messageSender.sendMessage(chatId, carDetailsProcessor.getCarDetailsString(chatId, carDetailsMap));
    }
}
