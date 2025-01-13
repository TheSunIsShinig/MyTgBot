package org.example.helper;

import org.example.model.CarDetails;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CarDetailsProcessor {

    public String getCarDetailsString(Long chatId, Map<Long, CarDetails> carDetailsMap) {
        CarDetails carDetails = carDetailsMap.get(chatId);
        return    "Brand: " + carDetails.getBrand() +
                "\nModel: " + carDetails.getModel() +
                "\nStartYear: " + carDetails.getStartYear() +
                "\nEndYear: " + carDetails.getEndYear() +
                "\nStartPrice: " + carDetails.getStartPrice() +
                "\nEndPrice: " + carDetails.getEndPrice();
    }
}