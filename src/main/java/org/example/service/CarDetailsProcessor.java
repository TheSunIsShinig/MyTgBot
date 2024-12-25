package org.example.service;

import org.example.model.CarDetails;

import java.util.Map;

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