package org.example.service;

import org.example.model.*;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void addCarToUser(long userId, CarDetails carDetails) {
        BotUser botUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("BotUser not found"));
        carDetails.setBotUser(botUser);
        botUser.getCarDetails().add(carDetails);
        userRepository.save(botUser);
    }
}
